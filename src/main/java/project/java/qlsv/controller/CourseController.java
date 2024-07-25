package project.java.qlsv.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.java.qlsv.dto.*;
import project.java.qlsv.service.CourseService;
import project.java.qlsv.service.DepartmentService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    CourseService courseService;

    // gui form them moi di
    @PostMapping("/new")
    public ResponseDTO<Void> create(
            @RequestBody @Valid CourseDTO courseDTO
    ) throws IOException, IllegalStateException {
        courseService.create(courseDTO);
        return ResponseDTO.<Void>builder().status(200).msg("ok").build();
    }



    // xoa nguoi dung theo id
    @DeleteMapping("/delete")
    public ResponseDTO<Void> delete(@RequestParam("id") int id)  {
        courseService.delete(id);
        return ResponseDTO.<Void>builder().status(200).msg("ok").build();
    }


    // search theo ten va tra ve danh sach
    @PostMapping("/search")
    public  ResponseDTO<PageDTO<List<CourseDTO>>> search(@ModelAttribute @Valid SearchDTO searchDTO) {

        PageDTO<List<CourseDTO>> pageDTO = courseService.search(searchDTO);

        return ResponseDTO.<PageDTO<List<CourseDTO>>>builder()
                .status(200)
                .data(pageDTO)
                .msg("ok").build();
    }

    @GetMapping("/")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseDTO<CourseDTO> get(@RequestParam("id") int id) {
        return ResponseDTO.<CourseDTO>builder()
                .status(200)
                .data(courseService.getById(id))
                .build();
    }

    // lay thong tin user de edit
//    @GetMapping("/edit")
//    public String editUser(Model model, @RequestParam("id") int id) {
//        DepartmentDTO departmentDTO = departmentService.getById(id);
//        model.addAttribute("department", departmentDTO); // day user can edit qua view
//        return "edit-department.html";
//    }

    // edit user
    @PutMapping("/edit")
    public ResponseDTO<CourseDTO> edit(@RequestBody  @Valid CourseDTO courseDTO) {
        courseService.update(courseDTO);
        return ResponseDTO.<CourseDTO>builder()
                .status(200)
                .data(courseDTO)
                .build();
    }
}
