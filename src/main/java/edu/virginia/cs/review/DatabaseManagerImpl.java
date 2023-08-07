package edu.virginia.cs.review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManagerImpl implements DatabaseManager {
    public static String REVIEW_DATABASE_PATH = "Reviews.sqlite3";
    public Connection connection;
    boolean connected;
    boolean tablesPopulated;
    @Override
    public void connect() {
        if(connected)
        {
            throw new IllegalStateException("ERROR: Manager is already connected.");
        }
        try {
            connected = true;
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" +
                    REVIEW_DATABASE_PATH);
        } catch (ClassNotFoundException | SQLException e) {
            throw new IllegalStateException("ERROR: Class not found.");
        }
    }

    @Override
    public void createTables() {
        try{
            if(connected){
                String studentsTable = "CREATE TABLE STUDENTS (ID INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR(150) NOT NULL, Password VARCHAR(50) NOT NULL);";
                String coursesTable = "CREATE TABLE COURSES (ID INTEGER PRIMARY KEY AUTOINCREMENT, Department VARCHAR(5) NOT NULL, Catalog_Number INT(5) NOT NULL);";
                String reviewsTable = "CREATE TABLE REVIEWS (ID INTEGER PRIMARY KEY AUTOINCREMENT, StudentID INT NOT NULL, CourseID INT NOT NULL, Message VARCHAR(100) NOT NULL, Rating INT(1) NOT NULL, FOREIGN KEY (StudentID) REFERENCES STUDENTS(ID) ON DELETE CASCADE, FOREIGN KEY (CourseID) REFERENCES COURSES(ID) ON DELETE CASCADE);";
                Statement students = connection.createStatement();
                Statement courses = connection.createStatement();
                Statement reviews = connection.createStatement();
                students.executeUpdate(studentsTable);
                courses.executeUpdate(coursesTable);
                reviews.executeUpdate(reviewsTable);
                students.close();
                courses.close();
                reviews.close();
            } else {
                throw new IllegalStateException("ERROR: Manager has not connected.");
            }
        } catch (SQLException e){
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void clear() {
        try{
            if(connected){
                String clearStudents = "DELETE FROM STUDENTS;";
                String clearCourses = "DELETE FROM COURSES;";
                String clearReviews = "DELETE FROM REVIEWS;";
                Statement students = connection.createStatement();
                Statement courses = connection.createStatement();
                Statement reviews = connection.createStatement();
                students.executeUpdate(clearStudents);
                courses.executeUpdate(clearCourses);
                reviews.executeUpdate(clearReviews);
                students.close();
                courses.close();
                reviews.close();
                tablesPopulated = false;
            } else {
                throw new IllegalStateException("ERROR: Manager has not connected.");
            }
        } catch (SQLException e){
            throw new IllegalStateException("ERROR: Tables don't exist.");
        }
    }

    @Override
    public void deleteTables() {
        try{
            if(connected){
                String deleteStudents = "DROP TABLE IF EXISTS STUDENTS;";
                String deleteCourses = "DROP TABLE IF EXISTS COURSES;";
                String deleteReviews = "DROP TABLE IF EXISTS REVIEWS;";
                Statement students = connection.createStatement();
                Statement courses = connection.createStatement();
                Statement reviews = connection.createStatement();
                students.executeUpdate(deleteStudents);
                courses.executeUpdate(deleteCourses);
                reviews.executeUpdate(deleteReviews);
                students.close();
                courses.close();
                reviews.close();
            } else {
                throw new IllegalStateException("ERROR: Manager has not connected.");
            }
        } catch (SQLException e){
            throw new IllegalStateException("ERROR: Tables don't exist.");
        }
    }

    @Override
    public void addStudentCreateAccount(Student student) {
        try{
            if(connected){

                String studentName = student.getName().replace("'", "''");
                String studentPassword = student.getPassword().replace("'", "''");
                String insertStudent = "INSERT INTO STUDENTS (Name, Password) VALUES ('" + studentName + "', '"
                        + studentPassword + "');";
                Statement insertStatement = connection.createStatement();
                insertStatement.executeUpdate(insertStudent);
                insertStatement.close();
                tablesPopulated = true;
            } else {
                throw new IllegalStateException("ERROR: Manager has not connected.");
            }
        } catch (SQLException e){
            throw new IllegalStateException(e);
        }
    }
    @Override
    public void addStudentLogIn(Student student) {
        if(connected){
            tablesPopulated = true;
        } else {
            throw new IllegalStateException("ERROR: Manager has not connected.");
        }
    }
    @Override
    public void addReview(Student student, Course course, Review review) {
        try{
            if(connected){
                String findCourse = "SELECT * FROM COURSES WHERE Department = '" + course.getDepartment() + "' AND Catalog_Number = " + course.getCatalogNum() + ";";
                Statement findCourseStatement = connection.createStatement();
                ResultSet findCourseResultSet = findCourseStatement.executeQuery(findCourse);
                if(!findCourseResultSet.next()){
                    String insertCourse = "INSERT INTO COURSES (Department, Catalog_Number) VALUES ('" + course.getDepartment() + "', '" + course.getCatalogNum() + "');";
                        Statement insertCourseStatement = connection.createStatement();
                        insertCourseStatement.executeUpdate(insertCourse);
                    }
                    String reviewMessage = review.getMessage().replace("'", "''");
                    String insertReview = "INSERT INTO REVIEWS (StudentID, CourseID, Message, Rating) VALUES ('" + student.getName() + "', '"
                            + course.getName() + "', '" + reviewMessage + "', " + review.rating + ");";
                    Statement insertReviewStatement = connection.createStatement();
                    insertReviewStatement.executeUpdate(insertReview);
                    insertReviewStatement.close();
                    tablesPopulated = true;
            } else {
                throw new IllegalStateException("ERROR: Manager has not connected.");
            }
        } catch (SQLException e){
            throw new IllegalStateException("ERROR: Tables don't exist.");
        }
    }
    @Override
    public List<Review> getReviews(String courseName){
        try{
            List<Review> reviewList;
            if(connected) {
                reviewList = new ArrayList<>();
                String getReviews = "SELECT ID, StudentID, CourseID, Message, Rating FROM REVIEWS;";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(getReviews);
                while (resultSet.next()) {
                    String courseID = resultSet.getString("CourseID");
                    String[] splitString = courseID.split(" ");
                    String department = splitString[0];
                    int catalogNum = Integer.parseInt(splitString[1]);
                    String studentName = resultSet.getString("StudentID");
                    String message = resultSet.getString("Message");
                    int rating = resultSet.getInt("Rating");
                    Course course = new Course(department, catalogNum);
                    Review review = new Review(studentName, course, message, rating);
                    if(course.getName().equals(courseName)){
                        reviewList.add(review);
                    }
                }
                statement.close();
                resultSet.close();
            } else {
                throw new IllegalStateException("ERROR: Manager has not connected.");
            }
            return reviewList;
        } catch (SQLException e){
            throw new IllegalStateException(e);
        }
    }
    @Override
    public List<Student> getStudents(){
        try{
            List<Student> studentList;
            if(connected) {
                studentList = new ArrayList<>();
                String getStudents = "SELECT ID, Name, Password FROM Students;";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(getStudents);
                while (resultSet.next()) {
                    String name = resultSet.getString("Name");
                    String password = resultSet.getString("Password");
                    Student student = new Student(name, password);
                    studentList.add(student);
                }
                statement.close();
                resultSet.close();
            } else {
                throw new IllegalStateException("ERROR: Manager has not connected.");
            }
            return studentList;
        } catch (SQLException e){
            throw new IllegalStateException(e);
        }
    }
}
