package edu.virginia.cs.review;

import java.util.List;

public interface DatabaseManager {
    /**
     * Establishes the database connection. Must be called before any other
     * methods are called.
     *
     * @throws IllegalStateException if the Manager is already connected
     */
    void connect();

    /**
     * Creates the tables Students, Courses, and Reviews in the database. Throws
     * an IllegalStateException if the tables already exist.
     *
     * This *does not* populate the tables Data.
     *
     * @throws IllegalStateException if the tables already exist
     * @throws IllegalStateException if the Manager hasn't connected yet
     */
    void createTables();

    /**
     * Empties all of the tables, but does not delete the tables. I.e.,
     * the table structure is still there, but the data content is emptied.
     *
     * @throws IllegalStateException if the tables don't exist.
     * @throws IllegalStateException if the Manager hasn't connected yet
     */
    void clear();

    /**
     * Deletes the tables Students, Courses, and Reviews from the database. This
     * removes both the data and the tables themselves.
     *
     * @throws IllegalStateException if the tables don't exist
     * @throws IllegalStateException if the Manager hasn't connected yet
     */
    void deleteTables();

    /**
     * Add the student to the Students table in the Database.
     *
     * @throws IllegalStateException if Students table doesn't exist
     * @throws IllegalArgumentException if you add a student that is already
     * in the database.
     * @throws IllegalStateException if the Manager hasn't connected yet
     */
    void addStudentCreateAccount(Student student);

    /**
     * Add the student to the Students table in the Database.
     *
     * @throws IllegalStateException if Students table doesn't exist
     * @throws IllegalArgumentException if student is not already in
     * the database.
     * @throws IllegalStateException if the Manager hasn't connected yet
     */
    void addStudentLogIn(Student student);

    /**
     * Add the review to the Review table in the Database.
     *
     * @throws IllegalStateException if Review table doesn't exist
     * @throws IllegalArgumentException if student has already made a review
     * on that course.
     * @throws IllegalStateException if the Manager hasn't connected yet
     */
    void addReview(Student student, Course course, Review review);

    /**
     * Gets reviews for courseName
     *
     * @throws IllegalStateException if Review table doesn't exist
     * @throws IllegalStateException if the Manager hasn't connected yet
     */
    List<Review> getReviews(String courseName);

    /**
     * Gets all students
     *
     * @throws IllegalStateException if Students table doesn't exist
     * @throws IllegalStateException if the Manager hasn't connected yet
     */
    List<Student> getStudents();
}
