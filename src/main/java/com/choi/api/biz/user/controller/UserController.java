package com.choi.api.biz.user.controller;

import com.choi.api.biz.mail.model.MailDTO;
import com.choi.api.core.annotation.UserAuth;
import com.choi.api.core.model.ApiResponse;
import com.choi.api.biz.user.model.UserDTO;
import com.choi.api.biz.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @PostMapping("/findPwd")
    public ApiResponse findId(@RequestBody UserDTO.FindPwdReq req) { return userService.findPwd(req);}

    @UserAuth
    @PostMapping("/chgPwd")
    public ApiResponse chgPwd(@RequestBody UserDTO.ChgPwdReq req) { return userService.chgPwd(req); }

    @UserAuth
    @GetMapping("/info")
    public ApiResponse getUser() { return userService.getUser(); }

    @GetMapping("/isLogin")
    public ApiResponse isLogin(){
        return userService.isLogin();
    }

    @PostMapping("/logout")
    public ApiResponse logout(){
        return new ApiResponse(ApiResponse.Status.success);
    }

    @PostMapping("/refresh")
    public ApiResponse refresh(@RequestBody UserDTO.RefreshReq req){
        return userService.refresh(req);
    }

}
