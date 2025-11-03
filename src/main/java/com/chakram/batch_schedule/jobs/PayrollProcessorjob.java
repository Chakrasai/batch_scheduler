package com.chakram.batch_schedule.jobs;

import com.chakram.batch_schedule.service.CsvPayrollService;
import com.chakram.batch_schedule.model.PayrollbatchLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Component
public class PayrollProcessorjob {

    private static final Logger log = LoggerFactory.getLogger(PayrollProcessorjob.class);

    @Autowired
    private CsvPayrollService csvService;

    @Value("${app.csv.input-dir}")
    private String inputDir;

    @Value("${app.batch.retry.max}")
    private int maxRetries;

    @Value("${app.batch.retry.delay-ms}")
    private long retryDelayMs;

    @Scheduled(fixedDelayString = "${app.batch.fixed-rate-ms}")
    public void scanAndProcess() {
        try {
            Path dir = Paths.get(inputDir);
            if (!Files.exists(dir)) {
                log.warn("Input dir does not exist: {}", dir.toAbsolutePath());
                return;
            }

            // pick oldest file
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.csv")) {
                Path oldest = null;
                for (Path p : stream) {
                    if (oldest == null || Files.getLastModifiedTime(p).toMillis() < Files.getLastModifiedTime(oldest).toMillis()) {
                        oldest = p;
                    }
                }

                if (oldest == null) {
                    log.debug("No CSV files found in input dir");
                    return;
                }

                log.info("Found file to process: {}", oldest.getFileName());

                int attempt = 0;
                PayrollbatchLog result = null;
                while (attempt <= maxRetries) {
                    attempt++;
                    try {
                        result = csvService.processFile(oldest);
                        if (!"FAILED".equals(result.getStatus())) {
                            // processed successfully or partial success
                            result.setRetryCount(attempt - 1);
                            break;
                        } else {
                            log.warn("Processing returned FAILED status, attempt={}", attempt);
                        }
                    } catch (Exception e) {
                        log.error("Exception while processing file, attempt={}", attempt, e);
                    }
                    if (attempt <= maxRetries) {
                        log.info("Retrying in {} ms", retryDelayMs);
                        TimeUnit.MILLISECONDS.sleep(retryDelayMs);
                    }
                }
                if (result != null && "FAILED".equals(result.getStatus())) {
                    log.error("File processing failed after {} attempts: {}", attempt - 1, oldest.getFileName());
                }
            }
        } catch (Exception e) {
            log.error("Fatal error in scanAndProcess", e);
        }
    }

}
