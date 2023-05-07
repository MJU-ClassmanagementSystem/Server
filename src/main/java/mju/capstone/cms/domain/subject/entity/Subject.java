package mju.capstone.cms.domain.subject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.capstone.cms.domain.teacher.entity.Teacher;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Builder
@Getter
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Subject {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subjectName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}
