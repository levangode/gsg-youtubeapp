package ge.gsg.youtubeapp.controllers;

import ge.gsg.youtubeapp.domain.User;
import ge.gsg.youtubeapp.models.RegistrationRequest;
import ge.gsg.youtubeapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/users")
public class UserinfoController {

    @Autowired
    private UserRepository users;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @CrossOrigin
    @GetMapping("/me")
    public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        Map<Object, Object> model = new HashMap<>();
        model.put("username", userDetails.getUsername());
        model.put("roles", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(toList())
        );
        users.findByUsername(userDetails.getUsername()).ifPresent(user -> model.put("jobInterval", user.getJobInterval()));
        users.findByUsername(userDetails.getUsername()).ifPresent(user -> model.put("topVideo", user.getTopVideo()));
        return ok(model);
    }

    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegistrationRequest registrationRequest) {
        try {
            if (this.users.findByUsername(registrationRequest.getUsername()).isPresent()) {
                return badRequest().body("Username Already Exists");
            }
            this.users.save(User.builder()
                    .username(registrationRequest.getUsername())
                    .password(this.passwordEncoder.encode(registrationRequest.getPassword()))
                    .country(registrationRequest.getCountry())
                    .jobInterval(registrationRequest.getJobInterval())
                    .nextRunDate(new Date(new Date().getTime() + registrationRequest.getJobInterval()))
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build()
            );
            return ok().build();
        } catch (Exception e) {
            return badRequest().build();
        }
    }
}
