package SpringAuthApiBoilerplate.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    @Value("${jwt.secret.key}")
    private String salt;

    private Key secertKey;

    //만료시간 : 1시간
    private final long exp = 1000L * 60L * 60L;

    private final CustomUserDetailsService customUserDetailsService;

    @PostConstruct
    protected void init() {
        secertKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    //토큰 생성
    public String createToken(String email, List<Authority> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + exp))
                .signWith(secertKey, SignatureAlgorithm.HS256)
                .compact();
    }

    //권한정보 획득
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(this.getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //토큰에 담겨있는 유저 email 획득
    public String getEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(secertKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    //Authorization Header를 이용해 인증
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public boolean validateToken(String token) {
        try {
            //Bearer 인증
            if (!token.substring(0, "BEARER".length()).equalsIgnoreCase("BEARER")) {
                return false;
            }
            else {
                token = token.split(" ")[1].trim();
            }

            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secertKey).build().parseClaimsJws(token);

            //만료되었을 시 false
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
