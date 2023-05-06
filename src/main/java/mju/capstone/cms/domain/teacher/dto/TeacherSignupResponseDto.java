package mju.capstone.cms.domain.teacher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSignupResponseDto {
    private String id;
    private String name;
    private String school;
}
