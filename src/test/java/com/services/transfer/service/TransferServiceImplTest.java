package com.services.transfer.service;

import com.services.transfer.domain.AccountEntity;
import com.services.transfer.domain.ResponseError;
import com.services.transfer.domain.dto.Transfer;
import com.services.transfer.exceptions.TooMuchMoneyException;
import com.services.transfer.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransferServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    private TransferService transferService;

    @Before
    public void init() {
        transferService = new TransferServiceImpl(accountRepository);
    }

    @Test
    public void shouldExecuteTransferForCorrectData() {
        AccountEntity accountFrom = new AccountEntity("a1", 100L);
        AccountEntity accountTo = new AccountEntity("a2", 100L);
        when(accountRepository.findByName("a1")).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findByName("a2")).thenReturn(Optional.of(accountTo));
        when(((JpaRepository) accountRepository).save(any())).thenReturn(null);
        Transfer transfer = new Transfer("a1", "a2",  11L);
        ResponseEntity done = new ResponseEntity<>("done", HttpStatus.OK);
        assertThat(transferService.transfer(transfer), samePropertyValuesAs(done));
    }

    @Test(expected = TooMuchMoneyException.class)
    public void shouldThrowExceptionForOverflow() {
        AccountEntity accountFrom = new AccountEntity("a1", 200L);
        AccountEntity accountTo = new AccountEntity("a2", 9223372036854775807L);
        when(accountRepository.findByName("a1")).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findByName("a2")).thenReturn(Optional.of(accountTo));
        when(((JpaRepository) accountRepository).save(any())).thenReturn(null);
        Transfer transfer = new Transfer("a1", "a2", 11L);
        transferService.transfer(transfer);
    }

    @Test
    public void shouldReturnNotEnoughMoneyError() {
        AccountEntity accountFrom = new AccountEntity("a1", 10L);
        AccountEntity accountTo = new AccountEntity("a2", 100L);
        when(accountRepository.findByName("a1")).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findByName("a2")).thenReturn(Optional.of(accountTo));
        when(((JpaRepository) accountRepository).save(any())).thenReturn(null);
        Transfer transfer = new Transfer("a1", "a2", 20L);
        ResponseEntity done = new ResponseEntity<>(new ResponseError("a1 hasn't enough money for this transfer"),
                HttpStatus.BAD_REQUEST);
        assertThat(transferService.transfer(transfer), samePropertyValuesAs(done));
    }


}

