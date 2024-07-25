package project.java.qlsv.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Student {

    @Id
    private int userId; //user_id, studentId

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER) //fetch = FetchType.EAGER mac dinh onetoone
    @PrimaryKeyJoinColumn // FK cua bang user chung cot (trung nhau) voi userId cua student
    @MapsId // copy id cua user de set cho student
    private User user; //user id

    private String studentCode;

    @OneToMany(mappedBy = "student")
    private List<Score> scores;
}
