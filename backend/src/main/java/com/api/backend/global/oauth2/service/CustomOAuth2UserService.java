package com.api.backend.global.oauth2.service;

import com.api.backend.global.oauth2.CustomOAuth2User;
import com.api.backend.global.oauth2.OAuthAttributes;
import com.api.backend.member.data.entity.Member;
import com.api.backend.member.data.repository.MemberRepository;
import com.api.backend.member.data.type.LoginType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        LoginType loginType = getLoginType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes extractAttributes = OAuthAttributes.of(loginType, userNameAttributeName, attributes);

        Member createdMember = getMember(extractAttributes, loginType);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdMember.getAuthority().getDescription())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdMember.getMemberId(),
                createdMember.getEmail(),
                createdMember.getAuthority()
        );
    }

    private LoginType getLoginType(String registrationId) {
        if(NAVER.equals(registrationId)) {
            return LoginType.NAVER;
        }
        if(KAKAO.equals(registrationId)) {
            return LoginType.KAKAO;
        }
        return LoginType.GOOGLE;
    }

    private Member getMember(OAuthAttributes attributes, LoginType loginType) {
        Member findMember = memberRepository.findByLoginTypeAndSocialId(loginType,
                attributes.getOauth2UserInfo().getId()).orElse(null);

        if(findMember == null) {
            return saveMember(attributes, loginType);
        }
        return findMember;
    }
    private Member saveMember(OAuthAttributes attributes, LoginType loginType) {
        Member createdMember = attributes.toEntity(loginType, attributes.getOauth2UserInfo());
        return memberRepository.save(createdMember);
    }
}