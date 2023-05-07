package mju.capstone.cms.domain.student.controller;

import lombok.RequiredArgsConstructor;
import mju.capstone.cms.domain.auth.jwt.provider.JwtProvider;
import mju.capstone.cms.domain.student.dto.StudentRegisterRequestDto;
import mju.capstone.cms.domain.student.dto.StudentRegisterResponseDto;
import mju.capstone.cms.domain.student.service.StudentService;
import mju.capstone.cms.domain.subject.dto.SubjectFocusRateDto;
import mju.capstone.cms.global.BaseResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final JwtProvider jwtProvider;

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

    // 학생 관리 수업 시간 - 학생 한명에 대한 집중도, 흥미도 조회
    @GetMapping("/class/{studentId}/{week}")
    public BaseResponse<List<SubjectFocusRateDto>> studentManagementForClass(
            @PathVariable("studentId") String studentId,
            @PathVariable("week") int week
    ) {
        return new BaseResponse<>(
                200,
                "학생 관리 - 수업시간",
                studentService.studentManagementForClass(studentId, week)
        );
    }
}