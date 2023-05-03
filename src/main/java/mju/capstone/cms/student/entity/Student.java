package mju.capstone.cms.student.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.capstone.cms.parent.entity.Parent;
import mju.capstone.cms.teacher.entity.Teacher;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@DynamicInsert
@NoArgsConstructor
@Entity
public class Student {

    @Id
    @Column(name = "id")
    private String id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @Builder
    public Student(String id, String name, Teacher teacher, Parent parent) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.parent = parent;
    }
}