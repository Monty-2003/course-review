package edu.virginia.cs.review;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SubmitReviewController {
    private String courseName;
    @FXML
    private Label questionLabel;
    @FXML
    private TextField messageTF;
    @FXML
    private TextField ratingTF;
    @FXML
    private Label errorLabel;
    @FXML
    private GridPane gridPane;
    @FXML
    private Button submitReviewButton;
    @FXML
    private Button mainMenuButton;
    @FXML
    private Button logOutButton;
    private DatabaseManagerImpl databaseManager;
    private static Student student;
    public void setInfo(String courseName, Student student) {
        this.courseName = courseName;
        this.student = student;
        questionLabel.setText("What do you have to say about " + courseName + "?");
    }
    public void initialize(){
        gridPane.setVisible(true);
        databaseManager = new DatabaseManagerImpl();
        databaseManager.connect();
        questionLabel.setVisible(true);
        errorLabel.setVisible(false);
    }
    public void logOut(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) messageTF.getScene().getWindow();
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
    public void goToMainMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainmenu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) messageTF.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading mainmenu.fxml: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void submitReview(){
        if(messageTF.getText().isEmpty() || ratingTF.getText().isEmpty()){
            errorLabel.setVisible(true);
            errorLabel.setText("ERROR: One or more fields are blank.");
        } else if (!validRating(ratingTF.getText())){
            errorLabel.setVisible(true);
            errorLabel.setText("Not a valid rating. Enter 1, 2, 3, 4 or 5!");
        } else {
            String message = messageTF.getText();
            int rating = Integer.parseInt(ratingTF.getText());
            String[] splitString = courseName.split(" ");
            String courseDept = splitString[0];
            int courseCatNum = Integer.parseInt(splitString[1]);
            Course course = new Course(courseDept, courseCatNum);
            Review review = new Review(student.getName(), course, message, rating);
            databaseManager.addReview(student, course, review);
            gridPane.setVisible(false);
            messageTF.clear();
            ratingTF.clear();
            questionLabel.setText("Thanks for submitting a review for " + courseName + "!");
        }
    }
    public static boolean validRating(String rating){
        if(rating.equals("1") || rating.equals("2") || rating.equals("3") || rating.equals("4") || rating.equals("5")){
            return true;
        }
        return false;
    }
}
