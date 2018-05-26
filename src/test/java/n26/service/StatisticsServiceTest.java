package n26.service;

import n26.model.Statistics;
import n26.model.Transaction;
import n26.util.TimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.DoubleSummaryStatistics;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class StatisticsServiceTest {

    @Mock
    private TimeUtils mockTimeUtils;

    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    public void shouldCollectGivenTransactionAndReturnStatistics() {
        when(mockTimeUtils.nowInMilliSeconds()).thenReturn(110000L);

        Transaction transaction = new Transaction(13.3, 100000L);
        Statistics statistics = statisticsService.collect(transaction);

        assertThat(statistics.getCount(), is(1L));
        assertThat(statistics.getSum(), is(13.3));
        assertThat(statistics.getAvg(), is(13.3));
        assertThat(statistics.getMax(), is(13.3));
        assertThat(statistics.getMin(), is(13.3));
    }

    @Test
    public void shouldReturnUpdatedStatisticsForGivenTransactionWithExistingTimeStamp() {
        when(mockTimeUtils.nowInMilliSeconds()).thenReturn(110000L);

        Transaction transactionOne = new Transaction(50.0, 100000L);
        Transaction transactionTwo = new Transaction(100.0, 100000L);
        Transaction transactionThree = new Transaction(300.0, 105000L);

        statisticsService.collect(transactionOne);
        statisticsService.collect(transactionTwo);
        Statistics statisticSummaryThree = statisticsService.collect(transactionThree);

        assertThat(statisticSummaryThree.getCount(), is(3L));
        assertThat(statisticSummaryThree.getSum(), is(450.0));
        assertThat(statisticSummaryThree.getAvg(), is(150.0));
        assertThat(statisticSummaryThree.getMax(), is(300.0));
        assertThat(statisticSummaryThree.getMin(), is(50.0));
    }

    @Test
    public void shouldReturnUpdatedStatisticsSummaryIfOlderStatisticsFoundToClear() {
        when(mockTimeUtils.nowInMilliSeconds()).thenReturn(62000L);
        when(mockTimeUtils.nowInSeconds()).thenReturn(62L);
        when(mockTimeUtils.convertToSeconds(2000L)).thenReturn(2L);
        when(mockTimeUtils.convertToSeconds(61000L)).thenReturn(61L);
        when(mockTimeUtils.convertToSeconds(62000L)).thenReturn(62L);

        Transaction olderTransaction = new Transaction(500.0, 2000L);
        Transaction recentTransactionOne = new Transaction(200.0, 61000L);
        Transaction recentTransactionTwo = new Transaction(300.0, 62000L);

        statisticsService.collect(olderTransaction);
        statisticsService.collect(recentTransactionOne);
        Statistics originalStatistics = statisticsService.collect(recentTransactionTwo);

        DoubleSummaryStatistics updatedStatistics = statisticsService.clearOlderStatisticsEverySecond();

        assertThat(originalStatistics.getCount(), is(3L));
        assertThat(updatedStatistics.getCount(), is(2L));

        assertThat(originalStatistics.getMax(), is(500.0));
        assertThat(updatedStatistics.getMax(), is(300.0));
    }

    @Test
    public void shouldReturnDefaultStatisticsIfNothingIsThereInTheBuffer() {
        Statistics defaultSummary = statisticsService.getStatistics();

        assertThat(defaultSummary.getCount(), is(0L));
        assertThat(defaultSummary.getSum(), is(0.0));
        assertThat(defaultSummary.getAvg(), is(0.0));
        assertThat(defaultSummary.getMax(), is(0.0));
        assertThat(defaultSummary.getMin(), is(0.0));
    }

    @Test
    public void shouldReturnStatisticsAsPerContentsOfBuffer() {
        when(mockTimeUtils.nowInMilliSeconds()).thenReturn(100000L);

        Transaction recentTransactionOne = new Transaction(200.0, 110000L);
        Transaction recentTransactionTwo = new Transaction(400.0, 120000L);

        statisticsService.collect(recentTransactionOne);
        statisticsService.collect(recentTransactionTwo);

        Statistics statisticSummary = statisticsService.getStatistics();

        assertThat(statisticSummary.getCount(), is(2L));
        assertThat(statisticSummary.getSum(), is(600.0));
        assertThat(statisticSummary.getAvg(), is(300.0));
        assertThat(statisticSummary.getMax(), is(400.0));
        assertThat(statisticSummary.getMin(), is(200.0));
    }

}