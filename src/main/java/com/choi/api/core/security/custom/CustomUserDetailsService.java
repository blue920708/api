package com.choi.api.core.security.custom;

import com.choi.api.biz.user.dao.UserRepository;
import com.choi.api.biz.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if(Objects.isNull(user)){
            throw new UsernameNotFoundException(username);
        }

        return new CustomUserDetails(user);
    }

}
