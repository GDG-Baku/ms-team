package az.gdg.msteam.model.dto;

import az.gdg.msteam.validation.MemberConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MemberConstraint
public class MemberDto {
    private String firstName;
    private String lastName;
    private String email;
    private String linkedin;
    private String github;
    private String position;
    private List<String> photo;
}
