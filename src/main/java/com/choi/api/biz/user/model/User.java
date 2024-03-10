package com.choi.api.biz.user.model;

import javax.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity(name="TB_USER_BASIC")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

}
