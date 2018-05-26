package n26.controller;

import lombok.extern.slf4j.Slf4j;
import n26.helper.TimeHelpers;
import n26.model.Transaction;
import n26.service.StatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TransactionController {

    private final TimeHelpers timeHelpers;
    private final StatisticsService statisticsService;

    public TransactionController(TimeHelpers timeHelpers, StatisticsService statisticsService) {
        this.timeHelpers = timeHelpers;
        this.statisticsService = statisticsService;
    }

    /**
     * @param transaction
     * @return StatusCode
     * If transaction is not older than 60 seconds, returns 201(CREATED) status code.
     * If transaction is older than 60 seconds, returns 204(NO_CONTENT) status code.
     */

    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity collect(@RequestBody Transaction transaction) {
        log.info("Request received is {} and {}", transaction.getAmount(), transaction.getTimestamp());
        if (transaction.isTimeStampValid(timeHelpers.currentTime())) {
            statisticsService.collect(transaction);
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
