package com.chakram.batch_schedule.repository;


import com.chakram.batch_schedule.model.PayrollRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollRecordRepository extends JpaRepository<PayrollRecord, Long> {

}
