import com.tanknavy.avro.User;
import org.junit.Test;

/**
 * Author: Alex Cheng 9/4/2020 12:43 PM
 */


public class TestCase {

    @Test
    public void testUser() {
        User user1 = new User();
        user1.setName("Alyssa");
        user1.setTitle("Sr Director");
        user1.setDisplayName("Bob Cheng");
        // Leave favorite color null

        // Alternate constructor
        User user2 = new User("Ben", "CTO","Ben Cheng");

        // Construct via builder
        User user3 = User.newBuilder()
                .setName("Charlie")
                .setTitle("blue")
                .setDisplayName("Charlie Cheng")
                .build();
    }


}
