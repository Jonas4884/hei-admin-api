package school.hei.haapi.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.hei.haapi.model.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    List<Course> getByMainTeacherId(String UserId, Pageable pageable);

    List<Course> findAllByNameContainingIgnoreCaseAndCodeContainingIgnoreCaseAndCreditsAndMainTeacherFirstNameContainingIgnoreCaseAndMainTeacherLastNameContainingIgnoreCase
            (String name, String code, Integer credits, String teacherFirstName, String teacherLastName, Pageable pageable);
    List<Course> findAllByNameContainingIgnoreCaseAndCodeContainingIgnoreCaseAndMainTeacherFirstNameContainingIgnoreCaseAndMainTeacherLastNameContainingIgnoreCase
            (String name, String code, String teacherFirstName, String teacherLastName, Pageable pageable);
}
