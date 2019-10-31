package ge.gsg.youtubeapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.gsg.youtubeapp.domain.User;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
class YoutubeappApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext applicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.mockMvc = webAppContextSetup(this.applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testSave() throws Exception {
        this.mockMvc
                .perform(
                        post("/users/register")
                                .content(this.objectMapper.writeValueAsBytes(User.builder().username("test").password("123456")
                                        .country("GE").jobInterval(60L).nextRunDate(new Date()).build()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

    }
}
