package az.gdg.msteam.util;

import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

@Component
public class CheckViolationHelper {
    public CheckViolationHelper() {
    }

    public void addViolation(ConstraintValidatorContext context, String node, String messageTemplate) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageTemplate).addPropertyNode(node).addConstraintViolation();
    }
}
