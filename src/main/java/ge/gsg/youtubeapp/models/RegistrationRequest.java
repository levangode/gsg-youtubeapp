package ge.gsg.youtubeapp.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest implements Serializable {
    private static final long serialVersionUID = -6986746375915710855L;
    @NotEmpty
    private String password;
    @NotEmpty
    private String username;
    @NotEmpty
    private String country;
    @NotNull
    @Min(1)
    @Max(60)
    private Long jobInterval;
}
