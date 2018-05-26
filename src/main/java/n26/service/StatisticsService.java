package n26.service;

import lombok.extern.slf4j.Slf4j;
import n26.helper.TimeHelpers;
import n26.model.Statistics;
import n26.model.Transaction;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class StatisticsService {
    private ConcurrentHashMap<Long, DoubleSummaryStatistics> perSecondBuckets;
    private DoubleSummaryStatistics statisticsResult;
    private final TimeHelpers timeHelpers;

    StatisticsService(TimeHelpers timeHelpers) {
        this.timeHelpers = timeHelpers;
        statisticsResult = new DoubleSummaryStatistics();
        perSecondBuckets = new ConcurrentHashMap<>();
    }

    /**
     * @return statistics
     * If empty return statistics with default values
     * otherwise return already calculated statistics in O(1)
     */
    public Statistics getStatistics() {
        if (perSecondBuckets.size() == 0)
            return new Statistics(0.0, 0.0, 0.0, 0.0, 0L);
        return new Statistics(statisticsResult);
    }

    /**
     * @param transaction
     * @return statistics
     * Add transaction in perSecondBuckets hash and update the final statistics result
     * This could have been done async but in this way can use the result of this method
     * to convey information to user in case of failures
     */
    public Statistics collect(final Transaction transaction) {
        long transactionTimeStampInSeconds = timeHelpers.convertToSeconds(transaction.getTimestamp());
        if (perSecondBuckets.containsKey(transactionTimeStampInSeconds)) {
            log.info("Updating existing statistics for the second : {}", transactionTimeStampInSeconds);
            DoubleSummaryStatistics existingStatisticsForSecond = perSecondBuckets.get(transactionTimeStampInSeconds);
            existingStatisticsForSecond.accept(transaction.getAmount());
        } else {
            log.info("Creating new statistics for the second : {}", transactionTimeStampInSeconds);
            DoubleSummaryStatistics newStatisticsForSecond = new DoubleSummaryStatistics();
            newStatisticsForSecond.accept(transaction.getAmount());
            perSecondBuckets.put(transactionTimeStampInSeconds, newStatisticsForSecond);
        }

        log.info("Updating statistics result. Current value : {}", new Statistics(statisticsResult));

        synchronized (this) {
            statisticsResult.accept(transaction.getAmount());
        }

        Statistics updatedStatistics = new Statistics(statisticsResult);
        log.info("Updated total statistics. New value : {}", updatedStatistics);
        return updatedStatistics;
    }

    /**
     * @return statistics result
     * Clear transaction which is older than 60 seconds
     */
    @Async
    @Scheduled(fixedDelay = TimeHelpers.MILLISECONDS_IN_A_SECOND, initialDelay = TimeHelpers.MILLISECONDS_IN_A_SECOND)
    public DoubleSummaryStatistics clearOlderStatisticsEverySecond() {

        long nowInSeconds = timeHelpers.currentTimeInSeconds();
        log.info("Running job to clear older statistics. Current time: {}", nowInSeconds);
        long oldestTimeInSeconds = nowInSeconds - 60;

        if (perSecondBuckets.containsKey(oldestTimeInSeconds)) {
            log.info("Older statistics found for time: {}", oldestTimeInSeconds);
            perSecondBuckets.remove(oldestTimeInSeconds);

            synchronized (this) {
                statisticsResult = calculateStatisticsResult();
            }

            log.info("Updated total statistics: {}", new Statistics(statisticsResult));
            return statisticsResult;
        }
        log.info("No older statistics found for clearing");
        return statisticsResult;
    }

    /**
     * @return DoubleSummaryStatistics final result
     * Recalculate the final result
     */
    private DoubleSummaryStatistics calculateStatisticsResult() {
        log.info("Recalculating total statistics Summary");
        DoubleSummaryStatistics result = new DoubleSummaryStatistics();
        for (Long second : perSecondBuckets.keySet()) {
            DoubleSummaryStatistics statistics = perSecondBuckets.get(second);
            result.combine(statistics);
        }
        return result;
    }
}
