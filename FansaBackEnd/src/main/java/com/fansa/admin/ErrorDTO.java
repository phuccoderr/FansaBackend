package com.fansa.admin;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
public class ErrorDTO {
    private Date timestamp;
    private int status;
    private String path;
    private List<String> error = new ArrayList<>();

    public void addError(String error) {
        this.error.add(error);
    }
}
