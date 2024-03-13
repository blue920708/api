package com.choi.api.biz.user.model;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserDTO {

    @Getter
    public static class UserJoinReq {
        private String username;
        private String password;
        private String name;
        private String email;
        private String authority;

    }

    @Getter
    public static class FindPwdReq {
        private String username;
        private String email;
        private int seq;
        private String code;
        private String password;
    }

    @Getter
    public static class ChgPwdReq {
        private String prevPwd;
        private String newPwd;
    }

    @Builder
    @Data
    public static class GetUserRes {
        private String username;
        private String email;
        private String insdate;
    }

}
