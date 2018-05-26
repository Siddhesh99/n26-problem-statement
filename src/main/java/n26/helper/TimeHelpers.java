package n26.helper;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TimeHelpers {
    public static final long MILLISECONDS_IN_A_SECOND = 1000;

    public long currentTime() {
        return Instant.now().toEpochMilli();
    }

    public long convertToSeconds(long millis) {
        return millis / MILLISECONDS_IN_A_SECOND;
    }

    public long currentTimeInSeconds() {
        return convertToSeconds(currentTime());
    }

}


