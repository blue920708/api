package com.choi.api.biz.guest.service;

import com.choi.api.biz.guest.dao.BoardRepository;
import com.choi.api.biz.guest.model.Board;
import com.choi.api.biz.guest.model.BoardDTO;
import com.choi.api.core.exception.BizException;
import com.choi.api.core.exception.SystemException;
import com.choi.api.core.model.ApiResponse;
import com.choi.api.core.model.ApiResponseData;
import com.choi.api.core.security.service.JwtService;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BoardService {

    @Autowired
    private BoardRepository repository;
    @Autowired
    private JwtService jwtService;

    @Transactional
    public ApiResponse addBoard(BoardDTO.BoardAddReq req){
        String accessToken = jwtService.getAccessToken();
        String userId = jwtService.get(accessToken, "userId");

        try {
            Board param = Board.builder()
                    .id(userId)
                    .title(req.getTitle())
                    .content(req.getContent())
                    .eventTime(req.getEventTime())
                    .eventDate(req.getEventDate())
                    .eventType(req.getEventType())
                    .build();

            int seq = repository.saveAndFlush(param).getSeq();
            if(seq == 0){
                throw new BizException("작업 처리 중 오류가 발생하였습니다.");
            } else {
                return new ApiResponseData(ApiResponse.Status.success, seq);
            }

        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            log.debug(this.getClass().getName() + " 디버그 -> 오류 : {}", e.getMessage());
            throw new SystemException();
        }
    }

    @Transactional
    public ApiResponse saveBoard(BoardDTO.BoardSaveReq req){
        String accessToken = jwtService.getAccessToken();
        String userId = jwtService.get(accessToken, "userId");

        try {
            Board param = Board.builder()
                    .id(userId)
                    .seq(req.getSeq())
                    .title(req.getTitle())
                    .content(req.getContent())
                    .eventTime(req.getEventTime())
                    .eventDate(req.getEventDate())
                    .eventType(req.getEventType())
                    .build();

            int seq = repository.saveBoard(param);
            if(seq == 0){
                throw new BizException("작업 처리 중 오류가 발생하였습니다.");
            } else {
                return new ApiResponseData(ApiResponse.Status.success, seq);
            }

        } catch (BizException e) {
            throw new BizException(e.getMessage());
        } catch (Exception e) {
            log.debug(this.getClass().getName() + " 디버그 -> 오류 : {}", e.getMessage());
            throw new SystemException();
        }
    }

    public ApiResponse getBoard(int seq){

        String accessToken = jwtService.getAccessToken();
        String userId = jwtService.get(accessToken, "userId");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {

            Optional<Board> board = Optional.ofNullable(repository.findByIdAndSeq(userId, seq)
                    .orElseThrow(() -> new BizException("데이터가 존재하지 않습니다.")));

            BoardDTO.BoardDetailRes req = BoardDTO.BoardDetailRes.builder()
                    .title(board.get().getTitle())
                    .content(board.get().getContent())
                    .eventDate(board.get().getEventDate())
                    .eventTime(board.get().getEventTime())
                    .eventType(board.get().getEventType())
                    .insdate(board.get().getSysCreationDate() == null ? null : board.get().getSysCreationDate().format(formatter))
                    .update(board.get().getSysUpdateDate() == null ? null : board.get().getSysUpdateDate().format(formatter))
                    .build();

            return new ApiResponseData<BoardDTO.BoardDetailRes>(req);

        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            log.debug(this.getClass().getName() + " 디버그 -> 오류 : {}", e.getMessage());
            throw new SystemException();
        }
    }

    public ApiResponse getBoardList(String keyword, int page, int size){
        String accessToken = jwtService.getAccessToken();
        String userId = jwtService.get(accessToken, "userId");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            Pageable pageable = PageRequest.of(page <= 0 ? 0 : page-1, size, Sort.by(Sort.Direction.DESC, "seq"));
            Page<Board> boardList = repository.findAllByIdAndTitleContainingOrderBySeqDesc(userId, keyword, pageable);
            List<BoardDTO.BoardListRes> dataList = boardList.getContent().stream().map(board -> {
                return BoardDTO.BoardListRes.builder()
                        .seq(board.getSeq())
                        .title(board.getTitle())
                        .eventDate(board.getEventDate())
                        .eventTime(board.getEventTime())
                        .eventType(board.getEventType())
                        .insdate(board.getSysCreationDate().format(formatter))
                        .build();
            }).collect(Collectors.toList());

            Map<String, Object> res = new HashMap<>();
            res.put("dataList", dataList);
            res.put("count", boardList.getTotalElements());
            res.put("page", boardList.getPageable().getPageNumber()+1);

            return new ApiResponseData<Map<String, Object>>(res);
        } catch (Exception e) {
            log.debug(this.getClass().getName() + " 디버그 -> 오류 : {}", e.getMessage());
            throw new SystemException();
        }

    }

    public ApiResponse deleteBoard(){
        return null;
    }

}
