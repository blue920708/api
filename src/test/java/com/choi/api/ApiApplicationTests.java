package com.choi.api;

import com.choi.api.biz.guest.enums.EventType;
import com.choi.api.biz.user.dao.UserRepository;
import com.choi.api.biz.user.model.User;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
class ApiApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(EventType.JOB.getEventType());
    }

}
