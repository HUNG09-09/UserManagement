package project.java.qlsv.service;

import jakarta.persistence.NoResultException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import project.java.qlsv.dto.*;
import project.java.qlsv.entity.Course;
import project.java.qlsv.entity.Score;
import project.java.qlsv.entity.Student;
import project.java.qlsv.repository.CourseRepo;
import project.java.qlsv.repository.ScoreRepo;
import project.java.qlsv.repository.StudentRepo;

import java.util.List;
import java.util.stream.Collectors;

public interface ScoreService {
    void create(ScoreDTO scoreDTO);
    void update(ScoreDTO scoreDTO);
    void delete(int id);
    ScoreDTO getById(int id);
    PageDTO<List<ScoreDTO>> search(SearchScoreDTO searchDTO);

    List<AvgScoreByCourse> avgScoreByCourses();
}
@Service
class ScoreServiceImpl implements ScoreService {

    @Autowired
    ScoreRepo scoreRepo;

    @Autowired
    StudentRepo studentRepo;

    @Autowired
    CourseRepo courseRepo;

    @Override
    @Transactional
    public void create(ScoreDTO scoreDTO) {
        Score score = new ModelMapper().map(scoreDTO, Score.class);
        scoreRepo.save(score);
    }

    @Override
    @Transactional
    public void update(ScoreDTO scoreDTO) {
        Score score = scoreRepo.findById(scoreDTO.getId()).orElseThrow(NoResultException::new);
        if (score != null) {
            // set thuoc tinh
            score.setScore(scoreDTO.getScore());
            Student student = studentRepo.findById(scoreDTO.getStudent().getUser().getId()).orElseThrow(NoResultException::new);
            Course course = courseRepo.findById(scoreDTO.getCourse().getId()).orElseThrow(NoResultException::new);
            score.setCourse(course);
            score.setStudent(student);
            // update
            scoreRepo.save(score);
        }
    }



    @Override
    @Transactional
    public void delete(int id) {
        scoreRepo.deleteById(id);
    }

    @Override
    public ScoreDTO getById(int id) {
        Score score = scoreRepo.findById(id).orElseThrow(NoResultException::new);
        return convert(score);
    }

    private ScoreDTO convert(Score score) {
        return new ModelMapper().map(score, ScoreDTO.class);
    }


    @Override
    public PageDTO<List<ScoreDTO>> search(SearchScoreDTO searchDTO) {
            // sap xep theo thu tu ten abc
            Sort sortBy = Sort.by("id").ascending();


            // sap xep theo thuoc tinh
            // neu rong hoac k nhap gi thi sap xep theo asc
            if (StringUtils.hasText(searchDTO.getSortedField())) {
                sortBy = Sort.by(searchDTO.getSortedField()).ascending();
            }

            if (searchDTO.getKeyword() == null)
                searchDTO.setKeyword("");
            if (searchDTO.getCurrentPage() == null)
                searchDTO.setCurrentPage(0);
            if (searchDTO.getSize() == null)
                searchDTO.setSize(5);

            // thiet lap tim kiem theo trang
            PageRequest pageRequest = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getSize(), sortBy);
            Page<Score> page = null;
            if (searchDTO.getCourseId() != null) {
                page = scoreRepo.searchByCourse(searchDTO.getCourseId(),pageRequest);
            } else if (searchDTO.getStudentId() != null) {
                page = scoreRepo.searchByStudent(searchDTO.getStudentId(),pageRequest);
            } else {
                page = scoreRepo.findAll(pageRequest);
            }

            // convert sang DTO de tra ve view
            PageDTO<List<ScoreDTO>> pageDTO = new PageDTO<>();
            pageDTO.setTotalPages(page.getTotalPages());
            pageDTO.setTotalElements(page.getTotalElements());

            List<ScoreDTO> scoreDTOs = page.get().map(u -> convert(u)).collect(Collectors.toList());

            pageDTO.setData(scoreDTOs);
            return  pageDTO;
    }

    @Override
    public List<AvgScoreByCourse> avgScoreByCourses() {
        return scoreRepo.avgScoreByCourse();
    }
}