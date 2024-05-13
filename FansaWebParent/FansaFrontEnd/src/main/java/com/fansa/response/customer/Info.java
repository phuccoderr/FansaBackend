package com.fansa.response.customer;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Info {
    private Long id;
    private String name;
    private String email;
    private String photo;
    
}
