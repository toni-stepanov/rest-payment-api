package com.services.transfer.dao;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.services.transfer.domain.AccountEntity;
import com.services.transfer.repository.AccountRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class AccountDaoTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> jdbcTemplate.getDataSource().getConnection());

    @Autowired
    protected AccountRepository accountRepository;

    @Test
    @DataSet("empty.xml")
    @ExpectedDataSet("expected-account.xml")
    @Commit
    public void accountMayBeStored() {
        accountRepository.save(new AccountEntity("mike", (long) 11));
    }

    @Test
    @DataSet("stored-account.xml")
    @Commit
    public void canFindAccountByName() {
        Optional<AccountEntity> jhoeAccount = accountRepository.findByName("jhoe");
        assertTrue(jhoeAccount.isPresent());
    }

}
