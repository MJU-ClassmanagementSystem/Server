package mju.capstone.cms.domain.teacher.Service;

import lombok.RequiredArgsConstructor;
import mju.capstone.cms.domain.emotion.entity.Emotion;
import mju.capstone.cms.domain.emotion.repository.EmotionRepository;
import mju.capstone.cms.domain.focus.entity.Focus;
import mju.capstone.cms.domain.focus.repository.FocusRepository;
import mju.capstone.cms.domain.subject.Dto.SubjectFocusRateDto;
import mju.capstone.cms.domain.subject.Repository.SubjectRepository;
import mju.capstone.cms.domain.subject.entity.Subject;
import mju.capstone.cms.domain.teacher.Repository.TeacherRepository;
import mju.capstone.cms.domain.teacher.entity.Teacher;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TeacherService {

    // reqArgconstructor 쓰면 final 붙여야 함! 아니면 오류
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final FocusRepository focusRepository;
    private final EmotionRepository emotionRepository;

    // 수업 관리
    // 교사 id 가 1이라고 가정
    // subject의 averageFocusRate 가져오면 됨.
    public List<SubjectFocusRateDto> manageClass(int week) {

        List<SubjectFocusRateDto> SubjectFocusRateList = new ArrayList<>();

        ////
        // 교번 1인 교사
        Teacher teacher = teacherRepository.findById("1")
                .orElseThrow(() -> new IllegalArgumentException("teacher not found"));

        // 교사1의 수업 목록
        List<Subject> subjectList = subjectRepository.findByTeacher(teacher);
        // 출력
        System.out.println("교사1의 수업 목록!!");
        subjectList.stream().forEach(s -> System.out.println(s.getSubjectName()));

//        // 과목id만 가져와서 List 만들기
//        List<Long> subjectIdList = subjectList.stream()
//                .map(subject -> subject.getId())
//                .collect(Collectors.toList());

        // 과목 객체
        for (Subject subject : subjectList) {
            SubjectFocusRateDto SubjectFocusRate = new SubjectFocusRateDto();
            SubjectFocusRate.setSubjectName(subject.getSubjectName());
            // 집중도
            SubjectFocusRate.setFocusRate(calcWeekFocus(week, subject.getId()));
            // 흥미도
            SubjectFocusRate.setInterestRate(calcWeekinterest(week, subject.getId()));
            
            SubjectFocusRateList.add(SubjectFocusRate);
        }

        return SubjectFocusRateList;
    }

    // 사용
    // 주차별로 해당 과목 평균 집중도 리스트 반환
    // 집중도 공식 : focusRate들의 평균
    public List<Double> calcWeekFocus(int week, Long subjectId) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.add(Calendar.DATE, -(week * 7));

        System.out.println("오늘 날짜: " + sdf.format(cal.getTime()));

        cal.add(Calendar.DATE, Calendar.MONDAY - (cal.get(Calendar.DAY_OF_WEEK)));
        System.out.println("월요일 날짜 : " + sdf.format(cal.getTime()));
        LocalDate monday = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());

        cal.add(Calendar.DATE, Calendar.FRIDAY - (cal.get(Calendar.DAY_OF_WEEK)));
        System.out.println("금요일 날짜 : " + sdf.format(cal.getTime()));
        LocalDate friday = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());

        System.out.println("오늘 날짜22: " + sdf.format(cal.getTime()));

        // 한 과목에 대해서만 집중 객체들 (subject id == 1)

        List<Double> focusRateList = new ArrayList<>();

        // 일주일 치 (5일) 반복
        for (int i=0; i<5; i++) {
            cal.add(Calendar.DATE, Calendar.MONDAY+i - (cal.get(Calendar.DAY_OF_WEEK)));
            System.out.println("월요일 날짜 : " + sdf.format(cal.getTime()));
            LocalDate monday1 = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());
            List<Focus> focusList = focusRepository.findBySubjectIdAndDateBetween(subjectId, monday1, monday1);

            Double result = 0.0;
            int count = 0;

            for (Focus f : focusList) {
                result += f.getFocusRate();
                count++;
            }
            focusRateList.add(result / count);
        }

        return focusRateList;
    }


    // 사용
    // 주차별로 해당 과목 평균 흥미도 리스트 반환
    // 집중도 공식 : focusRate들의 평균
    // 흥미도 가짜 공식 : happy 감정의 평균
    public List<Double> calcWeekinterest(int week, Long subjectId) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.add(Calendar.DATE, -(week * 7));

        System.out.println("오늘 날짜: " + sdf.format(cal.getTime()));

        cal.add(Calendar.DATE, Calendar.MONDAY - (cal.get(Calendar.DAY_OF_WEEK)));
        System.out.println("월요일 날짜 : " + sdf.format(cal.getTime()));
        LocalDate monday = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());

        cal.add(Calendar.DATE, Calendar.FRIDAY - (cal.get(Calendar.DAY_OF_WEEK)));
        System.out.println("금요일 날짜 : " + sdf.format(cal.getTime()));
        LocalDate friday = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());

        System.out.println("오늘 날짜22: " + sdf.format(cal.getTime()));

        // 한 과목에 대해서만 흥미도(감정) 객체들 (subject id == 1)

        List<Double> interestRateList = new ArrayList<>();

        // 일주일 치 (5일) 반복
        for (int i=0; i<5; i++) {
            cal.add(Calendar.DATE, Calendar.MONDAY+i - (cal.get(Calendar.DAY_OF_WEEK)));
            System.out.println("월요일 날짜 : " + sdf.format(cal.getTime()));
            LocalDate monday1 = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());
            List<Emotion> emotionList = emotionRepository.findBySubjectIdAndDateBetween(subjectId, monday1, monday1);

            Double result = 0.0;
            int count = 0;

            for (Emotion e : emotionList) {
                result += e.getHappy();
                count++;
            }
            interestRateList.add(result / count);
        }

        return interestRateList;
    }
    
    

    //회원가입
    public String signup(String id, String password, String name, String school) {

        //이미 있는 아이디 -> 예외
        teacherRepository.findById(id)
            .ifPresent(teacher -> {
                throw new IllegalStateException("teacher already exists");
            });


        Teacher teacher = Teacher.builder()
            .id(id)
            .password(password)
            .name(name)
            .school(school)
            .build();

        Teacher save = teacherRepository.save(teacher);
        return save.getId();
    }





    ///////////////////////////////
    // 사용 안하는 메서드. 참고용

    // focusRate 구하는 메서드 (이번주, 국어) // (1주전, 영어)
    // 이번주 월요일~금요일 내에 특정 과목의 집중도
    public List<Double> getFocusRate(int week, Long subjectId) {

//        LocalDate before = LocalDate.of(2023,05,01);
//        LocalDate now = LocalDate.now();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        Date date = new Date();
        Calendar cal = Calendar.getInstance(Locale.KOREA);
//        cal.setTime(date);
        cal.add(Calendar.DATE, -(week * 7));

        System.out.println("오늘 날짜: " + sdf.format(cal.getTime()));

        cal.add(Calendar.DATE, Calendar.MONDAY - (cal.get(Calendar.DAY_OF_WEEK)));
        System.out.println("월요일 날짜 : " + sdf.format(cal.getTime()));
        LocalDate monday = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());

