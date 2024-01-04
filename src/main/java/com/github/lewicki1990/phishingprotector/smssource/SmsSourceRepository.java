package com.github.lewicki1990.phishingprotector.smssource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsSourceRepository extends JpaRepository<Sms, Long> {
    @Query("SELECT s FROM Sms s WHERE s.id > :lastProcessedId ORDER BY s.id ASC")
    Page<Sms> findBatchOfRecords(@Param("lastProcessedId") long lastProcessedId, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(s) = 1 THEN true ELSE false END FROM Sms s WHERE s.id = :id")
    boolean existsById(long id);
}