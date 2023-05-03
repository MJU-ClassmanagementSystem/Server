package mju.capstone.cms.domain.focus.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Focus {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double focusRate;

    @CreatedDate
    private LocalDate date; // 날짜만

    // 안하면 오류!!
    // https://offetuoso.github.io/blog/develop/troubleshooting/jpa/no-serializer-found-for-class/
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @JsonIgnore // 안하면 오류!!
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;
}