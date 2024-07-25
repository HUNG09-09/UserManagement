package project.java.qlsv.service;

import jakarta.persistence.NoResultException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import project.java.qlsv.dto.*;
import project.java.qlsv.entity.Department;
import project.java.qlsv.entity.Student;
import project.java.qlsv.entity.User;
import project.java.qlsv.repository.StudentRepo;
import project.java.qlsv.repository.UserRepo;

import java.util.List;
import java.util.stream.Collectors;

public interface StudentService {
    void create(StudentDTO studentDTO);
    void update(StudentDTO studentDTO);
    void delete(int id);
    StudentDTO getById(int id);

    List<StudentDTO> getAll();

    PageDTO<List<StudentDTO>> search(SearchDTO searchDTO);
}

@Service
class StudentServiceImpl implements StudentService {
    @Autowired
    StudentRepo studentRepo;

    @Autowired
    UserRepo userRepo;

    @Override
    @Transactional
    public void create(StudentDTO studentDTO) {

        // ko dung cascade o student onetoone vs User
        // thi phai tao user truoc, luu user sau do moi set student
//        User user = new ModelMapper().map(studentDTO.getUser(), User.class);
//        userRepo.save(user);
//        Student student = new Student();
//        student.setStudentId(user.getId());
//        student.setStudentCode(studentDTO.getStudentCode());
//        studentRepo.save(student);

        // neu dung cascade thi
//        User user = new ModelMapper().map(studentDTO.getUser(), User.class);
//        Student student = new Student();
//        student.setUser(user); // set dong thoi ca user va student
//        student.setStudentCode(studentDTO.getStudentCode());
//        studentRepo.save(student);
        // hoac co the su dung thang modelmapper luon
        Student student = new ModelMapper().map(studentDTO, Student.class);
        studentRepo.save(student);

    }


    @Override
    @Transactional
    public void update(StudentDTO studentDTO) {
        Student student = studentRepo.findById(studentDTO.getUser().getId()).orElse(null);
        if (student != null) {
            // set thuoc tinh
            student.setStudentCode(studentDTO.getStudentCode());
            // update
            studentRepo.save(student);
        }
    }

    @Override
    @Transactional
    public void delete(int id) {
        studentRepo.deleteById(id);
    }

    @Override
    public StudentDTO getById(int id) {
        Student student = studentRepo.findById(id).orElseThrow(NoResultException::new);
        return convert(student);
    }

    @Override
    public List<StudentDTO> getAll() {
        List<Student> studentList = studentRepo.findAll();
        return studentList.stream().map(u -> convert(u)).collect(Collectors.toList());
    }

    private StudentDTO convert(Student student) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return new ModelMapper().map(student, StudentDTO.class);
    }

    @Override
    public PageDTO<List<StudentDTO>> search(SearchDTO searchDTO) {
        // sap xep theo thu tu ten abc
        Sort sortBy = Sort.by("studentCode").ascending();


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
        Page<Student> page = studentRepo.searchCode("%" + searchDTO.getKeyword() + "%", pageRequest);

        // convert sang DTO de tra ve view
        PageDTO<List<StudentDTO>> pageDTO = new PageDTO<>();
        pageDTO.setTotalPages(page.getTotalPages());
        pageDTO.setTotalElements(page.getTotalElements());

        List<StudentDTO> studentDTOs = page.get().map(u -> convert(u)).collect(Collectors.toList());

        pageDTO.setData(studentDTOs);
        return  pageDTO;
    }
}