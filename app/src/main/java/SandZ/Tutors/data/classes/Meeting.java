package SandZ.Tutors.data.classes;

import java.util.Date;

public class Meeting {
    private Date date;
    private String link;
    private String student;
    private String teacher;

    // Konstruktor
    public Meeting(Date date, String link, String student, String teacher) {
        this.date = date;
        this.link = link;
        this.student = student;
        this.teacher = teacher;
    }

    // Gettery i settery
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "date=" + date +
                ", link='" + link + '\'' +
                ", student='" + student + '\'' +
                ", teacher='" + teacher + '\'' +
                '}';
    }
}