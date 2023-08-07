package edu.virginia.cs.review;

public class Review {
    Course course;
    String message;
    int rating;
    String studentName;
    public Review(String studentName, Course course, String message, int rating){
        this.studentName = studentName;
        this.course = course;
        this.message = message;
        this.rating = rating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(Course course) {
        this.studentName = studentName;
    }
}
