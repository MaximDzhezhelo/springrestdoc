package smartjava.domain.speaker;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Relation(value = "speaker", collectionRelation = "speakers")
@ApiModel(value = "Speaker", description = "Speaker description")
public class SpeakerResource extends ResourceSupport {

    @ApiModelProperty(value = "Name of the Speaker in full format.", dataType = "java.lang.Integer", required = true)
    private String name;
    private String company;

    public SpeakerResource(Speaker speaker) {
        this.name = speaker.getName();
        this.company = speaker.getCompany();
        add(linkTo(methodOn(SpeakerController.class).getSpeaker(speaker.getId())).withSelfRel());
    }
}