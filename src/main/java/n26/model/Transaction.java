package n26.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import n26.util.TimeUtils;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Transaction {
    private static final int MAX_TIME_DIFFERENCE_ALLOWED = 60;

    Double amount;
    Long timestamp;

    public boolean isTimeStampFromLastSixtySeconds(long nowInMilliSeconds) {
        long diffInMillis = (nowInMilliSeconds - timestamp) / TimeUtils.MILLISECONDS_PER_SECOND;
        return diffInMillis >= 0 && diffInMillis <= MAX_TIME_DIFFERENCE_ALLOWED;
    }
}
