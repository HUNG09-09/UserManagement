package project.java.qlsv.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {
    private int status; // 200,400,500
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // neu dung builder thi ko can ham nay, va @NoArgsConstructor, @AllArgsConstructor
    public ResponseDTO(int status, String msg) {
        super();
        this.status = status;
        this.msg = msg;
    }
}
