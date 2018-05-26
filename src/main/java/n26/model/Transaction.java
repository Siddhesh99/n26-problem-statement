package n26.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import n26.helper.TimeHelpers;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Transaction {
    private static final int VALID_TIME_RANGE_IN_SECONDS = 60;

    Double amount;
    Long timestamp;

    /**
     * @param currentTime
     * @return True if timestamp not older than 60 seconds else return False
     */
    public boolean isTimeStampValid(long currentTime) {
        long diff = (currentTime - timestamp) / TimeHelpers.MILLISECONDS_IN_A_SECOND;
        return diff >= 0 && diff <= VALID_TIME_RANGE_IN_SECONDS;
    }
}
