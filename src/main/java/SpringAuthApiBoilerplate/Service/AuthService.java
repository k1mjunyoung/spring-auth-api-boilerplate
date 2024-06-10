package SpringAuthApiBoilerplate.Service;

import SpringAuthApiBoilerplate.auth.JwtProvider;
import SpringAuthApiBoilerplate.dto.LoginRequestDto;
import SpringAuthApiBoilerplate.dto.ResponseDto;
import SpringAuthApiBoilerplate.dto.SignupRequestDto;
import SpringAuthApiBoilerplate.dto.SignupResponseDto;
import SpringAuthApiBoilerplate.entity.CustomUser;
import SpringAuthApiBoilerplate.repository.CustomUserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final CustomUserRepository customUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public ResponseDto login(LoginRequestDto request) throws Exception {
        CustomUser zuser = customUserRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));

        if (!passwordEncoder.matches(request.getPassword(), zuser.getPassword())) {
            throw new BadCredentialsException("비밀번호 불일치");
        }

        SignupResponseDto loginResult = SignupResponseDto.builder()
                .email(zuser.getEmail())
                .zname(zuser.getZname())
                .roles(zuser.getRoles())
                .token(jwtProvider.createToken(zuser.getEmail(), zuser.getRoles()))
                .build();

        ResponseDto<SignupResponseDto> responseDto = ResponseDto.<SignupResponseDto>builder()
                .resultcode(1)
                .data(loginResult)
                .build();

        return responseDto;
    }

    public ResponseDto signup(SignupRequestDto request) throws Exception {
        try {
            CustomUser zuser = CustomUser.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .zname(request.getZname())
                    .regDateTime(LocalDateTime.now())
                    .build();
             customUserRepository.save(zuser);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }

        ResponseDto response = ResponseDto.builder()
                .resultcode(1)
                .build();

        return response;
    }

    public ResponseDto getCustomUser(String email) throws Exception {
        CustomUser customUser = customUserRepository.findByEmail(email).orElseThrow(() ->
                new Exception("계정을 찾을 수 없습니다."));

        SignupResponseDto userInfo = new SignupResponseDto(customUser);

        ResponseDto<SignupResponseDto> response = ResponseDto.<SignupResponseDto>builder()
                .resultcode(1)
                .data(userInfo)
                .build();

        return response;
    }
}
