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
import project.java.qlsv.dto.CourseDTO;
import project.java.qlsv.dto.DepartmentDTO;
import project.java.qlsv.dto.PageDTO;
import project.java.qlsv.dto.SearchDTO;
import project.java.qlsv.entity.Course;
import project.java.qlsv.entity.Department;
import project.java.qlsv.repository.CourseRepo;

import java.util.List;
import java.util.stream.Collectors;

public interface CourseService {
    void create(CourseDTO courseDTO);
    void update(CourseDTO courseDTO);
    void delete(int id);
    CourseDTO getById(int id);
    PageDTO<List<CourseDTO>> search(SearchDTO searchDTO);
}

@Service
class CourseServiceImpl implements CourseService {

    @Autowired
    CourseRepo courseRepo;

    @Override
    @Transactional
    public void create(CourseDTO courseDTO) {
        Course course = new ModelMapper().map(courseDTO, Course.class);
        courseRepo.save(course);
    }

    @Override
    @Transactional
    public void update(CourseDTO courseDTO) {
        Course course = courseRepo.findById(courseDTO.getId()).orElse(null);
        if (course != null) {
            course.setName(courseDTO.getName());
            courseRepo.save(course);
        }
    }

    @Override
    @Transactional
    public void delete(int id) {
        courseRepo.deleteById(id);
    }

    @Override
    public CourseDTO getById(int id) {
        Course course = courseRepo.findById(id).orElseThrow(NoResultException::new);
        return convert(course);
    }

    private CourseDTO convert(Course course) {
        return new ModelMapper().map(course, CourseDTO.class);
    }


    @Override
    public PageDTO<List<CourseDTO>> search(SearchDTO searchDTO) {
            // sap xep theo thu tu ten abc
            Sort sortBy = Sort.by("name").ascending();


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
            Page<Course> page = courseRepo.searchName("%" + searchDTO.getKeyword() + "%", pageRequest);

            // convert sang DTO de tra ve view
            PageDTO<List<CourseDTO>> pageDTO = new PageDTO<>();
            pageDTO.setTotalPages(page.getTotalPages());
            pageDTO.setTotalElements(page.getTotalElements());

            List<CourseDTO> courseDTOs = page.get().map(u -> convert(u)).collect(Collectors.toList());

            pageDTO.setData(courseDTOs);
            return  pageDTO;
    }
}