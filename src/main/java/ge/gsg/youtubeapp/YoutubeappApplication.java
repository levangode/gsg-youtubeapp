package ge.gsg.youtubeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YoutubeappApplication {

    public static void main(String[] args) {
        SpringApplication.run(YoutubeappApplication.class, args);
    }

}
