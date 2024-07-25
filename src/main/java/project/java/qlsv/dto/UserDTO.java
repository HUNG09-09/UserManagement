package project.java.qlsv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
public class UserDTO {

    private int id;
    @Min(value = 0)
    private int age;
    @NotBlank(message = "{not.blank}")
    private String name;
    private String username;
    private String password;
    private String homeAddress;

    // luu ten file path
    private String avatarURL;

    @JsonIgnore
    private MultipartFile file;

    private DepartmentDTO department;

    private List<String> roles;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date birthdate;
}