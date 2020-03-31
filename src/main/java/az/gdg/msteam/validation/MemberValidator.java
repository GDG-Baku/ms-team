package az.gdg.msteam.validation;

import az.gdg.msteam.model.dto.MemberDto;
import az.gdg.msteam.util.CheckViolationHelper;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class MemberValidator implements ConstraintValidator<MemberConstraint, MemberDto> {

    private final CheckViolationHelper checkViolationHelper;

    public MemberValidator(CheckViolationHelper checkViolationHelper) {
        this.checkViolationHelper = checkViolationHelper;
    }


    @Override
    public void initialize(MemberConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(MemberDto memberDto, ConstraintValidatorContext context) {
        return memberDto != null &&
                isFirstNameValid(memberDto.getFirstName(), context) &&
                isLastNameValid(memberDto.getLastName(), context) &&
                isEmailValid(memberDto.getEmail(), context);
    }

    private boolean isFirstNameValid(String firstName, ConstraintValidatorContext context) {
        if (firstName == null || firstName.length() == 0) {
            checkViolationHelper.addViolation(context, firstName, "First name cannot be empty");
            return false;
        }
        return true;
    }

    private boolean isLastNameValid(String lastName, ConstraintValidatorContext context) {
        if (lastName == null || lastName.length() == 0) {
            checkViolationHelper.addViolation(context, lastName, "Last name cannot be empty");
            return false;
        }
        return true;
    }

    private boolean isEmailValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.length() == 0) {
            checkViolationHelper.addViolation(context, email, "Email cannot be empty");
            return false;
        }
        return true;
    }


}
