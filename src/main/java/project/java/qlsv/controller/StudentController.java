package project.java.qlsv.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.java.qlsv.dto.*;
import project.java.qlsv.service.StudentService;
import project.java.qlsv.service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentService studentService;

    // gui form them moi di
    @PostMapping("/new")
    public ResponseDTO<Void> newStudent(@RequestBody @Valid StudentDTO studentDTO)
    {
        studentService.create(studentDTO);
        return ResponseDTO.<Void>builder().status(200).msg("create success").build();
    }

    @GetMapping("/")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseDTO<StudentDTO> get(@RequestParam("id") int id) {
        return ResponseDTO.<StudentDTO>builder()
                .status(200)
                .data(studentService.getById(id))
                .build();
    }

    @DeleteMapping("/delete")
    public ResponseDTO<Void> delete(@RequestParam("id") int id) {
        studentService.delete(id);
        return ResponseDTO.<Void>builder().status(200).msg("delete success").build();
    }



//    // yc trang danh sach student
    @GetMapping("/list")
    public ResponseDTO<List<StudentDTO>> list() {
        List<StudentDTO> studentDTOs = studentService.getAll();
        return ResponseDTO.<List<StudentDTO>>builder().status(200).data(studentDTOs).build();
    }
//
//    // search theo ten va tra ve danh sach
    @PostMapping("/search")
    public ResponseDTO<PageDTO<List<StudentDTO>>> search(@ModelAttribute @Valid SearchDTO searchDTO) {

        PageDTO<List<StudentDTO>> pageStudent = studentService.search(searchDTO);
        return ResponseDTO.<PageDTO<List<StudentDTO>>>builder().status(200).data(pageStudent).build();
    }
//
//
    @PutMapping("/edit")
    public ResponseDTO<StudentDTO> edit(@ModelAttribute StudentDTO studentDTO) {
        studentService.update(studentDTO);
        return ResponseDTO.<StudentDTO>builder()
                .status(200)
                .data(studentDTO)
                .build();
    }
}
