package dgp.misaeng.global.auth.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CustomOAuth2User implements OAuth2User {
    private final AuthUserInfo user;

    private final Long id;

    private final List<GrantedAuthority> authorities;

    private final Map<String, Object> attributes;

    public CustomOAuth2User(AuthUserInfo user, Map<String, Object> attributes) {
        this.user = user;
        this.id = user.getId();
        this.attributes = attributes;

        List<String> roles = Arrays.asList("USER");
        this.authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return this.user.getEmail();
    }

    public Long getId() {
        return this.id;
    }

    public AuthUserInfo getUserInfo() {
        return this.user;
    }
}
