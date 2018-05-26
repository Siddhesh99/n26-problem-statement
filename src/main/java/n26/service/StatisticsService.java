package n26.service;

import lombok.extern.slf4j.Slf4j;
import n26.model.Statistics;
import n26.model.Transaction;
import n26.util.TimeUtils;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class StatisticsService {
    private ConcurrentHashMap<Long, DoubleSummaryStatistics> statisticsBuffer;
    private DoubleSummaryStatistics statisticsResult;
    private final TimeUtils timeUtils;

    StatisticsService(TimeUtils timeUtils) {
        this.timeUtils = timeUtils;
        statisticsResult = new DoubleSummaryStatistics();
        statisticsBuffer = new ConcurrentHashMap<>();
    }

    public Statistics collect(final Transaction transaction) {
        long transactionTimeStampInSeconds = timeUtils.convertToSeconds(transaction.getTimestamp());
        if (statisticsBuffer.containsKey(transactionTimeStampInSeconds)) {
            log.info("Updating existing statistics for the second : {}", transactionTimeStampInSeconds);
            DoubleSummaryStatistics existingStatisticsForSecond = statisticsBuffer.get(transactionTimeStampInSeconds);
            existingStatisticsForSecond.accept(transaction.getAmount());
        } else {
            log.info("Creating new statistics for the second : {}", transactionTimeStampInSeconds);
            DoubleSummaryStatistics newStatisticsForSecond = new DoubleSummaryStatistics();
            newStatisticsForSecond.accept(transaction.getAmount());
            statisticsBuffer.put(transactionTimeStampInSeconds, newStatisticsForSecond);
        }

        log.info("Updating statistics result. Current value : {}", new Statistics(statisticsResult));

        synchronized (this) {
            statisticsResult.accept(transaction.getAmount());
        }

        Statistics updatedStatistics = new Statistics(statisticsResult);
        log.info("Updated total statistics. New value : {}", updatedStatistics);
        return updatedStatistics;
    }
}
