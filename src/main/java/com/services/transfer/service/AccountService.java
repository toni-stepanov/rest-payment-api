package com.services.transfer.service;

import com.services.transfer.domain.AccountEntity;
import com.services.transfer.domain.dto.Account;

import java.util.Optional;

public interface AccountService {
    AccountEntity save(Account account);
    AccountEntity findOneById(Long id);
    Optional<AccountEntity> findOneByName(String name);
}
