package com.github.lewicki1990.phishingprotector.smsprocessing;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@AllArgsConstructor
@Service
public class LastProcessedSmsIdService {

    private LastProcessedSmsIdRepository lastProcessedSmsIdRepository;

    private Optional<LastProcessedSmsId> findLastProcessedSmsId() {
        return lastProcessedSmsIdRepository.findById(1L);
    }

    // TODO: Create template method
    @Transactional(readOnly = true)
    public long getSmsIdWhichWereProcessedLast() {
        Optional<LastProcessedSmsId> lastProcessedSmsIdOptional = findLastProcessedSmsId();

        if (lastProcessedSmsIdOptional.isPresent()) {
            LastProcessedSmsId lastProcessedSmsId = lastProcessedSmsIdOptional.get();
            long smsId = lastProcessedSmsId.getLastProcessedId();
            log.debug("Last processed smsId fetched. smsId={} for fetching purpose.", smsId);

            return smsId;
        } else {
            log.error("Last processed smsId not found. This is really serious error.");
            throw new LastProcessedSmsIdNotFound();
        }
    }


    // TODO: Create template method
    @Transactional
    public void iterateLastProcessedSmsIdValue() {
        Optional<LastProcessedSmsId> lastProcessedSmsIdOptional = findLastProcessedSmsId();

        if (lastProcessedSmsIdOptional.isPresent()) {
            LastProcessedSmsId lastProcessedSmsId = lastProcessedSmsIdOptional.get();
            long smsId = lastProcessedSmsId.getLastProcessedId();
            log.debug("Last processed smsId fetched. smsId={} for iteration purpose.", smsId);

            long newSmsId = ++smsId;
            LastProcessedSmsId newLastProcessedSmsId = new LastProcessedSmsId(newSmsId);
            lastProcessedSmsIdRepository.save(newLastProcessedSmsId);

            log.debug("Last processed smsId updated. New smsId={} for iteration purpose.", newSmsId);
        } else {
            log.error("Last processed smsId not found. This is really serious error.");
            throw new LastProcessedSmsIdNotFound();
        }
    }
}
