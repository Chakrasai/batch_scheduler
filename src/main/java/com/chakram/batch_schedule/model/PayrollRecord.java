package com.chakram.batch_schedule.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payroll_record")
public class PayrollRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String employeeID;
    private String name;
    private double baseSalary;
    private double bonus;
    private double netSalary;
    private String month;
    private LocalDateTime uploadtime;

    // Constructors
    public PayrollRecord() {}

    public PayrollRecord(String employeeID, String name, double baseSalary, double bonus, 
                        double netSalary, String month, LocalDateTime uploadtime) {
        this.employeeID = employeeID;
        this.name = name;
        this.baseSalary = baseSalary;
        this.bonus = bonus;
        this.netSalary = netSalary;
        this.month = month;
        this.uploadtime = uploadtime;
    }

    // Builder pattern
    public static PayrollRecordBuilder builder() {
        return new PayrollRecordBuilder();
    }

    public static class PayrollRecordBuilder {
        private String employeeID;
        private String name;
        private double baseSalary;
        private double bonus;
        private double netSalary;
        private String month;
        private LocalDateTime uploadtime;

        public PayrollRecordBuilder employeeID(String employeeID) {
            this.employeeID = employeeID;
            return this;
        }

        public PayrollRecordBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PayrollRecordBuilder baseSalary(double baseSalary) {
            this.baseSalary = baseSalary;
            return this;
        }

        public PayrollRecordBuilder bonus(double bonus) {
            this.bonus = bonus;
            return this;
        }

        public PayrollRecordBuilder netSalary(double netSalary) {
            this.netSalary = netSalary;
            return this;
        }

        public PayrollRecordBuilder month(String month) {
            this.month = month;
            return this;
        }

        public PayrollRecordBuilder uploadtime(LocalDateTime uploadtime) {
            this.uploadtime = uploadtime;
            return this;
        }

        public PayrollRecord build() {
            return new PayrollRecord(employeeID, name, baseSalary, bonus, netSalary, month, uploadtime);
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmployeeID() { return employeeID; }
    public void setEmployeeID(String employeeID) { this.employeeID = employeeID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(double baseSalary) { this.baseSalary = baseSalary; }

    public double getBonus() { return bonus; }
    public void setBonus(double bonus) { this.bonus = bonus; }

    public double getNetSalary() { return netSalary; }
    public void setNetSalary(double netSalary) { this.netSalary = netSalary; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public LocalDateTime getUploadtime() { return uploadtime; }
    public void setUploadtime(LocalDateTime uploadtime) { this.uploadtime = uploadtime; }
}
