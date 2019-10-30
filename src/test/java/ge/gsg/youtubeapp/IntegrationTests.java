package ge.gsg.youtubeapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.gsg.youtubeapp.models.AuthenticationRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
public class IntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    ObjectMapper objectMapper;

    private String token;

    @Before
    public void setup() {
        RestAssured.port = this.port;
        token = given()
                .contentType(ContentType.JSON)
                .body(AuthenticationRequest.builder().username("user").password("password").build())
                .when().post("/auth/signin")
                .andReturn().jsonPath().getString("token");
//        log.debug("Got token:" + token);
        System.out.println(token);
    }
}
