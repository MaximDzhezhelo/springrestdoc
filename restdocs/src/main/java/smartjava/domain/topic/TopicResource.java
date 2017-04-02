package smartjava.domain.topic;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.time.Duration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Relation(value = "topic", collectionRelation = "topics")
public class TopicResource extends ResourceSupport {

    private String name;
    private String description;
    private Duration duration;

    public TopicResource(Topic topic) {
        this.name = topic.getName();
        this.description = topic.getDescription();
        this.duration = topic.getDuration();
        add(linkTo(methodOn(TopicController.class).getTopic(topic.getId())).withSelfRel());
    }
}