package smartjava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import smartjava.domain.speaker.SpeakerDto;
import smartjava.domain.speaker.SpeakerRepository;

@Service
public class TestDataGenerator {

    @Autowired
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    private SpeakerRepository speakerRepository;

    public SpeakerDataBuilder speaker(String speakerName) {
        return new SpeakerDataBuilder(speakerRepository).speaker(speakerName);
    }

    public SpeakerDto.SpeakerDtoBuilder speakerDto(String name) {
        return SpeakerDto.builder().name(name);
    }

    @SneakyThrows
    public String asJsonString(Object o) {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        messageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}