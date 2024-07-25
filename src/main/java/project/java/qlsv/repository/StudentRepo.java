package project.java.qlsv.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.java.qlsv.entity.Department;
import project.java.qlsv.entity.Student;

public interface StudentRepo extends JpaRepository<Student,Integer> {
    @Query("SELECT s FROM Student s WHERE s.studentCode LIKE :x")
    Page<Student> searchCode(@Param("x") String studentCode, Pageable pageable);

}
