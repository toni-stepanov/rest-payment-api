package com.services.transfer.service;

import com.services.transfer.domain.dto.Transfer;
import org.springframework.http.ResponseEntity;

public interface TransferService {
    ResponseEntity<?> transfer(Transfer transfer);
}
