package ge.gsg.youtubeapp.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest implements Serializable {
	private static final long serialVersionUID = -6986746375915710855L;
	@NotEmpty
	private String username;
	@NotEmpty
	private String password;
}
