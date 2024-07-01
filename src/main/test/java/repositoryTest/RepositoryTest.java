package repositoryTest;


import jakarta.persistence.EntityManagerFactory;
import org.example.configs.SpringAppConfig;
import org.example.models.Course;
import org.example.models.Student;
import org.example.models.Teacher;
import org.example.repository.CourseRepository;
import org.example.repository.StudentRepository;
import org.example.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringAppConfig.class)
@Testcontainers
public class RepositoryTest {

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @BeforeAll
    static void setup() throws Exception {

        mysqlContainer.start();
        System.setProperty("url", mysqlContainer.getJdbcUrl());
    }

    @BeforeEach
    void setUpEach() {
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.db.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.db.username", mysqlContainer::getUsername);
        registry.add("spring.db.password", mysqlContainer::getPassword);
        registry.add("spring.db.driver", mysqlContainer::getDriverClassName);
    }

    @Autowired
    private DataSource dataSource;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private JpaTransactionManager transactionManager;

    @Test
    public void testDataSourceCreation() {
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof com.zaxxer.hikari.HikariDataSource);
    }

    @Test
    public void testEntityManagerFactoryCreation() {
        assertNotNull(entityManagerFactory);
    }

    @Test
    public void testTransactionManagerCreation() {
        assertNotNull(transactionManager);
        assertEquals(entityManagerFactory, transactionManager.getEntityManagerFactory());
    }

    // StudentsReposTests
    @Test
    void testStudentFindByEmail() {
        Student student = new Student();
        student.setName("Петр");
        student.setEmail("1s@example.com");
        studentRepository.save(student);

        Optional<Student> foundStudent = studentRepository.findByEmail("1s@example.com");

        assertTrue(foundStudent.isPresent());
        assertEquals("Петр", foundStudent.get().getName());
    }

    @Test
    void testStudentFindById() {
        Student student = new Student();
        student.setName("Пётр");
        Student savedStudent = studentRepository.save(student);

        Optional<Student> foundStudent = studentRepository.findById(savedStudent.getId());

        assertTrue(foundStudent.isPresent());
        assertEquals(savedStudent.getId(), foundStudent.get().getId());
        assertEquals("Пётр", foundStudent.get().getName());
    }


    @Test
    void testStudentFindAll() {
        Student student1 = new Student();
        student1.setName("Петр");
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setName("Василий");
        studentRepository.save(student2);

        List<Student> allStudents = studentRepository.findAll();

        assertNotNull(allStudents);
        assertEquals(2, allStudents.size());
        assertTrue(allStudents.stream().anyMatch(s -> s.getName().equals("Петр")));
        assertTrue(allStudents.stream().anyMatch(s -> s.getName().equals("Василий")));
    }

    @Test
    void testStudentSave() {
        Student student = new Student();
        student.setName("Петр");

        Student savedStudent = studentRepository.save(student);

        assertNotNull(savedStudent);
        assertNotNull(savedStudent.getId());
        assertEquals("Петр", savedStudent.getName());

        Optional<Student> foundStudent = studentRepository.findById(savedStudent.getId());
        assertTrue(foundStudent.isPresent());
        assertEquals("Петр", foundStudent.get().getName());
    }

    @Test
    void testStudentDeleteById() {
        Student student = new Student();
        student.setName("Пётр");
        Student savedStudent = studentRepository.save(student);

        studentRepository.deleteById(savedStudent.getId());

        Optional<Student> foundStudent = studentRepository.findById(savedStudent.getId());
        assertFalse(foundStudent.isPresent());
    }

    //courseRepos tests


    @Test
    void testCourseFindByName() {
        Course course = new Course();
        course.setName("Java");
        courseRepository.save(course);

        Optional<Course> foundCourse = courseRepository.findByName("Java");

        assertTrue(foundCourse.isPresent());
        assertEquals("Java", foundCourse.get().getName());
    }

    @Test
    void testCourseFindById() {
        Course course = new Course();
        course.setName("Java");
        Course savedCourse = courseRepository.save(course);

        Optional<Course> foundCourse = courseRepository.findById(savedCourse.getId());

        assertTrue(foundCourse.isPresent());
        assertEquals(savedCourse.getId(), foundCourse.get().getId());
        assertEquals("Java", foundCourse.get().getName());
    }

    @Test
    void testCourseFindByNameNotFound() {
        Optional<Course> foundCourse = courseRepository.findByName("Non-existent Course");

        assertFalse(foundCourse.isPresent());
    }

    @Test
    void testCourseFindAll() {
        Course course1 = new Course();
        course1.setName("Java");
        courseRepository.save(course1);

        Course course2 = new Course();
        course2.setName("Python");
        courseRepository.save(course2);

        List<Course> allCourses = courseRepository.findAll();

        assertNotNull(allCourses);
        assertEquals(2, allCourses.size());
        assertTrue(allCourses.stream().anyMatch(c -> c.getName().equals("Java")));
        assertTrue(allCourses.stream().anyMatch(c -> c.getName().equals("Python")));
    }

    @Test
    void testCourseSave() {
        Course course = new Course();
        course.setName("Java");

        Course savedCourse = courseRepository.save(course);

        assertNotNull(savedCourse);
        assertNotNull(savedCourse.getId());
        assertEquals("Java", savedCourse.getName());

        Optional<Course> foundCourse = courseRepository.findById(savedCourse.getId());
        assertTrue(foundCourse.isPresent());
        assertEquals("Java", foundCourse.get().getName());
    }

    @Test
    void testCourseDeleteById() {
        Course course = new Course();
        course.setName("Java");
        Course savedCourse = courseRepository.save(course);

        courseRepository.deleteById(savedCourse.getId());

        Optional<Course> foundCourse = courseRepository.findById(savedCourse.getId());
        assertFalse(foundCourse.isPresent());
    }

    @Test
    void testTeacherFindByEmail() {
        Teacher teacher = new Teacher();
        teacher.setName("Иван Петрович");
        teacher.setEmail("teacher@example.com");
        teacherRepository.save(teacher);

        Optional<Teacher> foundTeacher = teacherRepository.findByEmail("teacher@example.com");

        assertTrue(foundTeacher.isPresent());
        assertEquals("Иван Петрович", foundTeacher.get().getName());
    }

    @Test
    void testTeacherFindById() {
        Teacher teacher = new Teacher();
        teacher.setName("Иван Петрович");
        Teacher savedTeacher = teacherRepository.save(teacher);

        Optional<Teacher> foundTeacher = teacherRepository.findById(savedTeacher.getId());

        assertTrue(foundTeacher.isPresent());
        assertEquals(savedTeacher.getId(), foundTeacher.get().getId());
        assertEquals("Иван Петрович", foundTeacher.get().getName());
    }

    @Test
    void testTeacherFindAll() {
        Teacher teacher1 = new Teacher();
        teacher1.setName("Иван Петрович");
        teacherRepository.save(teacher1);

        Teacher teacher2 = new Teacher();
        teacher2.setName("Сергей Иванов");
        teacherRepository.save(teacher2);

        List<Teacher> allTeachers = teacherRepository.findAll();

        assertNotNull(allTeachers);
        assertEquals(2, allTeachers.size());
        assertTrue(allTeachers.stream().anyMatch(t -> t.getName().equals("Иван Петрович")));
        assertTrue(allTeachers.stream().anyMatch(t -> t.getName().equals("Сергей Иванов")));
    }

    @Test
    void testTeacherSave() {
        Teacher teacher = new Teacher();
        teacher.setName("Иван Петрович");

        Teacher savedTeacher = teacherRepository.save(teacher);

        assertNotNull(savedTeacher);
        assertNotNull(savedTeacher.getId());
        assertEquals("Иван Петрович", savedTeacher.getName());

        Optional<Teacher> foundTeacher = teacherRepository.findById(savedTeacher.getId());
        assertTrue(foundTeacher.isPresent());
        assertEquals("Иван Петрович", foundTeacher.get().getName());
    }

    @Test
    void testTeacherDeleteById() {
        Teacher teacher = new Teacher();
        teacher.setName("Иван Петрович");
        Teacher savedTeacher = teacherRepository.save(teacher);

        teacherRepository.deleteById(savedTeacher.getId());

        Optional<Teacher> foundTeacher = teacherRepository.findById(savedTeacher.getId());
        assertFalse(foundTeacher.isPresent());
    }
}