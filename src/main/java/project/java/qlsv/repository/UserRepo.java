package project.java.qlsv.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.java.qlsv.entity.User;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Integer> {

    // tim theo username
    User findByUsername(String username);

    // tim theo ten
    // phan trang
    Page<User> findByName(String s, Pageable page);

    // tim theo ten
    @Query("SELECT u FROM User u WHERE u.name LIKE :x")
    Page<User> searchByName(@Param("x") String s, Pageable page);


    // tim theo ten hoac username
    @Query("SELECT u FROM User u WHERE u.name LIKE :x " + "OR u.username LIKE :x")
    List<User> searchByNameAndUsername(@Param("x") String s);

    // xoa user
    @Modifying
    @Query("DELETE FROM User u WHERE u.username = :x")
    int deleteUser(@Param("x") String username);

    @Query("SELECT u FROM User u WHERE " + "MONTH(u.birthdate) = :month AND DAY(u.birthdate) = :date")
    List<User> searchByBirthday(@Param("date") int date, @Param("month") int month);
}
