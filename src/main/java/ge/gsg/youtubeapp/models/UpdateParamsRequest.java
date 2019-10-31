package ge.gsg.youtubeapp.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateParamsRequest {
    private static final long serialVersionUID = -6986746375915710855L;
    private String country;
    private Long jobInterval;
}
