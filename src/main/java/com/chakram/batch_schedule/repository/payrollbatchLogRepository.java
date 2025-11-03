package com.chakram.batch_schedule.repository;

import com.chakram.batch_schedule.model.PayrollbatchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface payrollbatchLogRepository extends JpaRepository<PayrollbatchLog, Long> {


}
