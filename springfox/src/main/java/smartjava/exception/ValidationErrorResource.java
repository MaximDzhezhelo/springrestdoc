package smartjava.exception;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;
import org.springframework.validation.FieldError;

import lombok.Getter;

@Getter
@Relation(value = "validationError", collectionRelation = "validationErrors")
public class ValidationErrorResource extends ResourceSupport {
    private final String property;
    private final String message;
    private final String invalidValue;

    public ValidationErrorResource(FieldError fieldError) {
        this.property = fieldError.getField();
        this.message = fieldError.getDefaultMessage();
        this.invalidValue = String.valueOf(fieldError.getRejectedValue());
    }
}