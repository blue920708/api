package com.choi.api.biz.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserToken {
    private String accessToken;
    private String refreshToken;
    private String username;
}
