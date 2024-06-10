package SpringAuthApiBoilerplate.dto;

import SpringAuthApiBoilerplate.auth.Authority;
import SpringAuthApiBoilerplate.entity.CustomUser;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponseDto {
    private String email;

    private String zname;

    private List<Authority> roles = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

    public SignupResponseDto(CustomUser customUser) {
        this.email = customUser.getEmail();
        this.zname = customUser.getZname();
        this.roles = customUser.getRoles();
    }
}
