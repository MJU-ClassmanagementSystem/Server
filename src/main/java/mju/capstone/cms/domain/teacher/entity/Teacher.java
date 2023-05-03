package mju.capstone.cms.domain.teacher.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@DynamicInsert
@NoArgsConstructor
@Entity
public class Teacher {

    @Id
    @Column(name = "id")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String school;

    @Column(nullable = false)
    private String password;

    @Builder
    public Teacher(String id, String name, String school, String password) {
        this.id = id;
        this.name = name;
        this.school = school;
        this.password = password;
    }
}