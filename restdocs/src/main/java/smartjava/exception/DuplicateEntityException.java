package smartjava.exception;

import org.slf4j.helpers.MessageFormatter;

import smartjava.domain.speaker.Speaker;

public class DuplicateEntityException extends RuntimeException{

    public DuplicateEntityException(Class<Speaker> speakerClass, String message) {
        super(MessageFormatter.format("Entity  {} with name {} already present in DB." ,
                speakerClass.getSimpleName(),
                message).getMessage());
    }
}