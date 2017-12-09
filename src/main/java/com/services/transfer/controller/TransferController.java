package com.services.transfer.controller;

import com.services.transfer.domain.AccountEntity;
import com.services.transfer.domain.ResponseError;
import com.services.transfer.domain.dto.Account;
import com.services.transfer.domain.dto.Transfer;
import com.services.transfer.service.AccountService;
import com.services.transfer.service.TransferService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
public class TransferController {

    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);

    private final AccountService accountService;

    private final TransferService transferService;

    @PostMapping("/api/create-account")
    public ResponseEntity<?> createAccount(@RequestBody final Account account, final UriComponentsBuilder ucBuilder) {
        if (account == null || account.getName().isEmpty() || account.getInitialBalance() == null
                || account.getInitialBalance() < 0) {
            return new ResponseEntity<>(new ResponseError("name or initialBalance is empty"),
                    HttpStatus.BAD_REQUEST);
        }
        logger.info("Create account request with params: name: {}, initialBalance: {}",
                account.getName(), account.getInitialBalance());
        if(accountService.findOneByName(account.getName()).isPresent()){
            return new ResponseEntity<>(new ResponseError("Account with such name already exist"),
                    HttpStatus.CONFLICT);
        }
        final AccountEntity createdAccount = accountService.save(account);
        final HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/account/{id}").buildAndExpand(createdAccount.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/api/transfer")
    public ResponseEntity<?> transfer(@RequestBody final Transfer transfer){
        if (transfer == null || transfer.getFromName() == null || transfer.getToName() == null
                || transfer.getAmount() <= 0) {
            return new ResponseEntity<>(new ResponseError("fromName, toName or amount is empty(or incorrect)"),
                    HttpStatus.BAD_REQUEST);
        }
        logger.info("Transfer {} euros: {} => {}",
                transfer.getAmount(), transfer.getFromName(), transfer.getToName());
        return transferService.transfer(transfer);
    }

    @GetMapping("/api/account/{id}")
    public ResponseEntity<AccountEntity> getAccount(@PathVariable final Long id) {
        logger.info("Get account with id: {}", id);
        final AccountEntity account = accountService.findOneById(id);
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

}


