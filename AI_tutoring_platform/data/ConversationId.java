package de.hnu.eae.data;

import java.io.Serializable;


public class ConversationId implements Serializable{
    private long matrNr;
    private long courseId;


    public ConversationId(long matrNr, long courseId) {
        this.matrNr = matrNr;
        this.courseId = courseId;
    }

}
