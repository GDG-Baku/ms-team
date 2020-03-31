package az.gdg.msteam.validation;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.TYPE})
@Constraint(validatedBy = MemberValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MemberConstraint {
    String message() default "Member validation error";

    Class[] groups() default {};

    Class[] payload() default {};

    String[] sortingFields() default {};
}
