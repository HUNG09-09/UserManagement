package project.java.qlsv.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @CreatedDate
    @Column(updatable = false)
    private Date CreateAt;

    @LastModifiedDate
    private Date updateAt;

    // ko bat buoc
    // mapby la ten thuoc tinh manytoone ben Entity User
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<User> users;
}
