package mju.capstone.cms.domain.parent.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Builder
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Parent {

    @Id
    @Column(name = "id")
    private String id;

    @Column(nullable = false)
    private String password;
}