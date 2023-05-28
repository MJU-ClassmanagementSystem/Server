package mju.capstone.cms.domain.student.controller;

import lombok.RequiredArgsConstructor;
import mju.capstone.cms.domain.auth.jwt.provider.JwtProvider;
import mju.capstone.cms.domain.emotion.dto.StudentDayEmotionDto;
import mju.capstone.cms.domain.student.dto.AttendanceDto;
import mju.capstone.cms.domain.student.dto.StudentDto;
import mju.capstone.cms.domain.student.dto.StudentRegisterRequestDto;
import mju.capstone.cms.domain.student.dto.StudentRegisterResponseDto;
import mju.capstone.cms.domain.student.service.StudentService;
import mju.capstone.cms.domain.subject.dto.SubjectFocusRateDto;
import mju.capstone.cms.global.BaseResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(originPatterns = "http://127.0.0.1:5174")
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

    // 유저의 모든 학생 조회
    @GetMapping("")
    public BaseResponse<List<StudentDto>> getAllStudent(@RequestHeader("Authorization") String token) {
        String userId = jwtProvider.extractId(token);
        String userType = jwtProvider.extractType(token);
        return new BaseResponse<>(
                200,
                "유저의 모든 학생 조회 성공",
                studentService.getAllStudent(userId, userType)
        );
    }

    // 학생 삭제
    @DeleteMapping("")
    public BaseResponse<String> delete(@RequestHeader("Authorization") String token, @RequestBody StudentRegisterRequestDto requestDto) {
        String teacherId = jwtProvider.extractId(token);
        return new BaseResponse<>(
                200,
                "학생 삭제 완료",
                studentService.delete(requestDto.getId())
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

    // 학생 관리 - 학교생활 - 학생 한명에 대한 감정 조회
    @GetMapping("/recess/{studentId}/{week}")
    public BaseResponse<List<StudentDayEmotionDto>> studentManagementForRecess(
            @PathVariable("studentId") String studentId,
            @PathVariable("week") int week
    ) {
        return new BaseResponse<>(
                200,
                "학생 관리 - 학교생활",
                studentService.studentManagementForRecess(studentId, week)
        );
    }

    // 출석부
    // 교사의 모든 학생들의 출석부
    // 유저가 교사인 경우, 학부모인 경우 반환값 분리
    @GetMapping("/attendance/{week}")
    public BaseResponse<List<AttendanceDto>> attendance(@RequestHeader("Authorization") String token, @PathVariable("week") int week) {
        String userId = jwtProvider.extractId(token);
        String userType = jwtProvider.extractType(token);
        return new BaseResponse<>(
                200,
                "출석부",
                studentService.attendance(userId, week, userType)
        );
    }
}