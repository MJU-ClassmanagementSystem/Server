package mju.capstone.cms.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import mju.capstone.cms.domain.auth.dto.AccessToken;
import mju.capstone.cms.domain.auth.dto.LoginDto;
import mju.capstone.cms.domain.auth.service.AuthService;
import mju.capstone.cms.global.BaseResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public BaseResponse<AccessToken> login(@RequestBody LoginDto loginDto) {
        String token = authService.loginToken(loginDto.getId(), loginDto.getPassword());
        return new BaseResponse<>(
            200, "로그인 성공", new AccessToken(token)
        );
    }

    /**
     * token decode test
     * 사용 안함.
     */
    @GetMapping("/me")
    public BaseResponse<String> me(@RequestHeader("Authorization") String token) {
        return new BaseResponse<>(
            200,
            "/me",
            authService.me(token)
        );
    }
}
