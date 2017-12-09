package com.services.transfer.service;

import com.services.transfer.domain.AccountEntity;
import com.services.transfer.domain.ResponseError;
import com.services.transfer.domain.dto.Transfer;
import com.services.transfer.exceptions.TooMuchMoneyException;
import com.services.transfer.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TransferServiceImpl implements TransferService {

    private AccountRepository accountRepository;

    @Value("${exceptions.tooMuchMoney}")
    private String tooMuchMoneyMessage;

    @Value("${exceptions.noFromAccount}")
    private String noFromAccount;

    @Value("${exceptions.noToAccount}")
    private String noToAccountMessage;

    public TransferServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<?> transfer(final Transfer transfer) {
        AccountEntity fromAccount = accountRepository.findByName(transfer.getFromName()).orElseThrow(()
                -> new TooMuchMoneyException(noFromAccount));
        AccountEntity toAccount = accountRepository.findByName(transfer.getToName()).orElseThrow(()
                -> new TooMuchMoneyException(noToAccountMessage));
        long fromBalance = fromAccount.getBalance() - transfer.getAmount();
        if (!updateFromBalance(fromAccount, fromBalance)) {
            return new ResponseEntity<>(new ResponseError(fromAccount.getName() + " hasn't enough money for " +
                    "this transfer"), HttpStatus.BAD_REQUEST);
        }
        toAccount.setBalance(getToBalance(transfer, toAccount));
        accountRepository.save(toAccount);
        return new ResponseEntity<>("done", HttpStatus.OK);
    }

    private boolean updateFromBalance(AccountEntity fromAccount, long fromBalance) {
        if(fromBalance >= 0){
            fromAccount.setBalance(fromBalance);
            accountRepository.save(fromAccount);
            return true;
        } else {
            return false;
        }
    }

    private long getToBalance(Transfer transfer, AccountEntity toAccount) {
        try {
            return Math.addExact(toAccount.getBalance(), transfer.getAmount());
        }catch (ArithmeticException e){
            throw new TooMuchMoneyException(tooMuchMoneyMessage);
        }
    }
}
