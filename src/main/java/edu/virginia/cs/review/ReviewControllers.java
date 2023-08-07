package edu.virginia.cs.review;

import java.util.List;
import java.util.Scanner;

public class ReviewControllers {
    static DatabaseManagerImpl databaseManager;
    static Student student;
    static Course course;
    static Review review;
    public static void main(String[] args){
        setUp();
        initialPrompt();
        mainMenuPrompt();
    }
    public static void setUp(){
        databaseManager = new DatabaseManagerImpl();
        databaseManager.connect();
        //databaseManager.deleteTables();
        //databaseManager.createTables();
    }
    public static void initialPrompt(){
        Scanner logInScanner = new Scanner(System.in);
        System.out.println();
        System.out.print("Log in (1) or create account (2): ");
        String logInName = logInScanner.nextLine();
        if(logInName.equals("1")){
            logInPrompt();
            mainMenuPrompt();
        } else if (logInName.equals("2")){
            createAccountPrompt();
            mainMenuPrompt();
        } else{
            System.out.println("Invalid response. Must enter 1 or 2.");
            initialPrompt();
        }
    }
    public static void logInPrompt(){
        System.out.println();
        Scanner nameScanner = new Scanner(System.in);
        System.out.print("Enter name (computing ID): ");
        String name = nameScanner.nextLine();
        Scanner pwScanner = new Scanner(System.in);
        System.out.print("Enter password: ");
        String password = pwScanner.nextLine();
        if(!studentInDatabase(name)){
            System.out.println("You are not in database. Make an account!");
            initialPrompt();
        }
        List<Student> studentList = databaseManager.getStudents();
        Student student1 = new Student(name, password);
        for(Student s : studentList){
            if(s.getName().equals(name)){
                student1 = s;
            }
        }
        if(!student1.getPassword().equals(password)){
            System.out.println("Incorrect password. Try again!");
            initialPrompt();
        }
        student = new Student(name, password);
        databaseManager.addStudentLogIn(student);
    }
    public static void createAccountPrompt(){
        System.out.println();
        Scanner nameScanner = new Scanner(System.in);
        System.out.print("Enter name: ");
        String name = nameScanner.nextLine();
        Scanner pwScanner = new Scanner(System.in);
        System.out.print("Enter password: ");
        String password = pwScanner.nextLine();
        Scanner pwConfirmScanner = new Scanner(System.in);
        System.out.print("Confirm password: ");
        String passwordConfirm = pwConfirmScanner.nextLine();
        if(studentInDatabase(name)){
            System.out.println("You already are in the database, log in!");
            initialPrompt();
        }
        if(password.equals(passwordConfirm)){
            student = new Student(name, password);
            databaseManager.addStudentCreateAccount(student);
        } else{
            System.out.println("Passwords do not match. Please try again.");
            initialPrompt();
        }
    }
    public static boolean studentInDatabase(String name){
        List<Student> studentList = databaseManager.getStudents();
        for(Student s : studentList){
            if(s.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    public static void mainMenuPrompt(){
        System.out.println();
        Scanner mmScanner = new Scanner(System.in);
        System.out.print("Submit a review for a course (1), see reviews for a course (2), or log out (3): ");
        String mmName = mmScanner.nextLine();
        if(mmName.equals("1")){
            submitReviewPrompt();
            mainMenuPrompt();
        } else if (mmName.equals("2")){
            seeReviewsPrompt();
            mainMenuPrompt();
        } else if (mmName.equals("3")){
            initialPrompt();
        } else{
            System.out.println("Invalid response. Must enter 1 or 2.");
            mainMenuPrompt();
        }
    }
    public static void submitReviewPrompt(){
        System.out.println();
        Scanner courseNameScanner = new Scanner(System.in);
        System.out.print("Enter course name (e.g. 'CS 3140'): ");
        String courseName = courseNameScanner.nextLine();
        if (!validCourseName(courseName)){
            System.out.println("Not a valid course name. Must be 2-4 character department name (CAPITALIZED) and 4 digit CourseID number)!");
            mainMenuPrompt();
        } else if (studentAlreadyMadeReview(courseName)){
            System.out.println("You have already made a review for this class. You can make one for another class!");
            mainMenuPrompt();
        } else{
            Scanner messageScanner = new Scanner(System.in);
            System.out.print("Enter review message: ");
            String message = messageScanner.nextLine();
            if(message.isBlank()){
                System.out.println("Blank review. Please enter an actual message.");
                submitReviewPrompt();
            }
            Scanner ratingScanner = new Scanner(System.in);
            System.out.print("Enter rating (integer 1-5): ");
            int rating = Integer.valueOf(ratingScanner.nextLine());
            if(!validRating(rating)){
                System.out.println("Not a valid rating. Please enter 1, 2, 3, 4 or 5 and try again!");
                submitReviewPrompt();
            }
            else{
                String[] splitString = courseName.split(" ");
                String courseDept = splitString[0];
                int courseCatNum = Integer.parseInt(splitString[1]);
                course = new Course(courseDept, courseCatNum);
                review = new Review(student.getName(), course, message, rating);
                databaseManager.addReview(student, course, review);
            }
        }
    }
    public static boolean studentAlreadyMadeReview(String courseName){
        List<Review> reviewList = databaseManager.getReviews(courseName);
        for(Review r : reviewList){
            if(r.getStudentName().equals(student.getName())){
                return true;
            }
        }
        return false;
    }
    public static void seeReviewsPrompt(){
        System.out.println();
        Scanner courseNameScanner = new Scanner(System.in);
        System.out.print("Enter course name (e.g. 'CS 3140'): ");
        String courseName = courseNameScanner.nextLine();
        if (!validCourseName(courseName)){
            System.out.println("Not a valid course name. Must be 2-4 character department name (CAPITALIZED) and 4 digit CourseID number)!");
            mainMenuPrompt();
        }
        List<Review> reviewList = databaseManager.getReviews(courseName);
        if(reviewList.size() == 0){
            System.out.println("There are no reviews for this course. Try a different course!");
            mainMenuPrompt();
        }
        printReviews(reviewList);
        mainMenuPrompt();
    }
    public static boolean validCourseName(String courseName){
        String[] splitString = courseName.split(" ");
        if(splitString.length != 2){
            return false;
        }
        if(!splitString[0].toUpperCase().equals(splitString[0])){
            return false;
        }
        if(splitString[0].length() >= 5 || splitString[0].length() <= 1){
            return false;
        }
        for(int i = 0; i < splitString[0].length(); i++){
            if(!Character.isLetter(splitString[0].charAt(i))){
                return false;
            }
        }
        if(splitString[1].length() != 4){
            return false;
        }
        for (int i = 0; i < splitString[1].length(); i++) {
            if (!Character.isDigit(splitString[1].charAt(i))) {
                return false;
            }
        }
        return true;
    }
    public static boolean validRating(int rating){
        if(rating == 1 || rating == 2 || rating == 3 || rating == 4 || rating == 5){
            return true;
        }
        return false;
    }
    public static void printReviews(List<Review> reviewList){
        int totalRating = 0;
        System.out.println();
        System.out.println("Here's what other students have to say about the class!");
        for(Review r : reviewList){
            System.out.println("'" + r.getMessage() + "'");
            totalRating += r.getRating();
        }
        double avgRating = (double) totalRating / reviewList.size();
        System.out.println("The average rating for this course is " + avgRating + "/5.");
    }
}
