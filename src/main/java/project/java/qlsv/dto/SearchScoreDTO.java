package project.java.qlsv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchScoreDTO extends SearchDTO{
    private Integer courseId;
    private Integer studentId;
}