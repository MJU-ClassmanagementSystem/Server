package mju.capstone.cms.focus.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.capstone.cms.student.entity.Student;
import mju.capstone.cms.subject.entity.Subject;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@DynamicInsert
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Focus {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double focusRate;

    @CreatedDate
    private LocalDate date; // 날짜만

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;
}