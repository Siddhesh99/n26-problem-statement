package n26.controller;

import lombok.extern.slf4j.Slf4j;
import n26.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TransactionController {

    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity collect(@RequestBody Transaction transaction) {
        log.info("Request received is {} and {}", transaction.getAmount(), transaction.getTimestamp());
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
