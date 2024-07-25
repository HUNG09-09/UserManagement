package project.java.qlsv.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.java.qlsv.dto.*;
import project.java.qlsv.entity.User;
import project.java.qlsv.service.DepartmentService;
import project.java.qlsv.service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    // gui form them moi di
    @PostMapping("/new")
    public ResponseDTO<Void> newUser(
            @ModelAttribute @Valid UserDTO userDTO
             ) throws IOException, IllegalStateException {
        if (!userDTO.getFile().isEmpty()) {
            // ten file upload
            String filename = userDTO.getFile().getOriginalFilename();
            // luu file vao o cung may chu
            File saveFile = new File("D:/" + filename);
            userDTO.getFile().transferTo(saveFile);
            // luu d.c file vaof database
            userDTO.setAvatarURL(filename);
        }
        userService.create(userDTO);
        return ResponseDTO.<Void>builder().status(200).msg("ok").build();
    }

    @GetMapping("/download") //? filename = abc.jpg
    public void download(
            @RequestParam("filename") String filename,
            HttpServletResponse resp) throws Exception
        {
           File file = new File("D:/" + filename);
            Files.copy(file.toPath(), resp.getOutputStream());
        }

    @GetMapping("/")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseDTO<UserDTO> get(@RequestParam("id") int id) {
        return ResponseDTO.<UserDTO>builder()
                .status(200)
                .data(userService.getById(id))
                .build();
    }

    // xoa nguoi dung theo id
    @DeleteMapping("/delete")
    public ResponseDTO<Void> delete(@RequestParam("id") int id) {
        userService.delete(id);
        return ResponseDTO.<Void>builder().status(200).msg("ok").build();
    }

    // yc trang danh sach user
    @GetMapping("/list")
    public ResponseDTO<List<UserDTO>> list() {
        List<UserDTO> userDTOs = userService.getAll();
        return ResponseDTO.<List<UserDTO>>builder().status(200).data(userDTOs).build();
    }

    // search theo ten va tra ve danh sach
    @PostMapping("/search")
    public ResponseDTO<PageDTO<List<UserDTO>>> search(@ModelAttribute @Valid SearchDTO searchDTO) {

        PageDTO<List<UserDTO>> pageUser = userService.searchName(searchDTO);
        return ResponseDTO.<PageDTO<List<UserDTO>>>builder().status(200).data(pageUser).build();
    }


    // edit user
    @PostMapping("/user/edit")
    public ResponseDTO<UserDTO> edit(@ModelAttribute @Valid UserDTO userDTO) throws Exception {

        if (!userDTO.getFile().isEmpty()) {
            // ten file upload
            String filename = userDTO.getFile().getOriginalFilename();
            // luu file vao o cung may chu
            File saveFile = new File("D:/" + filename);
            userDTO.getFile().transferTo(saveFile);
            // luu d.c file vaof database
            userDTO.setAvatarURL(filename);
        }

        userService.update(userDTO);
        return ResponseDTO.<UserDTO>builder().status(200).data(userDTO).build();
    }
}
