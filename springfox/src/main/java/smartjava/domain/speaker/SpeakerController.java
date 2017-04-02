package smartjava.domain.speaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/", tags = "Speakers", description = "Operations about speaker")
@RestController
@ExposesResourceFor(value = SpeakerResource.class)
public class SpeakerController {

    @Autowired
    private SpeakerRepository speakerRepository;

    @ApiOperation(
            value = "Find speaker by ID",
            notes = "For valid response try integer IDs with value 1 ... 999. Other values will generated exceptions",
            response = SpeakerResource.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieve the speaker.", response = SpeakerResource.class),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Speaker not found"),
            @ApiResponse(code = 500, message = "Internal server error.")})
    @GetMapping(value = "/speakers/{id}")
    public ResponseEntity<SpeakerResource> getSpeaker(
            @ApiParam(value = "ID of speaker that needs to be fetched", allowableValues = "range[1,999]", required = true)
            @PathVariable long id) {
        return speakerRepository.findOne(id)
                .map(speaker -> ResponseEntity.ok(new SpeakerResource(speaker)))
                .orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/speakers")
    @ApiOperation(value = "Get all speakers.")
    public Resources<SpeakerResource> allSpeakers() {
        return new Resources(speakerRepository.findAll().stream()
                .map(SpeakerResource::new)
                .collect(Collectors.toList()));
    }

    @PostMapping(value = "/speakers/{id}")
    public ResponseEntity<SpeakerResource> createSpeaker(@PathVariable long id) {
        return null;
    }

    @PutMapping(value = "/speakers/{id}")
    public void updateSpeaker(@PathVariable long id) {

    }

    @DeleteMapping(value = "speakers/{$id}")
    @ApiOperation(value = "Delete speaker.")
    public void deleteSpeaker(@PathVariable long id) {
        speakerRepository.delete(id);
    }
}