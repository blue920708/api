package com.choi.api.biz.user.model;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

public class UserDTO {

    @Getter
    @Setter
    public static class UserJoinReq {

        private String username;
        private String password;
        private String name;
        private String email;
        private String authority;

        public User toEntity(){
            return User
                    .builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .authority("guest")
                    .status("Y")
                    .build();
        }
    }

}
