package project.java.qlsv.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Data;
import project.java.qlsv.entity.User;

@Data
public class StudentDTO {
//    private int studentId; //user_id

    private UserDTO user; // user_id

    private String studentCode;
}
