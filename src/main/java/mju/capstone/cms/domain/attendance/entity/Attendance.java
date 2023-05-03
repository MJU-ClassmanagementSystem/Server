package mju.capstone.cms.domain.attendance.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.capstone.cms.domain.student.entity.Student;
import mju.capstone.cms.domain.teacher.entity.Teacher;
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
public class Attendance {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @CreatedDate
    private LocalDate date; // 날짜만

    @Column(nullable = false)
    @Enumerated
    private AttendType attendType;
}

enum AttendType {
    ATTEND, ABSENT, MISSING
}
