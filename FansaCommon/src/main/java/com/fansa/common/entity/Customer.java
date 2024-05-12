package com.fansa.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String photo;

    @Column(nullable = false,length = 45)
    private String email;
    @Column(length = 64)
    private String password;

    @Column(name = "created_time",nullable = false)
    private LocalDate createdTime;

    private boolean enabled;

    @Column(name = "verification_code",length = 64)
    private String verificationCode;

    @Column(name = "reset_password_token",length = 30)
    private String resetPasswordToken;

}
