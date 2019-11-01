package ge.gsg.youtubeapp.schedulers;

import ge.gsg.youtubeapp.services.UpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class YoutubeApiScheduler {

    private final UpdateService updateService;

    public YoutubeApiScheduler(UpdateService updateService) {
        this.updateService = updateService;
    }

    /**
     * Check is performed for all users every 1 minute
     */
    @Scheduled(cron = "0 * * * * *")
    public void updateUsers() {
        updateService.updateUsers();
    }
}
