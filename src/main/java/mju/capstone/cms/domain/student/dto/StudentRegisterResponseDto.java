package mju.capstone.cms.domain.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRegisterResponseDto {
    private String id;
    private String name;
    private String teacherId;
}