//        cal.setTime(date);
        cal.add(Calendar.DATE, Calendar.FRIDAY - (cal.get(Calendar.DAY_OF_WEEK)));
        System.out.println("금요일 날짜 : " + sdf.format(cal.getTime()));
        LocalDate friday = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());

        // 한 과목에 대해서만 집중 객체들 (subject id == 1)
        List<Focus> focusList = focusRepository.findBySubjectIdAndDateBetween(subjectId, monday, friday);

        List<Double> focusListRates = focusList.stream()
                .map(s -> s.getFocusRate())
                .collect(Collectors.toList());

        System.out.println("focusListRates = " + focusListRates);
        return focusListRates;
    }

    // focusRate 구하는 메서드 (이번주, 국어) // (1주전, 영어)
    // 이번주 월요일~금요일 내에 특정 과목의 집중도
    // 학생들의 평균 구해야 함.
    public List<Double> getFocusRate2(int week, Long subjectId) {

//        LocalDate before = LocalDate.of(2023,05,01);
//        LocalDate now = LocalDate.now();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        Date date = new Date();
        Calendar cal = Calendar.getInstance(Locale.KOREA);
//        cal.setTime(date);
        cal.add(Calendar.DATE, -(week * 7));

        System.out.println("오늘 날짜: " + sdf.format(cal.getTime()));

        cal.add(Calendar.DATE, Calendar.MONDAY - (cal.get(Calendar.DAY_OF_WEEK)));
        System.out.println("월요일 날짜 : " + sdf.format(cal.getTime()));
        LocalDate monday = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());

