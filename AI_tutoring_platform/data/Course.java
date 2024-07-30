package de.hnu.eae.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Course {
    @Id @GeneratedValue
    private long courseId;
    @Column(length=30)
    private String name;
    @Column(length=30)
    private String lecturer;
    @Column(length=5000)
    private String content;
    

    public Course(long courseId, String name, String lecturer, String content) {
        this.courseId = courseId;
        this.name = name;
        this.lecturer = lecturer;
        this.content = content;
    }

    public Course() {
    }

    public long getCourseId() {
        return courseId;
    }


    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getLecturer() {
        return lecturer;
    }


    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }

    
}
