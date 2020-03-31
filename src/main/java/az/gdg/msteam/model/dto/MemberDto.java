package az.gdg.msteam.model.dto;

//import az.gdg.msteam.validation.MemberConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@MemberConstraint
public class MemberDto {
    @NotBlank(message = "First name cannot be empty")
    private String firstName;


    private String lastName;

    @NotBlank(message = "Email cannot be empty")

    private String email;

    private String linkedin;
    private String github;
    private String position;
    private String photo;
}
