package com.choi.api.biz.user.dao;

import com.choi.api.biz.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByEmailAndUsername(String email, String username);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE TB_USER_BASIC SET PASSWORD = :#{#user.password}, PASSWORD_FAIL = 0, SYS_UPDATE_DATE = NOW() WHERE USERNAME = :#{#user.username}")
    int updateByUsername(@Param("user") User user);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE TB_USER_BASIC SET PASSWORD = :#{#user.password}, PASSWORD_FAIL = 0, SYS_UPDATE_DATE = NOW() WHERE ID = :#{#user.id}")
    int updateByUsernameAndId(@Param("user") User user);

    Optional<User> findById(String userId);

}
