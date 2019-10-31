package ge.gsg.youtubeapp.schedulers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ge.gsg.youtubeapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Component
@Slf4j
public class YoutubeApiScheduler {

    private String topVideosUrl = "https://www.googleapis.com/youtube/v3/videos?part=snippet&chart=mostPopular&regionCode=%s&key=AIzaSyBZrUkg0xCovguJNRvXY-fu2Td-i1dxZqg";
    private String topCommentsUrl = "https://www.googleapis.com/youtube/v3/commentThreads?part=snippet&videoId=%s&order=relevance&key=AIzaSyBZrUkg0xCovguJNRvXY-fu2Td-i1dxZqg";

    private RestTemplate youtubeApi = new RestTemplate();
    private final UserRepository users;

    public YoutubeApiScheduler(UserRepository users) {
        this.users = users;
    }

    @Scheduled(fixedDelay = 10000)
    public void updateUsers() {
        log.info("Scheduler triggered");
        ObjectMapper mapper = new ObjectMapper();
        this.users.findAll().forEach(user -> {
            ResponseEntity<String> response = null;
            try {
                response = youtubeApi.getForEntity(String.format(topVideosUrl, user.getCountry()), String.class);
            } catch (Exception e){
                log.error("Error accessing youtube api");
            }
            JsonNode root;
            try {
                root = mapper.readTree(response.getBody());
                String topVideoId = root.path("items").get(0).path("id").textValue();
                user.setTopVideo(topVideoId);
                response = youtubeApi.getForEntity(String.format(topCommentsUrl, topVideoId), String.class);
                root = mapper.readTree(response.getBody());
                String topComment = root.path("items").get(0).path("snippet").path("topLevelComment").path("snippet").path("textDisplay").textValue();
                user.setTopComment(topComment);
            } catch (JsonProcessingException e) {
                log.error("Response Body Empty");
            }

//            log.info(response.getBody());
//            if(user.getNextRunDate().compareTo(currentDate) > 0){
//                youtubeApi.exchange(url, HttpMethod.GET);
//            }
            user.setNextRunDate(new Date(user.getNextRunDate().getTime() + user.getJobInterval() * 60000));
            users.save(user);
        });
    }
}
