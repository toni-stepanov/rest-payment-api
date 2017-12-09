package com.services.transfer.web;

import com.services.transfer.controller.TransferController;
import com.services.transfer.domain.AccountEntity;
import com.services.transfer.service.AccountService;
import com.services.transfer.service.TransferService;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TransferRestControllerTest {
    @Mock
    private TransferService transferService;

    @Mock
    private AccountService accountService;

    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TransferController(accountService, transferService))
                .build();
    }

    @Test
    public void notFoundResponseIfNoSuchAccount() throws Exception {
        mockMvc.perform(get("/api/account/2").accept(MediaType.APPLICATION_JSON))
        				.andExpect(status().isNotFound());
    }

    @Test
    public void returnAccountIfPresent() throws Exception {
        when(accountService.findOneById(any())).thenReturn(new AccountEntity());
        mockMvc.perform(get("/api/account/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnConflictStatusIfAlreadyExist() throws Exception {
        when(accountService.findOneByName(any())).thenReturn(Optional.of(new AccountEntity()));
        mockMvc.perform(post("/api/create-account")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"name\" : \"toni\",\n" +
                        "  \"initialBalance\" : 2\n" +
                        "}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldReturnBadRequestStatusIfNoInputs() throws Exception {
        mockMvc.perform(post("/api/create-account")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnOkStatus() throws Exception {
        when(accountService.findOneByName(any())).thenReturn(Optional.empty());
        when(accountService.save(any())).thenReturn(new AccountEntity("toni", 5L));
        mockMvc.perform(post("/api/create-account")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"name\" : \"toni\",\n" +
                        "  \"initialBalance\" : 2\n" +
                        "}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void transferShouldBeSuccessful() throws Exception {
        ResponseEntity doneResponse = new ResponseEntity<>("done", HttpStatus.OK);
        when(transferService.transfer(any())).thenReturn(doneResponse);
        when(accountService.save(any())).thenReturn(new AccountEntity("toni", 5L));
        mockMvc.perform(put("/api/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"fromName\" : \"toni\",\n" +
                        "  \"toName\" : \"toni2\",\n" +
                        "  \"amount\" : 20\n" +
                        "}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void transferShouldReturnBadRequestForEmptyInputParams() throws Exception {
        ResponseEntity doneResponse = new ResponseEntity<>("done", HttpStatus.OK);
        when(transferService.transfer(any())).thenReturn(doneResponse);
        when(accountService.save(any())).thenReturn(new AccountEntity("toni", 5L));
        mockMvc.perform(put("/api/transfer")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}