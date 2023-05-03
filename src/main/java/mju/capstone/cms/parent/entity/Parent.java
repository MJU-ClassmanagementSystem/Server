package mju.capstone.cms.parent.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@DynamicInsert
@NoArgsConstructor
@Entity
public class Parent {

    @Id
    @Column(name = "id")
    private String id;

    @Column(nullable = false)
    private String password;
}