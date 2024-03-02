package com.fansa.admin.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class PaginationResponse {
    //pagination
    private long currentPage;
    private int totalPages;
    private long startCount;
    private long endCount;
    private long totalItems;
    //param
    private String sortField;
    private String sortDir;
    private String keyword;

    private List<UserDTO> results;
}
