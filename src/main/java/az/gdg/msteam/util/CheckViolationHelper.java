package az.gdg.msteam.util;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidatorContext;

@Component
public class CheckViolationHelper {
    public CheckViolationHelper() {
    }

    public void addViolation(ConstraintValidatorContext context, String node, String messageTemplate) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageTemplate).addPropertyNode(node).addConstraintViolation();
    }
}
