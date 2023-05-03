package mju.capstone.cms.domain.teacher.Controller;

import lombok.RequiredArgsConstructor;
import mju.capstone.cms.domain.focus.entity.Focus;
import mju.capstone.cms.domain.subject.Dto.SubjectFocusRateDto;
import mju.capstone.cms.domain.teacher.Service.TeacherService;
import mju.capstone.cms.global.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    // 수업 관리
    @GetMapping("/class/{week}")
    public BaseResponse<List<SubjectFocusRateDto>> manageClass2(@PathVariable("week") int week) {
        return new BaseResponse<>(
                200,
                "수업 관리!",
                teacherService.manageClass(week)
        );
    }




    ///////////////////////////////
    // 사용 안하는 api. 참고용
    @GetMapping("/test2/{start}/{end}")
    public Boolean isDate(@PathVariable("start") String start, @PathVariable("end") String end) throws ParseException {
        return teacherService.isDate(start, end);
    }

    // 해당 날짜에 해당 수업 들은 focus 객체 찾기 (수업 들은 모든 학생에 대해)
    @GetMapping("/findFocus/{date}/{subjectId}")
    public List<Focus> findFocus(@PathVariable("date") String date, @PathVariable("subjectId") Long subjectId) throws ParseException {
        return teacherService.findFocus(date, subjectId);
    }

    // 해당 날짜에 해당 수업 들은 focus 객체 찾기 (수업 들은 모든 학생에 대해)
    // 찾고 해당 과목 평균 계산
    @GetMapping("/calcSubjectFocus/{date}/{subjectId}")
    public Double calcSubjectFocus(@PathVariable("date") String date, @PathVariable("subjectId") Long subjectId) throws ParseException {
        return teacherService.calcSubjectFocus(date, subjectId);
    }
}
