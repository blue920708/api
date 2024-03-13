package com.choi.api.biz.guest.controller;

import com.choi.api.biz.guest.model.BoardDTO;
import com.choi.api.biz.guest.service.BoardService;
import com.choi.api.core.annotation.UserAuth;
import com.choi.api.core.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/board")
@RestController
public class BoardController {

    @Autowired
    private BoardService service;

    @UserAuth
    @PostMapping("/add")
    public ApiResponse addBoard(@RequestBody BoardDTO.BoardAddReq req){
        return service.addBoard(req);
    }

    @UserAuth
    @PostMapping("/save")
    public ApiResponse saveBoard(@RequestBody BoardDTO.BoardSaveReq req){
        return service.saveBoard(req);
    }

    @UserAuth
    @GetMapping("/{seq}")
    public ApiResponse getBoard(@PathVariable("seq")int seq){
        return service.getBoard(seq);
    }

    @UserAuth
    @GetMapping("/list")
    public ApiResponse getBoardList(@RequestParam String keyword, @RequestParam int page, @RequestParam int size) { return service.getBoardList(keyword, page, size); }
}
