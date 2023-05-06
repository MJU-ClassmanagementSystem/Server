package mju.capstone.cms.domain.student.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mju.capstone.cms.domain.auth.jwt.provider.JwtProvider;
import mju.capstone.cms.domain.student.dto.StudentRegisterRequestDto;
import mju.capstone.cms.domain.student.dto.StudentRegisterResponseDto;
import mju.capstone.cms.domain.student.service.StudentService;
import mju.capstone.cms.global.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final JwtProvider jwtProvider;

    @Getter
    public static class testDto {
        private String time;
    }

    @PostMapping("/register")
    public BaseResponse<StudentRegisterResponseDto> register(@RequestHeader("Authorization") String token, @RequestBody StudentRegisterRequestDto requestDto) {
        String teacherId = jwtProvider.extractId(token);
        StudentRegisterResponseDto register = studentService.register(requestDto.getId(), requestDto.getName(), teacherId);
        return new BaseResponse<>(
            200,
            "학생 등록 완료",
            register
        );
    }

    @PostMapping("/test")

    public ResponseEntity<String> test(@RequestBody testDto test) {

        System.out.println("!!! time : " + test.getTime());

        // 파이썬에서 post 방식으로 json 보내면 스프링에서 저장하면 됨.
        // 리액트는 파이썬이랑 연동하면 됨?

        return ResponseEntity.ok("success");
    }
}
