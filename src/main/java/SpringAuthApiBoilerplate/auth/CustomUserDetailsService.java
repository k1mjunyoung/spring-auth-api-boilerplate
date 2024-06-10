package SpringAuthApiBoilerplate.auth;

import SpringAuthApiBoilerplate.utils.CustomUserRole;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import SpringAuthApiBoilerplate.entity.CustomUser;
import SpringAuthApiBoilerplate.repository.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final CustomUserRepository customUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<CustomUser> _customUser = this.customUserRepository.findByEmail(email);

        if (_customUser.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        CustomUser customUser = _customUser.get();

        List<GrantedAuthority> authorities = new ArrayList<>();

        if ("jetz".equals(email)) {
            authorities.add(new SimpleGrantedAuthority(CustomUserRole.ADMIN.getRole()));
        } else {
            authorities.add(new SimpleGrantedAuthority(CustomUserRole.USER.getRole()));
        }

        return new User(customUser.getEmail(), customUser.getPassword(), authorities);
    }
}
