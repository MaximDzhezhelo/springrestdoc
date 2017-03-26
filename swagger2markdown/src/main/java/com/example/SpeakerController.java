package com.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import io.swagger.annotations.ApiOperation;

@RestController
public class SpeakerController {

    @RequestMapping(value = "/speaker/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Gets speaker")
    public ResponseEntity<Speaker> speaker(@PathVariable long id) {
        return ResponseEntity.ok(new Speaker("Name" + id, "" + id, "company"));
    }

    @RequestMapping(value = "/speakers", method = RequestMethod.GET)
    @ApiOperation(value = "Gets all speakers")
    public List<Speaker> allSpeakers() {
        return Arrays.asList(new Speaker("Name", "", "company"));
    }
}