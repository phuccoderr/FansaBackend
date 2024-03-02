package com.fansa.common.entity;

import com.fansa.common.IdBasedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends IdBasedEntity {
    @Column(length = 128,nullable = false)
    private String name;

}
