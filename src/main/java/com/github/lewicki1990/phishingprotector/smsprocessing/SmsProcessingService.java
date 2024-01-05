package com.github.lewicki1990.phishingprotector.smsprocessing;

import com.github.lewicki1990.phishingprotector.smsprocessing.lastfetchedsmsid.LastFetchedSmsIdService;
import com.github.lewicki1990.phishingprotector.smsprocessing.strategy.NullSmsDTO;
import com.github.lewicki1990.phishingprotector.smsprocessing.strategy.SmsDTOFinishStrategy;
import com.github.lewicki1990.phishingprotector.smsprocessing.strategy.SmsDTOProcessingStrategy;
import com.github.lewicki1990.phishingprotector.smssource.Sms;
import com.github.lewicki1990.phishingprotector.smssource.SmsSourceService;
import com.github.lewicki1990.phishingprotector.smssource.mapping.SmsDTO;
import com.github.lewicki1990.phishingprotector.smssource.mapping.SmsMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class SmsProcessingService {

    private final SmsSourceService smsSourceService;

    private final LastFetchedSmsIdService lastFetchedSmsIdService;

    private final SmsMapper smsMapper;

    private final SmsDTOProcessingStrategy smsDTOAntiPhishingSubscriptionProcessingStrategy;
    
    private final SmsDTOProcessingStrategy activatedSmsDTOAntiPhishingSubscriptionProcessingStrategy;
    
    private final SmsDTOFinishStrategy smsDTOPhishingVerificationProcessingStrategy;

    private final int smsFetchingBatchSize;


    @Autowired
    public SmsProcessingService(SmsSourceService smsSourceService,
                                LastFetchedSmsIdService lastFetchedSmsIdService,
                                SmsMapper smsMapper,
                                @Qualifier("smsDTOAntiPhishingSubscriptionProcessingManagementStrategy") SmsDTOProcessingStrategy smsDTOAntiPhishingSubscriptionProcessingManagementStrategy,
                                @Qualifier("activatedSmsDTOAntiPhishingSubscriptionProcessingStrategy") SmsDTOProcessingStrategy activatedSmsDTOAntiPhishingSubscriptionProcessingStrategy,
                                SmsDTOFinishStrategy smsDTOPhishingVerificationProcessingStrategy,
                                @Value("${data-fetching.batch-size}") int smsFetchingBatchSize) {
        this.smsSourceService = smsSourceService;
        this.lastFetchedSmsIdService = lastFetchedSmsIdService;
        this.smsMapper = smsMapper;
        this.smsDTOAntiPhishingSubscriptionProcessingStrategy = smsDTOAntiPhishingSubscriptionProcessingManagementStrategy;
        this.activatedSmsDTOAntiPhishingSubscriptionProcessingStrategy = activatedSmsDTOAntiPhishingSubscriptionProcessingStrategy;
        this.smsDTOPhishingVerificationProcessingStrategy = smsDTOPhishingVerificationProcessingStrategy;
        this.smsFetchingBatchSize = smsFetchingBatchSize;
        
    }

    @Scheduled(fixedDelayString = "${data-fetching.retry-time-in-msecs}")
    public void process() {
        long lastProcessedSmsId = lastFetchedSmsIdService.getSmsIdWhichWereFetchedLast();
        long firstTriedBeFetchedByBatchSmsId = lastProcessedSmsId + 1;

        boolean newSmsRecordsExist = areNotProcessedRecordsExist(lastProcessedSmsId);
        if (!newSmsRecordsExist) {
            log.warn("There are no new sms messages in db.");
            return;
        }

        log.debug("Process of fetching new smses starts.");
        List<Sms> batchOfSmses = fetchSmsRecords(lastProcessedSmsId, smsFetchingBatchSize);
        long lastSmsIdInFetchedBatch = getLastSmsId(batchOfSmses);
        lastFetchedSmsIdService.setLastFetchedSmsId(lastSmsIdInFetchedBatch);

        log.debug("Transforming smses to smsDTOs process for batch starts.");
        List<SmsDTO> batchOfSmsDTOs = smsMapper.transformSmsListToSmsDTOList(batchOfSmses);
        if (checkIfThereIsNeedOfNextProcessStart(batchOfSmsDTOs, firstTriedBeFetchedByBatchSmsId, lastSmsIdInFetchedBatch)) return;

        log.debug("Anti phishing subscription management process for batch starts.");
        batchOfSmsDTOs = processBatchOfSmsDTOsInContextOfAntiPhishingSubscriptionManagement(batchOfSmsDTOs, smsDTOAntiPhishingSubscriptionProcessingStrategy);
        if (checkIfThereIsNeedOfNextProcessStart(batchOfSmsDTOs, firstTriedBeFetchedByBatchSmsId, lastSmsIdInFetchedBatch)) return;

        log.debug("A process to stop processing of smses sent to msisdns without active ");
        batchOfSmsDTOs = processBatchOfSmsDTOsInContextOfActivatedAntiPhishingSubscription(batchOfSmsDTOs, activatedSmsDTOAntiPhishingSubscriptionProcessingStrategy);
        if (checkIfThereIsNeedOfNextProcessStart(batchOfSmsDTOs, firstTriedBeFetchedByBatchSmsId, lastSmsIdInFetchedBatch)) return;

        log.debug("Process of finding phishing for batch starts.");
        processBatchOfSmsDTOInContextOfContainingPhishing(batchOfSmsDTOs);

        log.debug("All smses has been processed. Batch included records with smsId between: {} and {}", firstTriedBeFetchedByBatchSmsId, lastSmsIdInFetchedBatch);
    }

    private boolean areNotProcessedRecordsExist(long lastProcessedSmsId) {
        return smsSourceService.areExistAnySmsWithIdGreaterThan(lastProcessedSmsId);
    }

    private List<Sms> fetchSmsRecords(long lastProcessedSmsId, int batchSize) {
        return smsSourceService.getSortedSmsList(lastProcessedSmsId, batchSize);
    }

    private long getLastSmsId(List<Sms> batchOfSmses) {
        if (!batchOfSmses.isEmpty()) {
            int batchSize = batchOfSmses.size();
            Sms lastSms = batchOfSmses.get(--batchSize);
            return lastSms.getId();
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static boolean checkIfThereIsNeedOfNextProcessStart(List<SmsDTO> batchOfSmsDTOs, long firstTriedBeFetchedByBatchSmsId, long lastSmsIdInFetchedBatch) {
        if (batchOfSmsDTOs.isEmpty()) {
            log.warn("No smses to process in batch after last processing. Batch included records with smsId between: {} and {}", firstTriedBeFetchedByBatchSmsId, lastSmsIdInFetchedBatch);
            return true;
        }
        return false;
    }

    private List<SmsDTO> processBatchOfSmsDTOsInContextOfAntiPhishingSubscriptionManagement(List<SmsDTO> batchOfSmsDTOs, SmsDTOProcessingStrategy smsDTOAntiPhishingSubscriptionProcessingStrategy) {
        return processSmsDTOsInContextOfStrategy(batchOfSmsDTOs, smsDTOAntiPhishingSubscriptionProcessingStrategy);
    }

    private List<SmsDTO> processBatchOfSmsDTOsInContextOfActivatedAntiPhishingSubscription(List<SmsDTO> batchOfSmsDTOs, SmsDTOProcessingStrategy smsDTOAntiPhishingSubscriptionProcessingStrategy) {
        return processSmsDTOsInContextOfStrategy(batchOfSmsDTOs, smsDTOAntiPhishingSubscriptionProcessingStrategy);
    }

    private static List<SmsDTO> processSmsDTOsInContextOfStrategy(List<SmsDTO> batchOfSmsDTOs, SmsDTOProcessingStrategy smsDTOProcessingStrategy) {
        return batchOfSmsDTOs.stream()
                             .map(smsDTOProcessingStrategy::process)
                             .filter((smsDTO) -> !(smsDTO instanceof NullSmsDTO))
                             .collect(Collectors.toList());
    }

    private void processBatchOfSmsDTOInContextOfContainingPhishing(List<SmsDTO> batchOfSmsDTOs) {
        batchOfSmsDTOs.forEach(smsDTOPhishingVerificationProcessingStrategy::process);
    }
}