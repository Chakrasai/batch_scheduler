package com.chakram.batch_schedule.service;

import com.chakram.batch_schedule.model.PayrollRecord;
import com.chakram.batch_schedule.model.PayrollbatchLog;
import com.chakram.batch_schedule.repository.PayrollRecordRepository;
import com.chakram.batch_schedule.repository.payrollbatchLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Service
public class CsvPayrollService {

    private static final Logger log = LoggerFactory.getLogger(CsvPayrollService.class);

    @Autowired
    private PayrollRecordRepository recordRepository;
    
    @Autowired
    private payrollbatchLogRepository logRepository;

    @Value("${app.csv.input-dir}")
    private String inputDir;

    @Transactional
    public PayrollbatchLog processFile(Path filePath) {
        String fileName = filePath.getFileName().toString();
        PayrollbatchLog batchLog = PayrollbatchLog.builder()
                .runTime(LocalDateTime.now())
                .fileName(fileName)
                .retryCount(0)
                .status("FAILED")
                .build();

        int total = 0, success = 0, failed = 0;
        StringBuilder errors = new StringBuilder();

        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            String header = br.readLine();
            if (header == null) throw new RuntimeException("Empty file");
            String[] cols = header.split(",");
            // Expecting employeeID,name,baseSalary,bonus,month
            if (cols.length < 5) throw new RuntimeException("Invalid header: " + header);

            String line;
            while ((line = br.readLine()) != null) {
                total++;
                try {
                    String[] parts = line.split(",");
                    String empId = parts[0].trim();
                    String name = parts[1].trim();
                    Double baseSalary = Double.parseDouble(parts[2].trim());
                    Double bonus = Double.parseDouble(parts[3].trim());
                    String month = parts[4].trim();

                    // Basic validation
                    if (empId.isEmpty() || name.isEmpty()) throw new RuntimeException("Missing employeeID or name");

                    PayrollRecord rec = PayrollRecord.builder()
                            .employeeID(empId)
                            .name(name)
                            .baseSalary(baseSalary)
                            .bonus(bonus)
                            .netSalary(baseSalary + bonus)
                            .month(month)
                            .uploadtime(LocalDateTime.now())
                            .build();

                    recordRepository.save(rec);
                    success++;
                } catch (Exception e) {
                    failed++;
                    String err = String.format("Row %d failed: %s", total, e.getMessage());
                    errors.append(err).append("; ");
                    log.error(err, e);
                }
            }

            // if at least one success, mark success (business decision)
            batchLog.setTotalRecords(total);
            batchLog.setSuccessCount(success);
            batchLog.setFailedCount(failed);
            batchLog.setErrorMessage(errors.toString());
            batchLog.setStatus(failed == 0 ? "SUCCESS" : "PARTIAL_SUCCESS");

            log.info("Processed file={}, total={}, success={}, failed={}", fileName, total, success, failed);

            // Move processed file to processed/ or error/ based on failed count
            Path processedDir = Paths.get(inputDir, failed == 0 ? "processed" : "error");
            Files.createDirectories(processedDir);
            Files.move(filePath, processedDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
            log.error("Fatal error processing file {}", fileName, e);
            batchLog.setTotalRecords(total);
            batchLog.setSuccessCount(success);
            batchLog.setFailedCount(failed + 1);
            batchLog.setErrorMessage(e.getMessage());
            batchLog.setStatus("FAILED");

            try {
                Path errorDir = Paths.get(inputDir, "error");
                Files.createDirectories(errorDir);
                Files.move(filePath, errorDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception ex) {
                log.error("Failed to move file to error dir", ex);
            }
        } finally {
            batchLog.setRunTime(LocalDateTime.now());
            logRepository.save(batchLog);
        }

        return batchLog;
    }

}
