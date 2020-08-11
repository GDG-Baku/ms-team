package az.gdg.msteam.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

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
