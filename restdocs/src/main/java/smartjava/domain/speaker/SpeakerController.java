package smartjava.domain.speaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.stream.Collectors;

@RestController
@ExposesResourceFor(value = SpeakerResource.class)
public class SpeakerController {

    @Autowired
    private SpeakerRepository speakerRepository;

    @GetMapping(value = "/speakers/{id}")
    public ResponseEntity<SpeakerResource> getSpeaker(
            @PathVariable long id) {
        return speakerRepository.findOne(id)
                .map(speaker -> ResponseEntity.ok(new SpeakerResource(speaker)))
                .orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/speakers")
    public Resources<SpeakerResource> getAllSpeakers() {
        return new Resources(speakerRepository.findAll().stream()
                .map(SpeakerResource::new)
                .collect(Collectors.toList()));
    }

    @PostMapping(value = "/speakers")
    public ResponseEntity<SpeakerResource> createSpeaker(@Validated @RequestBody SpeakerDto speakerDto) {
        Speaker savedSpeaker = speakerRepository.save(speakerDto.createSpeaker());
        Link linkToTestStep = new SpeakerResource(savedSpeaker).getLink(Link.REL_SELF);
        return ResponseEntity.created(URI.create(linkToTestStep.getHref())).build();
    }

    @DeleteMapping(value = "speakers/{$id}")
    public void deleteSpeaker(@PathVariable long id) {
        speakerRepository.delete(id);
    }
}