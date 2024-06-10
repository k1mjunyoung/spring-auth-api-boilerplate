package SpringAuthApiBoilerplate.controller;

import SpringAuthApiBoilerplate.Service.AuthService;
import SpringAuthApiBoilerplate.dto.LoginRequestDto;
import SpringAuthApiBoilerplate.dto.SignupRequestDto;
import SpringAuthApiBoilerplate.repository.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/api/auth")
public class AuthController {
    private CustomUserRepository customUserRepository;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDto request) throws Exception {
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody SignupRequestDto request) throws Exception {
        return new ResponseEntity<>(authService.signup(request), HttpStatus.OK);
    }

    @PostMapping("/user/info")
    public ResponseEntity getUserinfo(@RequestParam("email") String email) throws Exception {
        return new ResponseEntity<>(authService.getCustomUser(email), HttpStatus.OK);
    }
}
