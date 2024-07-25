package project.java.qlsv.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.java.qlsv.entity.Course;
import project.java.qlsv.entity.Department;


public interface CourseRepo extends JpaRepository<Course, Integer> {
    @Query("SELECT c FROM Course c WHERE c.name LIKE :x")
    Page<Course> searchName(@Param("x") String name, Pageable pageable);
}
