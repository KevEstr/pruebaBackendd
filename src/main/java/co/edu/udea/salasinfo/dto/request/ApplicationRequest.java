package co.edu.udea.salasinfo.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ApplicationRequest {
    private String applicationName;
    private String version;
}
