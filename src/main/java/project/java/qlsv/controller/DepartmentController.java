package project.java.qlsv.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.java.qlsv.dto.*;
import project.java.qlsv.service.DepartmentService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    // gui form them moi di
    @PostMapping("/new")
    public ResponseDTO<Void> create(
            @ModelAttribute @Valid DepartmentDTO departmentDTO
    ) throws IOException, IllegalStateException {
        departmentService.create(departmentDTO);
        return ResponseDTO.<Void>builder().status(200).msg("ok").build();
    }

    // neu day len dang JSON, dung RequestBody
    // nhung ko day dc file
    @PostMapping("/json")
    public ResponseDTO<Void> createNewJson(
            @RequestBody @Valid DepartmentDTO departmentDTO
    ) throws IOException, IllegalStateException {
        departmentService.create(departmentDTO);
        return ResponseDTO.<Void>builder().status(200).msg("ok").build();
    }


    // xoa nguoi dung theo id
    @DeleteMapping("/delete")
    public ResponseDTO<Void> delete(@RequestParam("id") int id)  {
        departmentService.delete(id);
        return ResponseDTO.<Void>builder().status(200).msg("ok").build();
    }


    // search theo ten va tra ve danh sach
    @PostMapping("/search")
    public  ResponseDTO<PageDTO<List<DepartmentDTO>>> search(@ModelAttribute @Valid SearchDTO searchDTO) {

        PageDTO<List<DepartmentDTO>> pageDTO = departmentService.search(searchDTO);

        return ResponseDTO.<PageDTO<List<DepartmentDTO>>>builder()
                .status(200)
                .data(pageDTO)
                .msg("ok").build();
    }

    @GetMapping("/")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseDTO<DepartmentDTO> get(@RequestParam("id") int id) {
        return ResponseDTO.<DepartmentDTO>builder()
                .status(200)
                .data(departmentService.getById(id))
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
    public ResponseDTO<DepartmentDTO> edit(@ModelAttribute DepartmentDTO departmentDTO) {
        departmentService.update(departmentDTO);
        return ResponseDTO.<DepartmentDTO>builder()
                .status(200)
                .data(departmentDTO)
                .build();
    }
}
