package com.choi.api.biz.guest.controller;

import com.choi.api.biz.guest.model.PageDTO;
import com.choi.api.biz.guest.service.PageService;
import com.choi.api.core.annotation.UserAuth;
import com.choi.api.core.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/page")
public class PageController {

    @Autowired
    private PageService pageService;

    @UserAuth
    @PostMapping("/add")
    public ApiResponse pageAdd(){
        return pageService.add();
    }

    @UserAuth
    @GetMapping("")
    public ApiResponse getList(){
        return pageService.getPageList();
    }

    @UserAuth
    @GetMapping("/{seq}")
    public ApiResponse getPage(@PathVariable int seq) {return pageService.getPage(seq);}

    @UserAuth
    @PostMapping("/save")
    public ApiResponse pageSave(@RequestBody PageDTO.PageSaveReq req) { return pageService.savePage(req); }

    @UserAuth
    @DeleteMapping("/{seq}")
    public ApiResponse deletePage(@PathVariable int seq) {return pageService.deletePage(seq);}

}
