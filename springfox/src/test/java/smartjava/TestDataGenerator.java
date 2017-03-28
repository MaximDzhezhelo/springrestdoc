package smartjava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import smartjava.domain.speaker.SpeakerRepository;

@Service
public class TestDataGenerator {

    @Autowired
    private SpeakerRepository speakerRepository;

    public SpeakerDataBuilder speaker(String speakerName) {
        return new SpeakerDataBuilder(speakerRepository).speaker(speakerName);
    }

}