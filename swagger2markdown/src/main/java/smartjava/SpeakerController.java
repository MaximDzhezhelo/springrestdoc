package smartjava;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "/", tags = "Speakers", description = "Operations about speaker")
public class SpeakerController {

    @RequestMapping(value = "/speaker/{id}", method = RequestMethod.GET)
    @ApiOperation(
            value = "Find speaker by ID",
            notes = "For valid response try integer IDs with value <= 5 or > 10. Other values will generated exceptions",
            response = Speaker.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Speaker not found")})
    public ResponseEntity<Speaker> speaker(
            @ApiParam(value = "ID of speaker that needs to be fetched", allowableValues = "range[1,5]", required = true)
            @PathVariable long id) {
        return ResponseEntity.ok(new Speaker("Name" + id, "" + id, "company"));
    }

    @RequestMapping(value = "/speakers", method = RequestMethod.GET)
    public List<Speaker> allSpeakers() {
        return Arrays.asList(new Speaker("Name", "", "company"));
    }
}