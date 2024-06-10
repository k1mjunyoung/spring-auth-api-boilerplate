package SpringAuthApiBoilerplate.auth;

import SpringAuthApiBoilerplate.entity.CustomUser;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final CustomUser zuser;

    public final CustomUser getCustomUser() {
        return zuser;
    }

    @Override
    public String getUsername() {
        return zuser.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return zuser.getRoles().stream().map(o -> new SimpleGrantedAuthority(o.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return zuser.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
