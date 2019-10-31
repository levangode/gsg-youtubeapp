package ge.gsg.youtubeapp;

import ge.gsg.youtubeapp.domain.User;
import ge.gsg.youtubeapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    @Autowired
    UserRepository users;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        this.users.save(User.builder()
                .username("user")
                .password(this.passwordEncoder.encode("password"))
                .country("GE")
                .jobInterval(50L)
                .nextRunDate(new Date(new Date().getTime()+50L))
                .roles(Arrays.asList("ROLE_USER"))
                .build()
        );

        this.users.save(User.builder()
                .username("admin")
                .password(this.passwordEncoder.encode("password"))
                .country("GE")
                .jobInterval(50L)
                .nextRunDate(new Date(new Date().getTime()+50L))
                .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                .build()
        );

        log.debug("printing all users...");
        this.users.findAll().forEach(v -> log.debug(" User :" + v.toString()));
    }
}
