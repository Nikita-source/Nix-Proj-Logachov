import models.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;

public class DbService {
    public void start(Long id) {
        Configuration configuration = new Configuration().configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            EntityManager entityManager = sessionFactory.createEntityManager();
            createEntities(entityManager);
            try {
                findNextLesson(entityManager, id);
            } catch (Exception e) {
                throw new RuntimeException("Id not found");
            }
        }
    }

    private void findNextLesson(EntityManager entityManager, Long id) {
        try {
            entityManager.getTransaction().begin();
            Student student = entityManager.find(Student.class, id);
            TypedQuery<Lesson> queryLesson = entityManager.createQuery("from Lesson l where l.group = ?1 and l.lessonTime >= ?2 order by lessonTime asc", Lesson.class)
                    .setParameter(1, student.getGroup()).setParameter(2, LocalDateTime.now()).setMaxResults(1);
            Lesson lesson = queryLesson.getSingleResult();

            if (student != null && lesson != null) {
                System.out.println(printStudent(student) + " next lesson is: " + printLesson(lesson));
            } else {
                System.out.println("Student or lesson not found");
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    private void createEntities(EntityManager entityManager) {
        try {
            entityManager.getTransaction().begin();

            Student student = new Student();
            student.setName("Student_1");

            Group group = new Group();
            group.setName("Group_1");
            group.addStudent(student);

            Teacher teacher = new Teacher();
            teacher.setName("Teacher_1");
            teacher.addGroup(group);

            Course course = new Course();
            course.setName("Course_1");
            course.addGroup(group);

            Topic topic = new Topic();
            topic.setName("Topic_1");
            course.addTopic(topic);

            Lesson lesson = new Lesson();
            lesson.setName("Lesson_1");
            topic.addLesson(lesson);
            lesson.setGroup(group);
            lesson.setLessonTime(LocalDateTime.now().plusDays(2));

            Mark mark = new Mark();
            mark.setMark(10);
            student.addMark(mark);
            lesson.addMark(mark);

            entityManager.persist(student);
            entityManager.persist(group);
            entityManager.persist(teacher);
            entityManager.persist(course);
            entityManager.persist(topic);
            entityManager.persist(lesson);
            entityManager.persist(mark);


            student = new Student();
            student.setName("Student_2");

            group.addStudent(student);

            topic = new Topic();
            topic.setName("Topic_2");
            course.addTopic(topic);

            lesson = new Lesson();
            lesson.setName("Lesson_2");
            topic.addLesson(lesson);
            lesson.setGroup(group);
            lesson.setLessonTime(LocalDateTime.now().plusDays(1));

            mark = new Mark();
            mark.setMark(8);
            student.addMark(mark);
            lesson.addMark(mark);

            entityManager.persist(student);
            entityManager.persist(topic);
            entityManager.persist(lesson);
            entityManager.persist(mark);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    private String printStudent(Student student) {
        return "Student{" +
                "id=" + student.getId() +
                ", name='" + student.getName() + '\'' +
                ", group=" + student.getGroup().getName() +
                '}';
    }

    private String printLesson(Lesson lesson) {
        return "Lesson{" +
                "id=" + lesson.getId() +
                ", name='" + lesson.getName() + '\'' +
                ", topic=" + lesson.getTopic().getName() +
                ", group=" + lesson.getGroup().getName() +
                ", teacher=" + lesson.getGroup().getTeacher().getName() +
                ", lessonTime=" + lesson.getLessonTime() +
                '}';
    }
}
