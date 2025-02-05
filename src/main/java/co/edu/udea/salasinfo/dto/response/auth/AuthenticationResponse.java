package co.edu.udea.salasinfo.dto.response.auth;

import co.edu.udea.salasinfo.utils.enums.RoleName;
import lombok.*;

import java.awt.datatransfer.StringSelection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String token;
    private RoleName role;
    private String id;
}
