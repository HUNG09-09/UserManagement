package project.java.qlsv.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Table(name = "user")
@Entity
public class User {
//    @OneToMany(mappedBy = "user")
//    private List<UserRole> roles;

    // neu bang chi co 2 cot, nhu bang user_role, voi cot id va role, thi dung cach nay
    // ko can tao mot Entity userRole riemg
    // de fetch = FetchType.EAGER thi ko can de @Transaction ben public UserDetails loadUserByUsername cua Uservice
    @ElementCollection(fetch = FetchType.EAGER) // để khi load user, sẽ load luôn role của user, mới tổa man luc phan quyen admin
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<String> roles;

    @ManyToOne
    private Department department;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int age;
    private String name;

    @Column(unique = true)
    private String username;
    private String password;
    private String homeAddress;

    // luu ten file path
    private String avatarURL;

    @Temporal(TemporalType.DATE)
    private Date birthdate;

    private String email;
}
