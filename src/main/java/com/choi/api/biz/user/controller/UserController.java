package com.choi.api.biz.user.controller;

import com.choi.api.biz.mail.model.MailDTO;
import com.choi.api.core.model.ApiResponse;
import com.choi.api.biz.user.model.UserDTO;
import com.choi.api.biz.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/join")
    public ApiResponse save(@RequestBody UserDTO.UserJoinReq req){
        return userService.save(req);
    }

    @PostMapping("/verify-email")
    public ApiResponse verifyEmail(@RequestBody MailDTO.SendMailReq req) {
        return userService.verifyEmail(req);
    }

    @PostMapping("/verify-email-code")
    public ApiResponse verifyEmailCode(@RequestBody MailDTO.VerifyCodeReq req) { return userService.verifyEmailCode(req);}

    @GetMapping("/isLogin")
    public ApiResponse isLogin(){
        return userService.isLogin();
    }

    @PostMapping("/logout")
    public ApiResponse logout(){
        return new ApiResponse(ApiResponse.Status.success);
    }

    @PostMapping("/refresh")
    public ApiResponse refresh(@RequestParam String refresh, @RequestParam String username){
        return userService.refresh(refresh, username);
    }

}