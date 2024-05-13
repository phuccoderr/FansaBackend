package com.fansa.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse {
    private long current_page;
    private long total_pages;
    private long total_items;
    private long start_count;
    private long end_count;
    private List<?> entity;
}
