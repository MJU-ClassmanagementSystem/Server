package mju.capstone.cms.domain.student.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mju.capstone.cms.domain.student.dto.StudentDto;
import mju.capstone.cms.domain.teacher.entity.Teacher;
import mju.capstone.cms.domain.parent.entity.Parent;
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

    // 안쓰면 에러!
    // https://offetuoso.github.io/blog/develop/troubleshooting/jpa/no-serializer-found-for-class/
    @JsonIgnore
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

    public StudentDto toDto() {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(this.id);
        studentDto.setName(this.name);
        studentDto.setTeacherId(this.teacher.getId());
        studentDto.setParentId(this.parent.getId());
        return studentDto;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }
}