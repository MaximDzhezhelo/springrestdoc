package smartjava.domain.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ExposesResourceFor(value = TopicResource.class)
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    @GetMapping(value = "/topics/{id}")
    public ResponseEntity<TopicResource> getTopic(@PathVariable long id) {
        return topicRepository.findOne(id)
                .map(topic -> ResponseEntity.ok(new TopicResource(topic)))
                .orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

}