package com.api.backend.global.oauth2;

import com.api.backend.global.oauth2.userinfo.GoogleOAuth2UserInfo;
import com.api.backend.global.oauth2.userinfo.KakaoOAuth2UserInfo;
import com.api.backend.global.oauth2.userinfo.NaverOAuth2UserInfo;
import com.api.backend.global.oauth2.userinfo.OAuth2UserInfo;
import com.api.backend.member.data.entity.Member;
import com.api.backend.member.data.type.Authority;
import com.api.backend.member.data.type.LoginType;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.api.backend.member.data.type.LoginType.*;

@Getter
@Slf4j
public class OAuthAttributes {

    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    @Builder
    private OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    public static OAuthAttributes of(LoginType loginType,
                                     String userNameAttributeName, Map<String, Object> attributes) {

        if (loginType == NAVER) {
            log.info("네이버 로그인");
            return ofNaver(userNameAttributeName, attributes);
        }else if (loginType == KAKAO) {
            log.info("카카오 로그인");
            return ofKakao(userNameAttributeName, attributes);
        }
        else if (loginType == GOOGLE) {
            log.info("구글 로그인");
            return ofGoogle(userNameAttributeName, attributes);
        }
        else{
            log.info("로그인 타입이 문제 발생");
            return null;
        }
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    public Member toEntity(LoginType loginType, OAuth2UserInfo oauth2UserInfo) {
        return Member.builder()
                .loginType(loginType)
                .socialId(oauth2UserInfo.getId())
                .email(oauth2UserInfo.getEmail())
                .authority(Authority.USER)
                .isAuthenticatedEmail(true)
                .build();
    }
}
