package edu.virginia.cs.review;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SeeReviewsController {
    @FXML
    private Label contextLabel;
    @FXML
    private Label ratingLabel;
    @FXML
    private Label reviewOne;
    @FXML
    private Label reviewTwo;
    @FXML
    private Label reviewThree;
    @FXML
    private Label reviewFour;
    @FXML
    private Label reviewFive;
    private DatabaseManagerImpl databaseManager;
    private List<Label> reviews;

    public void setCourse(String courseName) {
        contextLabel.setText("Showing reviews for " + courseName + "...");
        showReviews(courseName);
    }
    public void initialize(){
        databaseManager = new DatabaseManagerImpl();
        databaseManager.connect();
        makeLabelList();
    }
    public void makeLabelList(){
        reviews = new ArrayList<>();
        reviews.add(reviewOne);
        reviews.add(reviewTwo);
        reviews.add(reviewThree);
        reviews.add(reviewFour);
        reviews.add(reviewFive);
    }
    public void goToMainMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainmenu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) contextLabel.getScene().getWindow();
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
    public void logOut(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) contextLabel.getScene().getWindow();
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
    public void showReviews(String courseName){
        List<Review> reviewList = databaseManager.getReviews(courseName);
        int numReviews = reviewList.size();
        if(numReviews <= 5){
            for(int i = 0; i < numReviews; i++){
                String message = reviewList.get(i).getMessage();
                reviews.get(i).setText("'" + message + "'");
            }
        } else{
            for(int i = numReviews - 5; i < numReviews; i++){
                reviewOne.setText("'" + reviewList.get(numReviews - 5).getMessage() + "'");
                reviewTwo.setText("'" + reviewList.get(numReviews - 4).getMessage() + "'");
                reviewThree.setText("'" + reviewList.get(numReviews - 3).getMessage() + "'");
                reviewFour.setText("'" + reviewList.get(numReviews - 2).getMessage() + "'");
                reviewFive.setText("'" + reviewList.get(numReviews - 1).getMessage() + "'");
            }
        }
        int totalRating = 0;
        for(Review r : reviewList){
            totalRating += r.getRating();
        }
        double avgRating = (double) totalRating / reviewList.size();
        ratingLabel.setText("The average rating for this course is " + avgRating + "/5.");
    }
}
