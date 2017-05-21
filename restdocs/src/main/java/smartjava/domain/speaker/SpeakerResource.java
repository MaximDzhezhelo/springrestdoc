package smartjava.domain.speaker;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Relation(value = "speaker", collectionRelation = "speakers")
public class SpeakerResource extends ResourceSupport {

    private String name;
    private String company;
    private String status;

    public SpeakerResource(Speaker speaker) {
        this.name = speaker.getName();
        this.company = speaker.getCompany();
        this.status = "I LIKE morning@lohika \\0// !!!";

        add(linkTo(methodOn(SpeakerController.class).getSpeaker(speaker.getId())).withSelfRel());
        add(linkTo(methodOn(SpeakerController.class).getSpeakerTopics(speaker.getId())).withRel("topics"));
    }
}