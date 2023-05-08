package mju.capstone.cms.domain.emotion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class StudentDayEmotionDto {
    // 요일 -> enum으로 바꾸자
    private int day;
    // 감정
    private List<Double> emotionList;
}
