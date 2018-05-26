package n26.util;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TimeUtils {
    public static final long MILLISECONDS_PER_SECOND = 1000;

    public long nowInMilliSeconds() {
        return Instant.now().toEpochMilli();
    }

    public long convertToSeconds(long timeInMilliSeconds) {
        return timeInMilliSeconds / MILLISECONDS_PER_SECOND;
    }

    public long nowInSeconds() {
        return convertToSeconds(nowInMilliSeconds());
    }

}


