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
@ApiModel(value = "Speaker Resource", description = "Speaker detailed description.")
public class SpeakerResource extends ResourceSupport {

    @ApiModelProperty(value = "Name of the Speaker in full format.",
            example = "Roman Tsypuk",
            dataType = "java.lang.String",
            required = true)
    private String name;

    @ApiModelProperty(value = "The company that speaker is working on.",
            example = "Lohika",
            dataType = "java.lang.String",
            required = true,
            allowableValues = "Full company names.")
    private String company;

    @ApiModelProperty(value = "Current status.",
            example = "I like Spring & Rest Docs.",
            dataType = "java.lang.String",
            required = true,
            allowableValues = "Only positive :)")
    private String status;

    public SpeakerResource(Speaker speaker) {
        this.name = speaker.getName();
        this.company = speaker.getCompany();
        this.status = "I like Spring & Rest Docs.";
        add(linkTo(methodOn(SpeakerController.class).getSpeaker(speaker.getId())).withSelfRel());
    }
}