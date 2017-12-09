package com.services.transfer.service;

import com.services.transfer.domain.AccountEntity;
import com.services.transfer.domain.dto.Account;
import com.services.transfer.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    @Override
    public AccountEntity save(Account account) {
        AccountEntity accountEntity = new AccountEntity(account.getName(), account.getInitialBalance());
        return accountRepository.save(accountEntity);
    }

    @Override
    public AccountEntity findOneById(Long id) {
        return accountRepository.findOne(id);
    }

    @Override
    public Optional<AccountEntity> findOneByName(String name) {
        return accountRepository.findByName(name);
    }
}
