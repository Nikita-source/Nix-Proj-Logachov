package models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "course")
    private List<Group> groups;

    @OneToMany(mappedBy = "course")
    private List<Topic> topics;

    public Course() {
        this.groups = new ArrayList<>();
        this.topics = new ArrayList<>();
    }

    public Course(String name) {
        this.name = name;
        this.groups = new ArrayList<>();
        this.topics = new ArrayList<>();
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

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public void addGroup(Group group) {
        groups.add(group);
        group.setCourse(this);
    }

    public void addTopic(Topic topic) {
        topics.add(topic);
        topic.setCourse(this);
    }
}
