package ge.gsg.youtubeapp;

import ge.gsg.youtubeapp.domain.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {
    @Test
    public void testUser() {
        User u = User.builder().username("test").build();
        u.setId(1L);
        assertEquals("id is 1L", 1L, (long) u.getId());
        assertEquals("username is test", "test", u.getUsername());

        User u2 =  User.builder().username("test2").build();
        u2.setId(2L);
        assertEquals("id is 2L", 2L, (long) u2.getId());
        assertEquals("username is test2", "test2", u2.getUsername());
    }
}
