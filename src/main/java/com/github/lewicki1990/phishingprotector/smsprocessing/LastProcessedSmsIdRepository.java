package com.github.lewicki1990.phishingprotector.smsprocessing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LastProcessedSmsIdRepository extends JpaRepository<LastProcessedSmsId, Long> {
}
