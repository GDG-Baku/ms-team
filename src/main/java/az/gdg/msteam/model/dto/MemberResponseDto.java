package az.gdg.msteam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String linkedin;
    private String github;
    private String position;
    private byte[] photo;
}
