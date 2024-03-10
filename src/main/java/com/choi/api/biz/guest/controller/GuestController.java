package com.choi.api.biz.guest.controller;

import com.choi.api.biz.guest.model.PageDTO;
import com.choi.api.biz.guest.service.PageService;
import com.choi.api.core.annotation.UserAuth;
import com.choi.api.core.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guest")
public class GuestController {

    @Autowired
    private PageService pageService;

    @UserAuth
    @PostMapping("/page/add")
    public ApiResponse pageAdd(){
        return pageService.add();
    }

    @UserAuth
    @GetMapping("/page")
    public ApiResponse getList(){
        return pageService.getPageList();
    }

    @UserAuth
    @GetMapping("/page/{seq}")
    public ApiResponse getPage(@PathVariable int seq) {return pageService.getPage(seq);}

    @UserAuth
    @PostMapping("/page/save")
    public ApiResponse pageSave(@RequestBody PageDTO.PageSaveReq req) { return pageService.savePage(req); }

    @UserAuth
    @DeleteMapping("/page/{seq}")
    public ApiResponse deletePage(@PathVariable int seq) {return null;}

}
