package n26.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TransactionTest {

    @Test
    public void shouldReturnTrueWhenTransactionIsFromLastSixtySeconds() {
        Transaction transaction = new Transaction(12.3, 90000L);

        boolean isValid = transaction.isTimeStampFromLastSixtySeconds(95000L);

        assertTrue(isValid);
    }

    @Test
    public void shouldReturnFalseWhenTransactionIsOlderThanSixtySeconds() {
        Transaction transaction = new Transaction(12.3, 10000L);

        boolean isValid = transaction.isTimeStampFromLastSixtySeconds(80000L);

        assertFalse(isValid);

    }

    @Test
    public void shouldReturnFalseWhenTransactionTimestampIsInFuture() {
        Transaction transaction = new Transaction(12.3, 90000L);

        boolean isValid = transaction.isTimeStampFromLastSixtySeconds(80000L);

        assertFalse(isValid);
    }
}