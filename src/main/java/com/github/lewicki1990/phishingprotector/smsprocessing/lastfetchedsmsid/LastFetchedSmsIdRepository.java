package com.github.lewicki1990.phishingprotector.smsprocessing.lastfetchedsmsid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LastFetchedSmsIdRepository extends JpaRepository<LastFetchedSmsId, Long> {
}
