package ge.gsg.youtubeapp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ge.gsg.youtubeapp.domain.User;
import ge.gsg.youtubeapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
public class UpdateService {
    private final UserRepository users;
    private static final String topVideosUrl = "https://www.googleapis.com/youtube/v3/videos?part=snippet&chart=mostPopular&regionCode=%s&key=AIzaSyBZrUkg0xCovguJNRvXY-fu2Td-i1dxZqg";
    private static final String topCommentsUrl = "https://www.googleapis.com/youtube/v3/commentThreads?part=snippet&videoId=%s&order=relevance&key=AIzaSyBZrUkg0xCovguJNRvXY-fu2Td-i1dxZqg";
    //TODO extract to properties

    private ObjectMapper mapper = new ObjectMapper();
    private RestTemplate youtubeApi = new RestTemplate();

    public UpdateService(UserRepository users) {
        this.users = users;
    }

    private String getVideo(String country) throws JsonProcessingException {
        ResponseEntity<String> response;
        JsonNode root;
        response = youtubeApi.getForEntity(String.format(topVideosUrl, country), String.class);
        root = mapper.readTree(Objects.requireNonNull(response.getBody()));
        return root.path("items").get(0).path("id").textValue();
    }

    private String getComment(String topVideoId) throws JsonProcessingException {
        ResponseEntity<String> response;
        JsonNode root;
        response = youtubeApi.getForEntity(String.format(topCommentsUrl, topVideoId), String.class);
        root = mapper.readTree(Objects.requireNonNull(response.getBody()));
        return root.path("items").get(0).path("snippet").path("topLevelComment").path("snippet").path("textDisplay").textValue();
    }

    public void updateUsers() {
        log.info("Updating users");
        this.users.findAll().forEach(user -> {
            try {
                if (user.getNextRunDate().compareTo(new Date()) < 0) {
                    updateUser(user);
                }
            } catch (Exception e) {
                log.error("Error while updating user");
            }
        });
    }

    public void updateUser(User user) {
        log.info("Updating user {}", user.getUsername());
        try {
            String topVideoId = getVideo(user.getCountry());
            String topComment = getComment(topVideoId);
            user.setTopVideo(topVideoId);
            user.setTopComment(topComment);
            user.setNextRunDate(new Date((user.getNextRunDate() == null ? new Date().getTime() : user.getNextRunDate().getTime()) + user.getJobInterval() * 60000));
            users.save(user);
        } catch (Exception e) {
            log.error("Error in updating user");
        }
    }
}
