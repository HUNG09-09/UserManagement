package project.java.qlsv.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.java.qlsv.dto.*;
import project.java.qlsv.service.ScoreService;

import java.util.List;

@RestController
@RequestMapping("/score")
public class ScoreController {
    @Autowired
    ScoreService scoreService;

    // them moi course
    @PostMapping("/new")
    public ResponseDTO<Void> create(
            @RequestBody @Valid ScoreDTO scoreDTO)
    {
        scoreService.create(scoreDTO);
        return ResponseDTO.<Void>builder().status(200).msg("create success").build();
    }

    // neu du lieu gui len chi co text ( string ) thi se day dinh dang JSON theo cách nay, trong PM se là raw (json)
    // dung requestBody, ko upload dc file
//    @PostMapping("/json")
//    public ResponseDTO<Void> createNewJson(
//            @RequestBody @Valid DepartmentDTO departmentDTO)
//    {
//        departmentService.create(departmentDTO);
//        return ResponseDTO.<Void>builder().status(200).msg("create success").build();
//    }
//
//    // xoa department
    @DeleteMapping("/delete")
    public ResponseDTO<Void> delete(@RequestParam("id") int id) {
        scoreService.delete(id);
        return ResponseDTO.<Void>builder().status(200).msg("delete success").build();
    }
    //
    @GetMapping("/")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseDTO<ScoreDTO> get(@RequestParam("id") int id) {
        return ResponseDTO.<ScoreDTO>builder().status(200).data(scoreService.getById(id)).build();
    }
    //
//    // ham sua
    @PutMapping("/edit")
    public ResponseDTO<ScoreDTO> edit(@RequestBody ScoreDTO scoreDTO){
        scoreService.update(scoreDTO);
        return ResponseDTO.<ScoreDTO>builder().status(200).data(scoreDTO).build();
    }
    //
//    // tim kiem & phan trang
    @PostMapping("/search")
    public ResponseDTO<PageDTO<List<ScoreDTO>>> search(@RequestBody @Valid SearchScoreDTO searchDTO)
    {

        PageDTO<List<ScoreDTO>> pageDTO = scoreService.search(searchDTO);
        return ResponseDTO.<PageDTO<List<ScoreDTO>>>builder().status(200).msg("success").data(pageDTO).build();
    }

    // ham thong ke
    @GetMapping("/avg-score-by-course")
    public ResponseDTO<List<AvgScoreByCourse>> avgScoreByCourse() {
        return ResponseDTO.<List<AvgScoreByCourse>>builder().status(200).msg("success").data(scoreService.avgScoreByCourses()).build();
    }

}
