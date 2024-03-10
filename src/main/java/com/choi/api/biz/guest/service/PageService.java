package com.choi.api.biz.guest.service;
import com.choi.api.biz.guest.dao.PageRepository;
import com.choi.api.biz.guest.model.Page;
import com.choi.api.biz.guest.model.PageDTO;
import com.choi.api.core.exception.SystemException;
import com.choi.api.core.model.ApiResponse;
import com.choi.api.core.model.ApiResponseData;
import com.choi.api.core.model.ApiResponseErr;
import com.choi.api.core.security.service.JwtService;
import com.choi.api.core.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.choi.api.biz.guest.dao.PageRepositoryCustom;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PageService {

    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private PageRepositoryCustom pc;
    @Autowired
    private JwtService jwtService;

    public ApiResponse add(){

        try {
            String accessToken = jwtService.getAccessToken();
            String userId = jwtService.get(accessToken, "userId");

            List<Page> list = pageRepository.findById(userId);
            if(list.size() >= 5){
                return new ApiResponse(ApiResponse.Status.fail, "이용가능한 페이지 수를 초과하였습니다.");
            }

            Page page = Page.builder()
                    .id((userId))
                    .title("기본 페이지")
                    .content("")
                    .build();

            pageRepository.saveAndFlush(page);

            return new ApiResponseData<List<Page>>(list);
        } catch (Exception e) {
            log.debug(this.getClass().getName() + " 디버그 -> 오류 : " + e.getMessage());
            return new ApiResponseErr("SYSTEM_ERROR");
        }

    }

    public ApiResponse getPageList(){
        try {
            String accessToken = jwtService.getAccessToken();
            String userId = jwtService.get(accessToken, "userId");

            List<Page> list = pageRepository.findById(userId);
            List<PageDTO.PageListRes> res = list.stream().map(page ->
                PageDTO.PageListRes.builder()
                        .seq(page.getSeq())
                        .title(page.getTitle())
                        .build()
            ).collect(Collectors.toList());

            return new ApiResponseData<List<PageDTO.PageListRes>>(res);
        } catch (Exception e) {
            log.debug(this.getClass().getName() + " 디버그 -> 오류 : " + e.getMessage());
            throw new SystemException("SYSTEM_ERROR");
        }
    }

    public ApiResponse getPage(int seq){
        try {
            String accessToken = jwtService.getAccessToken();
            String userId = jwtService.get(accessToken, "userId");

            Page param = Page.builder()
                    .id(userId)
                    .seq(seq)
                    .build();

            Page page = pageRepository.selectPageByIdAndSeq(param);
            PageDTO.PageDetailRes res = PageDTO.PageDetailRes.builder()
                    .seq(page.getSeq())
                    .title(page.getTitle())
                    .content(page.getContent())
                    .build();
            return new ApiResponseData<PageDTO.PageDetailRes>(res);
        } catch (Exception e) {
            log.debug(this.getClass().getName() + " 디버그 -> 오류 : " + e.getMessage());
            throw new SystemException("SYSTEM_ERROR");
        }
    }

    @Transactional
    public ApiResponse savePage(PageDTO.PageSaveReq req){
        try {
            String accessToken = jwtService.getAccessToken();
            String userId = jwtService.get(accessToken, "userId");

            Page param = Page.builder()
                    .id(userId)
                    .seq(req.getSeq())
                    .title(req.getTitle())
                    .content(req.getContent())
                    .build();

            int res = pc.savePage(param);
            if(res == 0){
                return new ApiResponse(ApiResponse.Status.fail, "페이지 정보가 존재하지 않습니다.");
            }
            return new ApiResponse(ApiResponse.Status.success);
        } catch (Exception e) {
            log.debug(this.getClass().getName() + " 디버그 -> 오류 : " + e.getMessage());
            throw new SystemException("SYSTEM_ERROR");
        }
    }
}
