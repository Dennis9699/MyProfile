package de.hnu.eae.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(ConversationId.class)
public class Conversation {
    @Id
    private long matrNr;
    @Id
    private long courseId;
    @Column(length=100000)
    private String content;
    

    public Conversation(long matrNr, long courseId, String content) {
        this.matrNr = matrNr;
        this.courseId = courseId;
        this.content = content;
    }

    public Conversation() {
    }

    public long getMatrNr() {
        return matrNr;
    }

    public void setMatrNr(long matrNr) {
        this.matrNr = matrNr;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    

}
