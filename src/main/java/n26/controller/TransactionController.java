package n26.controller;

import lombok.extern.slf4j.Slf4j;
import n26.model.Transaction;
import n26.service.StatisticsService;
import n26.util.TimeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TransactionController {

    private final TimeUtils timeUtils;
    private final StatisticsService statisticsService;

    public TransactionController(TimeUtils timeUtils, StatisticsService statisticsService) {
        this.timeUtils = timeUtils;
        this.statisticsService = statisticsService;
    }

    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity collect(@RequestBody Transaction transaction) {
        log.info("Request received is {} and {}", transaction.getAmount(), transaction.getTimestamp());
        log.info("Received transaction with amount: {} and timestamp: {} ",
                transaction.getAmount(), transaction.getTimestamp());
        if (transaction.isTimeStampFromLastSixtySeconds(timeUtils.nowInMilliSeconds())) {
            statisticsService.collect(transaction);
            return new ResponseEntity(HttpStatus.CREATED);
        }
        log.info("Discarding older transaction");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
