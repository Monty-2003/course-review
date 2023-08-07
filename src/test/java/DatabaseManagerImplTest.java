package edu.virginia.cs.review;
import edu.virginia.cs.review.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import edu.virginia.cs.review.DatabaseManagerImpl;
import edu.virginia.cs.review.Student;

public class DatabaseManagerImplTest {

    private DatabaseManagerImpl dbManager;
    private Student student = new Student("Ben", "hello");
    private Student student2 = new Student("Mike", "happy");
    private Course course = new Course("CS", 3140);
    private Course course2 = new Course("CS", 2120);

    @BeforeEach
    public void setUp() {
        dbManager = new DatabaseManagerImpl();
        dbManager.connect();
        dbManager.deleteTables();
        dbManager.createTables();
    }

    @AfterEach
    void tearDown() {
        dbManager.clear();
        //dbManager.deleteTables();
    }

    @Test
    public void testAddReview() {
        Review review = new Review("Ben", course, "Test add review 1", 5);
        dbManager.addReview(student, course, review);
        assertNotNull(dbManager.getReviews("CS 3140"));
    }

    @Test
    void testClear() {
        // Check that all tables are cleared
        Review review = new Review("Ben", course, "Test add review 1", 5);
        dbManager.addReview(student, course, review);
        dbManager.clear();
        assertFalse(dbManager.tablesPopulated);
    }
    @Test
    public void testConnect() {
        assertNotNull(dbManager.connection);
    }

    @Test
    public void testGetReviews() {
        Review review1 = new Review("Ben",course, "Test review 1", 5);
        Review review2 = new Review("Mike",course, "Test review 2", 3);
        dbManager.addReview(student, course, review1);
        dbManager.addReview(student, course, review2);
        assertEquals(2, dbManager.getReviews("CS 3140").size());
}

}
