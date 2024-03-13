package com.choi.api.biz.user.model;

import javax.persistence.*;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity(name="TB_USER_BASIC")
public class User {

    @Id
    @Column(name = "ID", nullable = false)
    private String id;
    @Column(nullable = false, length = 30)
    private String username;
    @Column(nullable = false, length = 100)
    private String password;
    @Column(nullable = false, length = 30, unique = true)
    private String email;
    @Column(nullable = false, length = 10)
    private String status;
    @Column(nullable = false, length = 10)
    private String authority;
    @Column(name = "SYS_CREATION_DATE")
    private LocalDateTime sysCreationDate;
    @Column(name = "SYS_UPDATE_DATE")
    private LocalDateTime sysUpdateDate;

    @PrePersist
    protected void onCreate() {
        sysCreationDate = LocalDateTime.now();
        sysUpdateDate = LocalDateTime.now();
    }

}
