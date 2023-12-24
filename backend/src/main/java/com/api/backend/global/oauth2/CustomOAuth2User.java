package com.api.backend.global.oauth2;

import com.api.backend.member.data.type.Authority;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
    private Long memberId;
    private String email;
    private Authority role;

    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
     *                         {@link #getAttributes()}
     *
     */
    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            Long memberId, String email, Authority role) {
        super(authorities, attributes, nameAttributeKey);
        this.memberId = memberId;
        this.email = email;
        this.role = role;
    }
}
