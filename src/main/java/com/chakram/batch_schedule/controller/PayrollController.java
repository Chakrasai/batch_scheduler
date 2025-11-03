package com.chakram.batch_schedule.controller;

import com.chakram.batch_schedule.service.CsvPayrollService;
import com.chakram.batch_schedule.repository.payrollbatchLogRepository;
import com.chakram.batch_schedule.model.PayrollbatchLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    @Autowired
    private payrollbatchLogRepository logRepository;
    
    @Autowired
    private CsvPayrollService csvService;

    @GetMapping("/logs")
    public List<PayrollbatchLog> getLogs() {
        return logRepository.findAll();
    }

    @PostMapping("/run")
    public PayrollbatchLog runManual(@RequestParam String filename) {
        Path p = Paths.get("./input").resolve(filename);
        return csvService.processFile(p);
    }

}
