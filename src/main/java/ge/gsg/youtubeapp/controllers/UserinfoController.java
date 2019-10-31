package ge.gsg.youtubeapp.controllers;

import ge.gsg.youtubeapp.domain.User;
import ge.gsg.youtubeapp.models.RegistrationRequest;
import ge.gsg.youtubeapp.models.UpdateParamsRequest;
import ge.gsg.youtubeapp.repository.UserRepository;
import ge.gsg.youtubeapp.services.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/users")
public class UserinfoController {

    private final UserRepository users;

    private final PasswordEncoder passwordEncoder;

    private final UpdateService updateService;

    public UserinfoController(UserRepository users, PasswordEncoder passwordEncoder, UpdateService updateService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.updateService = updateService;
    }

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
        Optional<User> userOptional = users.findByUsername(userDetails.getUsername());
        userOptional.ifPresent(user -> {
            model.put("jobInterval", user.getJobInterval());
            model.put("topVideo", user.getTopVideo());
            model.put("country", user.getCountry());
            model.put("topComment", user.getTopComment());
        });
        return ok(model);
    }

    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        try {
            if (this.users.findByUsername(registrationRequest.getUsername()).isPresent()) {
                return badRequest().body("Username Already Exists");
            }
            User user = User.builder()
                    .username(registrationRequest.getUsername())
                    .password(this.passwordEncoder.encode(registrationRequest.getPassword()))
                    .country(registrationRequest.getCountry())
                    .jobInterval(registrationRequest.getJobInterval())
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
            updateService.updateUser(user);
            return ok().build();
        } catch (Exception e) {
            return badRequest().build();
        }
    }

    @CrossOrigin
    @PostMapping("/update")
    public ResponseEntity update(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdateParamsRequest updateParamsRequest) {
        try {
            Optional<User> userOptional = users.findByUsername(userDetails.getUsername());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setJobInterval(updateParamsRequest.getJobInterval());
                user.setCountry(updateParamsRequest.getCountry());
                user.setNextRunDate(new Date(new Date().getTime() + updateParamsRequest.getJobInterval()));
                users.save(user);
                return ok().build();
            } else {
                return badRequest().body("User not found");
            }
        } catch (Exception e) {
            return badRequest().build();
        }
    }
}
