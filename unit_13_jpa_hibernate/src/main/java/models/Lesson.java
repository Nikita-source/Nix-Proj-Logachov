package models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    @Access(AccessType.PROPERTY)
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    @Access(AccessType.PROPERTY)
    private Group group;

    @OneToMany(mappedBy = "lesson")
    private List<Mark> marks;

    @Column(name = "lesson_time")
    private LocalDateTime lessonTime;

    public Lesson() {
        this.marks = new ArrayList<>();
    }

    public Lesson(String name, Topic topic, Group group, LocalDateTime lessonTime) {
        this.name = name;
        this.topic = topic;
        this.group = group;
        this.marks = new ArrayList<>();
        this.lessonTime = lessonTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }

    public LocalDateTime getLessonTime() {
        return lessonTime;
    }

    public void setLessonTime(LocalDateTime lessonTime) {
        this.lessonTime = lessonTime;
    }

    public void addMark(Mark mark) {
        marks.add(mark);
        mark.setLesson(this);
    }
}
