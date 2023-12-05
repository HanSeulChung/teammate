package com.api.backend.member.service.impl;

import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import com.api.backend.member.data.entity.Member;
import com.api.backend.member.data.entity.User;
import com.api.backend.member.data.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.api.backend.global.exception.type.ErrorCode.EMAIL_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceimpl implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String useremail) throws UsernameNotFoundException {
        Member principal = memberRepository.findByEmail(useremail)
                .orElseThrow(() -> {
                    return new CustomException(EMAIL_NOT_FOUND_EXCEPTION);
                });
        return new User(principal);
    }
}
