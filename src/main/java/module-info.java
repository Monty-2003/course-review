module CourseReview.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires javafx.graphics;

    opens edu.virginia.cs.review to javafx.fxml;
    exports edu.virginia.cs.review to javafx.graphics;
}