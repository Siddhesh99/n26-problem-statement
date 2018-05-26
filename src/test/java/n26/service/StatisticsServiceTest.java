package n26.service;

import n26.model.Statistics;
import n26.model.Transaction;
import n26.util.TimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
}