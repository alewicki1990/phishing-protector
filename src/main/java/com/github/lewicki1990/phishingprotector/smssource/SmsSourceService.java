package com.github.lewicki1990.phishingprotector.smssource;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@AllArgsConstructor
@Service
public class SmsSourceService {

    private SmsSourceRepository smsSourceRepository;

    @Transactional(readOnly = true)
    public List<Sms> getSortedSmsList(long lastProcessedSmsId, int batchSize) {
        Pageable pageable = PageRequest.of(0, batchSize, Sort.by("id"));
        Page<Sms> page = smsSourceRepository.findBatchOfRecords(lastProcessedSmsId, pageable);

        log.info("Sms fetching: {} has/have been fetched. First fetched sms id={}", getNumberOfRecordsInSmsPage(page), getFirstFetchedSmsIdBasingOnLastProcessedSmsId(lastProcessedSmsId));
        return page.getContent();
    }

    private long getFirstFetchedSmsIdBasingOnLastProcessedSmsId(long lastProcessedSmsId) {
        return ++lastProcessedSmsId;
    }

    private int getNumberOfRecordsInSmsPage(Page<Sms> page) {
        return page.getNumberOfElements();
    }

    @Transactional
    public void deleteSmsById(long smsId) {
        smsSourceRepository.deleteById(smsId);
        log.info("smsId={} Sms has been deleted", smsId);
    }

    @Transactional(readOnly = true)
    public boolean areExistAnySmsWithIdGreaterThan(long smsId){
        return smsSourceRepository.areExistAnyRecordWitIdGreaterThan(smsId);
    }
}
