package com.chakram.batch_schedule.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payroll_batch_log")
public class PayrollbatchLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime runTime;
    private String fileName;
    private Integer totalRecords;
    private Integer successCount;
    private Integer failedCount;
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
    private String status;
    private Integer retryCount;

    // Constructors
    public PayrollbatchLog() {}

    public PayrollbatchLog(LocalDateTime runTime, String fileName, Integer totalRecords, 
                          Integer successCount, Integer failedCount, String errorMessage, 
                          String status, Integer retryCount) {
        this.runTime = runTime;
        this.fileName = fileName;
        this.totalRecords = totalRecords;
        this.successCount = successCount;
        this.failedCount = failedCount;
        this.errorMessage = errorMessage;
        this.status = status;
        this.retryCount = retryCount;
    }

    // Builder pattern
    public static PayrollbatchLogBuilder builder() {
        return new PayrollbatchLogBuilder();
    }

    public static class PayrollbatchLogBuilder {
        private LocalDateTime runTime;
        private String fileName;
        private Integer totalRecords;
        private Integer successCount;
        private Integer failedCount;
        private String errorMessage;
        private String status;
        private Integer retryCount;

        public PayrollbatchLogBuilder runTime(LocalDateTime runTime) {
            this.runTime = runTime;
            return this;
        }

        public PayrollbatchLogBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public PayrollbatchLogBuilder retryCount(Integer retryCount) {
            this.retryCount = retryCount;
            return this;
        }

        public PayrollbatchLogBuilder status(String status) {
            this.status = status;
            return this;
        }

        public PayrollbatchLog build() {
            return new PayrollbatchLog(runTime, fileName, totalRecords, successCount, 
                                      failedCount, errorMessage, status, retryCount);
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getRunTime() { return runTime; }
    public void setRunTime(LocalDateTime runTime) { this.runTime = runTime; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public Integer getTotalRecords() { return totalRecords; }
    public void setTotalRecords(Integer totalRecords) { this.totalRecords = totalRecords; }

    public Integer getSuccessCount() { return successCount; }
    public void setSuccessCount(Integer successCount) { this.successCount = successCount; }

    public Integer getFailedCount() { return failedCount; }
    public void setFailedCount(Integer failedCount) { this.failedCount = failedCount; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
}
