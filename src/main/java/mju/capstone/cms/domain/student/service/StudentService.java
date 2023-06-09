package mju.capstone.cms.domain.student.service;

import lombok.RequiredArgsConstructor;
import mju.capstone.cms.domain.attendance.entity.Attendance;
import mju.capstone.cms.domain.attendance.repository.AttendanceRepository;
import mju.capstone.cms.domain.emotion.dto.StudentDayEmotionDto;
import mju.capstone.cms.domain.emotion.entity.Emotion;
import mju.capstone.cms.domain.emotion.repository.EmotionRepository;
import mju.capstone.cms.domain.focus.entity.Focus;
import mju.capstone.cms.domain.focus.repository.FocusRepository;
import mju.capstone.cms.domain.parent.entity.Parent;
import mju.capstone.cms.domain.parent.repository.ParentRepository;
import mju.capstone.cms.domain.student.dto.AttendanceDto;
import mju.capstone.cms.domain.student.dto.StudentDto;
import mju.capstone.cms.domain.student.dto.StudentRegisterResponseDto;
import mju.capstone.cms.domain.student.entity.Student;
import mju.capstone.cms.domain.student.repository.StudentRepository;
import mju.capstone.cms.domain.subject.Repository.SubjectRepository;
import mju.capstone.cms.domain.subject.dto.SubjectFocusRateDto;
import mju.capstone.cms.domain.subject.entity.Subject;
import mju.capstone.cms.domain.teacher.Repository.TeacherRepository;
import mju.capstone.cms.domain.teacher.entity.Teacher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final EmotionRepository emotionRepository;
    private final SubjectRepository subjectRepository;
    private final FocusRepository focusRepository;
    private final AttendanceRepository attendanceRepository;
    private final ParentRepository parentRepository;

    public StudentRegisterResponseDto register(String id, String name, String teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalStateException("Teacher이 없습니다"));
        Student student = Student.builder()
                .id(id)
                .name(name)
                .teacher(teacher)
                .build();
        studentRepository.save(student);
        return new StudentRegisterResponseDto(id, name, teacherId);
    }

    // 유저의 모든 학생 조회
    public List<StudentDto> getAllStudent(String userId, String userType) {
        List<Student> students = new ArrayList<>();
        if (userType.equals("teacher")) {
            Teacher teacher = teacherRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("teacher not found"));
            students = studentRepository.findByTeacher(teacher);
        } else if (userType.equals("parent")) {
            Parent parent = parentRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("parent not found"));
            students = studentRepository.findByParent(parent);
        }
        List<StudentDto> studentDtos = new ArrayList<>();

        for (Student student : students) {
            studentDtos.add(student.toDto());
        }

        return studentDtos;
    }


    // 학생 삭제
    public String delete(String studentId) {
        studentRepository.deleteById(studentId);
        return studentId;
    }

    // 학생 관리 수업 시간 - 학생 한명에 대한 집중도, 흥미도 조회
    public List<SubjectFocusRateDto> studentManagementForClass(String studentId, int week) {

        List<SubjectFocusRateDto> SubjectFocusRateList = new ArrayList<>();
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("student not found"));

        // 학생이 듣는 과목 뭔지 어떻게 알까?
        // 학생의 교사가 가르치는 모든 과목
        Teacher teacher = teacherRepository.findById(student.getTeacher().getId())
                .orElseThrow(() -> new IllegalArgumentException("teacher not found"));

        // 교사의 수업 목록
        List<Subject> subjectList = subjectRepository.findByTeacher(teacher);

        // 과목 객체
        for (Subject subject : subjectList) {
            SubjectFocusRateDto SubjectFocusRate = new SubjectFocusRateDto();
            SubjectFocusRate.setSubjectName(subject.getSubjectName());

            // 오류 -> db에 데이터 다 맞게 들어있어야 오류 안남.
            // 집중도
            SubjectFocusRate.setFocusRate(calcWeekFocus(studentId, week, subject.getId()));
            // 흥미도
            SubjectFocusRate.setInterestRate(calcWeekInterest(studentId, week, subject.getId()));

            SubjectFocusRateList.add(SubjectFocusRate);
        }

        return SubjectFocusRateList;
    }

    // 사용
    // 주차별로 해당 과목 평균 집중도 리스트 반환
    // 집중도 공식 : focusRate들의 평균
    public List<Double> calcWeekFocus(String studentId, int week, Long subjectId) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.add(Calendar.DATE, -(week * 7));

        // 한 과목에 대해서만 집중 객체들 (subject id == 1)

        List<Double> focusRateList = new ArrayList<>();

        // 일주일 치 (5일) 반복
        for (int i = 0; i < 5; i++) {
            cal.add(Calendar.DATE, Calendar.MONDAY + i - (cal.get(Calendar.DAY_OF_WEEK)));
            System.out.println("월요일 날짜 : " + sdf.format(cal.getTime()));
            LocalDate monday1 = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());

            Focus focus;

            try {
                focus = focusRepository.findBySubjectIdAndStudentIdAndDateBetween(subjectId, studentId, monday1, monday1);

                System.out.println("!!!!!" + focus.getFocusRate());

                focusRateList.add(focus.getFocusRate());
            } catch (NullPointerException e) {
                focusRateList.add(0.0);
            }
        }

        return focusRateList;
    }


    // 사용
    // 주차별로 해당 과목 평균 흥미도 리스트 반환
    // 집중도 공식 : focusRate
    // 흥미도 가짜 공식 : happy 감정만 추출
    public List<Double> calcWeekInterest(String studentId, int week, Long subjectId) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.add(Calendar.DATE, -(week * 7));

        // 한 과목에 대해서만 흥미도(감정) 객체들 (subject id == 1)

        List<Double> interestRateList = new ArrayList<>();

        // 일주일 치 (5일) 반복
        for (int i = 0; i < 5; i++) {
            cal.add(Calendar.DATE, Calendar.MONDAY + i - (cal.get(Calendar.DAY_OF_WEEK)));
            System.out.println("월요일 날짜 : " + sdf.format(cal.getTime()));
            LocalDate monday1 = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());

            Emotion emotion;
            try {
                emotion = emotionRepository.findBySubjectIdAndStudentIdAndDateBetween(subjectId, studentId, monday1, monday1);
                Focus focus = focusRepository.findBySubjectIdAndStudentIdAndDateBetween(subjectId, emotion.getStudent().getId(), monday1, monday1);
                double interestScore = (emotion.getHappy() + emotion.getNeutral() + emotion.getSurprise() - emotion.getAngry() - emotion.getDisgust() - emotion.getFear() - emotion.getSad() + 1) * 50;
                double focusScore = focus.getFocusRate();
                double result = (interestScore + focusScore) / 2;
                interestRateList.add(result);
            } catch (NullPointerException e) {
                interestRateList.add(0.0);
            }
        }

        return interestRateList;
    }

    // 학생 관리 - 학교생활 - 학생 한명에 대한 감정 조회
    public List<StudentDayEmotionDto> studentManagementForRecess(String studentId, int week) {
        List<StudentDayEmotionDto> StudentDayEmotionList = new ArrayList<>();
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("student not found"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.add(Calendar.DATE, -(week * 7));

        // 월화수목금
        for (int i = 0; i < 5; i++) {

            cal.add(Calendar.DATE, Calendar.MONDAY + i - (cal.get(Calendar.DAY_OF_WEEK)));
            System.out.println("월요일 날짜 : " + sdf.format(cal.getTime()));
            LocalDate day = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());

            StudentDayEmotionDto studentDayEmotion = new StudentDayEmotionDto();
            studentDayEmotion.setDay(i);
            studentDayEmotion.setEmotionList(calcDayEmotion(day, student));

            StudentDayEmotionList.add(studentDayEmotion);
        }
        return StudentDayEmotionList;
    }

    // 사용
    // 날짜별로 해당 감정 퍼센트 리스트 반환
    public List<Double> calcDayEmotion(LocalDate day, Student student) {

        List<Double> resultList = new ArrayList<>();

        try {
            List<Emotion> emotionList = emotionRepository.findRecessListByStudentAndDateBetween(student, day, day);
            double[] emotionRateList = {0,0,0,0,0,0,0};
            int count = 0;
            for (Emotion emotion : emotionList) {

                emotionRateList[0] += emotion.getAngry();

                emotionRateList[1] += emotion.getDisgust();

                emotionRateList[2] += emotion.getFear();

                emotionRateList[3] += emotion.getHappy();

                emotionRateList[4] += emotion.getNeutral();

                emotionRateList[5] += emotion.getSad();

                emotionRateList[6] += emotion.getSurprise();
                count++;
            }
            resultList.add(emotionRateList[0]/count);
            resultList.add(emotionRateList[1]/count);
            resultList.add(emotionRateList[2]/count);
            resultList.add(emotionRateList[3]/count);
            resultList.add(emotionRateList[4]/count);
            resultList.add(emotionRateList[5]/count);
            resultList.add(emotionRateList[6]/count);
        } catch (Exception e) {
            resultList.add(0.0);
        }
        return resultList;
    }

    // 출석부
    public List<AttendanceDto> attendance(String userId, int week, String userType) {

        List<Student> studentList = new ArrayList<>();

        if (userType.equals("teacher")) {

            Teacher teacher = teacherRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("teacher not found"));

            studentList = studentRepository.findByTeacher(teacher);
        } else if (userType.equals("parent")) {
            Parent parent = parentRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("parent not found"));

            studentList = studentRepository.findByParent(parent);
        }

        List<AttendanceDto> attendanceList = new ArrayList<>();
        for (Student student : studentList) {
            AttendanceDto attendance = new AttendanceDto();
            attendance.setId(student.getId());
            attendance.setName(student.getName());
            attendance.setAttend(getStudentAttend(student.getId(), week));
            attendanceList.add(attendance);
        }
        return attendanceList;
    }

    // 학생 한명에 대한 출석 가져오기
    public List<Long> getStudentAttend(String studentId, int week) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.add(Calendar.DATE, -(week * 7));

        List<Long> attendList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            cal.add(Calendar.DATE, Calendar.MONDAY + i - (cal.get(Calendar.DAY_OF_WEEK)));
            System.out.println("i요일 날짜 : " + sdf.format(cal.getTime()));
            LocalDate day = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());

            List<Attendance> attendanceList = new ArrayList<>();
            ;

            try {
                attendanceList.add(attendanceRepository.findByStudentIdAndDateBetween(studentId, day, day));

                for (Attendance attendance : attendanceList) {
                    attendList.add(attendance.getAttendType());
                }

            } catch (NullPointerException e) {
                attendList.add(3L);
            }
        }
        return attendList;
    }
}
