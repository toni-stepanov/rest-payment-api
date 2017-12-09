package com.services.transfer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transfer {
    private String fromName;
    private String toName;
    private Long amount;
}
