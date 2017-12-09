package com.services.transfer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class TooMuchMoneyException extends RuntimeException{

    public TooMuchMoneyException(String errorMsg) {
        super(errorMsg);
    }

}