//        cal.setTime(date);
        cal.add(Calendar.DATE, Calendar.FRIDAY - (cal.get(Calendar.DAY_OF_WEEK)));
        System.out.println("금요일 날짜 : " + sdf.format(cal.getTime()));
        LocalDate friday = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());

        // 한 과목에 대해서만 집중 객체들 (subject id == 1)
        List<Focus> focusList = focusRepository.findBySubjectIdAndDateBetween(subjectId, monday, friday);

        List<Double> focusListRates = focusList.stream()
                .map(s -> s.getFocusRate())
                .collect(Collectors.toList());

        System.out.println("focusListRates = " + focusListRates);
        return focusListRates;
    }

    // 해당 날짜에 해당 수업 들은 focus 객체 찾기 (수업 들은 모든 학생에 대해)
    public List<Focus> findFocus(String date2, Long subjectId) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Date date = sdf.parse(date2);

        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.setTime(date);

        System.out.println("입력 날짜: " + sdf.format(cal.getTime()));
        LocalDate today = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());
        return focusRepository.findBySubjectIdAndDateBetween(subjectId, today, today);
    }

    // 해당 날짜에 해당 수업 들은 focus 객체 찾기 (수업 들은 모든 학생에 대해)
    // 찾고 해당 과목 평균 계산
    public Double calcSubjectFocus(String date2, Long subjectId) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Date date = sdf.parse(date2);

        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.setTime(date);

        System.out.println("입력 날짜: " + sdf.format(cal.getTime()));
        LocalDate today = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());
        List<Focus> focusList = focusRepository.findBySubjectIdAndDateBetween(subjectId, today, today);

        Double result = 0.0;
        int count = 0;

        for (Focus f : focusList) {
            result += f.getFocusRate();
            count++;
        }

        return result / count;
    }


    // 오늘이 시작, 끝 범위 안에 들면 T
    // 오늘이 끝이랑 같아도 T
    // https://eggwhite0.tistory.com/103
    public boolean isDate(String start, String end) throws ParseException {
        boolean result = false;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        Date currTime = new Date();
        String today = fmt.format(currTime);
        System.out.println("현재 날짜 : " + today);

        Date startDate = fmt.parse(start);
        Date endDate = fmt.parse(end);

        Date todate = fmt.parse(today);

        int compare1 = todate.compareTo(startDate);
        int compare2 = endDate.compareTo(todate);

        if (compare1 >= 0 && compare2 >= 0) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    // 이번주 월, 금요일 날짜
    public boolean isDate2(String start, String end) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        try {
            date = sdf.parse("20230503");
            System.out.println("date = " + date);
        } catch (ParseException e) {

        }
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.setTime(date);

        cal.add(Calendar.DATE, Calendar.MONDAY - (cal.get(Calendar.DAY_OF_WEEK)));
        System.out.println("월요일 날짜" + sdf.format(cal.getTime()));
        cal.setTime(date);
        cal.add(Calendar.DATE, Calendar.FRIDAY - (cal.get(Calendar.DAY_OF_WEEK)));
        System.out.println("금요일 날짜" + sdf.format(cal.getTime()));

        return true;
    }
}