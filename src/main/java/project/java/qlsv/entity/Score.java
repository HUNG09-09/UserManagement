package project.java.qlsv.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Score extends TimeAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double score;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Student student;
}
