package mju.capstone.cms.domain.subject.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter @Setter
public class SubjectFocusRateDto {
    // 과목 명
    private String subjectName;
    // 집중도
    private List<Double> focusRate;
    // 흥미도
    private List<Double> interestRate;

    @Builder
    public SubjectFocusRateDto(String subjectName, List<Double> focusRate, List<Double> interestRate) {
        this.subjectName = subjectName;
        this.focusRate = focusRate;
        this.interestRate = interestRate;
    }
}
