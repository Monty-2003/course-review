package edu.virginia.cs.review;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class MainMenuController {
    @FXML
    private Button submitReviewButton;
    @FXML
    private Button seeReviewsButton;
    @FXML
    private Label enterNameLabel;
    @FXML
    private TextField enterNameTF;
    @FXML
    private Button goBackButton;
    @FXML
    private Button enterButton;
    @FXML
    private Label errorLabel;
    @FXML
    private Label statusLabel;
    private String mode;
    private static DatabaseManagerImpl databaseManager;
    private static Student student;

    public void initialize(){
        databaseManager = new DatabaseManagerImpl();
        enterNameTF.textProperty().addListener((observable, oldValue, newValue) -> {
            enterNameTF.setText(newValue.toUpperCase());
        });
        databaseManager.connect();
        mode = "uninitialized";
        submitReviewButton.setVisible(true);
        seeReviewsButton.setVisible(true);
        enterNameLabel.setVisible(false);
        enterNameTF.setVisible(false);
        goBackButton.setVisible(false);
        errorLabel.setVisible(false);
        enterButton.setVisible(false);
        statusLabel.setVisible(false);
        enterNameTF.clear();
    }
    public void showOptions(){
        enterNameLabel.setVisible(true);
        enterNameTF.setVisible(true);
        enterButton.setVisible(true);
        goBackButton.setVisible(true);
        submitReviewButton.setVisible(false);
        seeReviewsButton.setVisible(false);
    }
    public void showOptionsSubmitReview(){
        statusLabel.setVisible(true);
        statusLabel.setText("Submitting a review...");
        showOptions();
        mode = "submitReview";
    }
    public void showOptionsSeeReviews(){
        statusLabel.setVisible(true);
        statusLabel.setText("Seeing reviews...");
        showOptions();
        mode = "seeReviews";
    }
    public void enterPressed(){
        if(enterNameTF.getText().isEmpty()){
            errorLabel.setVisible(true);
            errorLabel.setText("ERROR: Course name is blank.");
        } else if(mode.equals("seeReviews")){
            errorLabel.setVisible(false);
            String courseName = enterNameTF.getText();
            if(!validCourseName(courseName)){
                errorLabel.setVisible(true);
                errorLabel.setText("ERROR: Not a valid course name!");
            } else{
                errorLabel.setVisible(true);
                List<Review> reviewList = databaseManager.getReviews(courseName);
                if(reviewList.size() == 0){
                    errorLabel.setText("ERROR: There are no reviews for this course!");
                } else{
                    errorLabel.setVisible(false);
                    switchToSeeReviews();
                    // switch scene to see reviews page
                }
            }
        } else if (mode.equals("submitReview")){
            errorLabel.setVisible(false);
            String courseName = enterNameTF.getText();
            if(!validCourseName(courseName)){
                errorLabel.setVisible(true);
                errorLabel.setText("ERROR: Not a valid course name.");
            } else if (studentAlreadyMadeReview(courseName)){
                errorLabel.setVisible(true);
                errorLabel.setText("ERROR: You have already made a review for this class!");
            } else{
                errorLabel.setVisible(false);
                switchToSubmitReview();
                // switch scene to submit review page
            }
        }
    }
    public void goBackPressed(){
        initialize();
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
    public void logOut(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) enterNameTF.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading login.fxml: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
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
    public void setLoggedInUser(Student student) {
        this.student = student;
    }
    public void switchToSubmitReview() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("submit_review.fxml"));
            Parent root = loader.load();
            String courseName = enterNameTF.getText();
            SubmitReviewController submitReviewController = loader.getController();
            submitReviewController.setInfo(courseName, student);
            Scene scene = new Scene(root);
            Stage stage = (Stage) enterNameTF.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading submit_review.fxml: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void switchToSeeReviews() {
        try {
            String courseName = enterNameTF.getText();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("see_reviews.fxml"));
            Parent root = loader.load();
            SeeReviewsController seeReviewsController = loader.getController();
            seeReviewsController.setCourse(courseName);
            Scene scene = new Scene(root);
            Stage stage = (Stage) enterNameTF.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading see_reviews.fxml: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
