package com.github.lewicki1990.phishingprotector.smsprocessing.lastfetchedsmsid;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@AllArgsConstructor
@Service
public class LastFetchedSmsIdService {

    private LastFetchedSmsIdRepository lastFetchedSmsIdRepository;

    private Optional<LastFetchedSmsId> findLastFetchedSmsId() {
        return lastFetchedSmsIdRepository.findById(1L);
    }

    // TODO: Create template method
    @Transactional(readOnly = true)
    public long getSmsIdWhichWereFetchedLast() {
        Optional<LastFetchedSmsId> lastFetchedSmsIdOptional = findLastFetchedSmsId();

        if (lastFetchedSmsIdOptional.isPresent()) {
            LastFetchedSmsId lastFetchedSmsId = lastFetchedSmsIdOptional.get();
            long smsId = lastFetchedSmsId.getLastFetchedId();
            log.debug("Last fetched smsId={}.", smsId);

            return smsId;
        } else {
            log.error("Last fetched smsId not found. This is really serious error.");
            throw new LastFetchedSmsIdNotFoundException();
        }
    }


    // TODO: Create template method
    @Transactional
    public void iterateLastFetchedSmsIdValue() {
        Optional<LastFetchedSmsId> lastFetchedSmsIdOptional = findLastFetchedSmsId();

        if (lastFetchedSmsIdOptional.isPresent()) {
            LastFetchedSmsId lastFetchedSmsId = lastFetchedSmsIdOptional.get();
            long smsId = lastFetchedSmsId.getLastFetchedId();
            log.debug("Last fetched smsId={} found for iteration purpose.", smsId);

            long newSmsId = ++smsId;
            LastFetchedSmsId newLastFetchedSmsId = new LastFetchedSmsId(newSmsId);
            lastFetchedSmsIdRepository.save(newLastFetchedSmsId);

            log.debug("Last fetched smsId updated. New smsId={} for iteration purpose.", newSmsId);
        } else {
            log.error("Last fetched smsId not found. This is really serious error.");
            throw new LastFetchedSmsIdNotFoundException();
        }
    }

    // TODO: Create template method
    @Transactional
    public void setLastFetchedSmsId(Long id) {
        Optional<LastFetchedSmsId> lastFetchedSmsIdOptional = findLastFetchedSmsId();

        if (lastFetchedSmsIdOptional.isPresent()) {
            LastFetchedSmsId lastFetchedSmsId = lastFetchedSmsIdOptional.get();
            long smsId = lastFetchedSmsId.getLastFetchedId();
            log.debug("Last fetched smsId={} found for check before update.", smsId);

            long newSmsId = id;
            LastFetchedSmsId newLastFetchedSmsId = new LastFetchedSmsId(newSmsId);
            lastFetchedSmsIdRepository.save(newLastFetchedSmsId);

            log.debug("Last fetched smsId updated. New smsId={} for value set purpose.", newSmsId);
        } else {
            log.error("Last fetched smsId not found. This is really serious error.");
            throw new LastFetchedSmsIdNotFoundException();
        }
    }
}
