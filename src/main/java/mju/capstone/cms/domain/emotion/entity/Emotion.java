package mju.capstone.cms.domain.emotion.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.capstone.cms.domain.student.entity.Student;
import mju.capstone.cms.domain.subject.entity.Subject;
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
public class Emotion {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDate date; // 날짜만

    @Column(nullable = false)
    private Double angry;

    @Column(nullable = false)
    private Double disgust;

    @Column(nullable = false)
    private Double fear;

    @Column(nullable = false)
    private Double happy;

    @Column(nullable = false)
    private Double sad;

    @Column(nullable = false)
    private Double surprise;

    @Column(nullable = false)
    private Double neutral;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;
}
