package edu.virginia.cs.review;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class LogInController {
    private Scene scene;
    private Stage stage;
    @FXML
    private Label titleLabel;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Button logInButton;
    @FXML
    private Button createAccountButton;
    @FXML
    private Label enterNameLabel;
    @FXML
    private Label enterPasswordLabel;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private TextField enterNameTF;
    @FXML
    private TextField enterPwTF;
    @FXML
    private TextField confirmPwTF;
    @FXML
    private Button confirmLiOrCaButton;
    @FXML
    private Label errorLabel;
    private static DatabaseManagerImpl databaseManager;
    private String buttonClicked;
    public void initialize(){
        initializeLogIn();
    }
    public void initializeLogIn(){
        enterNameTF.textProperty().addListener((observable, oldValue, newValue) -> {
            enterNameTF.setText(newValue.toLowerCase());
        });
        enterNameTF.requestFocus();
        databaseManager = new DatabaseManagerImpl();
        databaseManager.connect();
        enterNameTF.clear();
        enterPwTF.clear();
        confirmPwTF.clear();
        enterPwTF.setVisible(false);
        enterNameTF.setVisible(false);
        confirmPwTF.setVisible(false);
        enterNameLabel.setVisible(false);
        enterPasswordLabel.setVisible(false);
        confirmPasswordLabel.setVisible(false);
        confirmLiOrCaButton.setVisible(false);
    }
    public void logInPressed(){
        errorLabel.setVisible(false);
        enterNameTF.requestFocus();
        enterNameTF.clear();
        enterPwTF.clear();
        confirmPwTF.clear();
        buttonClicked = "logIn";
        enterPwTF.setVisible(true);
        enterNameTF.setVisible(true);
        enterNameLabel.setVisible(true);
        enterPasswordLabel.setVisible(true);
        confirmPasswordLabel.setVisible(false);
        confirmPwTF.setVisible(false);
        confirmLiOrCaButton.setVisible(true);
        confirmLiOrCaButton.setText("Log me in!");
    }

    public void createAccountPressed(){
        errorLabel.setVisible(false);
        enterNameTF.requestFocus();
        enterNameTF.clear();
        enterPwTF.clear();
        confirmPwTF.clear();
        buttonClicked = "createAccount";
        enterNameLabel.setVisible(true);
        enterPasswordLabel.setVisible(true);
        confirmPasswordLabel.setVisible(true);
        enterNameTF.setVisible(true);
        enterPwTF.setVisible(true);
        confirmPwTF.setVisible(true);
        confirmLiOrCaButton.setVisible(true);
        confirmLiOrCaButton.setText("Create account!");
    }
    public void confirmLiOrCa(){
        if(buttonClicked.equals("logIn")){
            if(enterNameTF.getText().isEmpty() || enterPwTF.getText().isEmpty()){
                errorLabel.setVisible(true);
                errorLabel.setText("ERROR: One or more fields are blank.");
            }else{
                errorLabel.setVisible(false);
                checkLogIn();
            }
        } else if (buttonClicked.equals("createAccount")){
            if(enterNameTF.getText().isEmpty() || enterPwTF.getText().isEmpty() || confirmPwTF.getText().isEmpty()){
                errorLabel.setVisible(true);
                errorLabel.setText("ERROR: One or more fields are blank.");
            } else {
                errorLabel.setVisible(false);
                checkCreateAccount();
            }
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText("ERROR: You must click either 'Log In' or 'Create Account'.");
        }
    }
    public void checkLogIn(){
        String name = enterNameTF.getText();
        String password = enterPwTF.getText();
        if(!studentInDatabase(name)){
            errorLabel.setVisible(true);
            errorLabel.setText("ERROR: You are not in database. Make an account!");
            initialize();
        } else{
            List<Student> studentList = databaseManager.getStudents();
            Student student = new Student(name, password);
            for(Student s : studentList){
                if(s.getName().equals(name)){
                    student = s;
                }
            }
            if(!student.getPassword().equals(password)){
                errorLabel.setVisible(true);
                errorLabel.setText("ERROR: Incorrect password. Try again!");
            } else{
                student = new Student(name, password);
                databaseManager.addStudentLogIn(student);
                switchToMainMenu();
            }
        }
    }
    public void checkCreateAccount(){
        String name = enterNameTF.getText();
        String password = enterPwTF.getText();
        String passwordConfirm = confirmPwTF.getText();
        if(studentInDatabase(name)){
            errorLabel.setVisible(true);
            errorLabel.setText("ERROR: You already are in the database, log in!");
            initialize();
        } else {
            if(password.equals(passwordConfirm)){
                Student student = new Student(name, password);
                databaseManager.addStudentCreateAccount(student);
                switchToMainMenu();
            } else{
                errorLabel.setVisible(true);
                errorLabel.setText("ERROR: Passwords do not match. Please try again.");
            }
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
    public void switchToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainmenu.fxml"));
            Parent root = loader.load();
            MainMenuController mainMenuController = loader.getController();
            String username = enterNameTF.getText();
            String password = enterPwTF.getText();
            Student student = new Student(username, password);
            mainMenuController.setLoggedInUser(student);
            Scene scene = new Scene(root);
            Stage stage = (Stage) enterNameTF.getScene().getWindow();
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

}
