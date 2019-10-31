package ge.gsg.youtubeapp;

import ge.gsg.youtubeapp.domain.User;
import ge.gsg.youtubeapp.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository users;

    @Test
    public void mapping() {
        User saved = this.users.save(User.builder().username("test").build());
        User u = this.users.getOne(saved.getId());
        assertThat(u.getUsername()).isEqualTo("test");
        assertThat(u.getId()).isNotNull();
        assertThat(u.getId()).isGreaterThan(0);
    }
}
