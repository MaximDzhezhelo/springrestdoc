package smartjava;

import java.util.Arrays;

import smartjava.domain.speaker.Speaker;
import smartjava.domain.speaker.SpeakerRepository;
import smartjava.domain.topic.Topic;
import smartjava.domain.topic.TopicRepository;

public class SpeakerDataBuilder {

    private final SpeakerRepository speakerRepository;
    private final TopicRepository topicRepository;

    private Speaker speaker;

    public SpeakerDataBuilder(SpeakerRepository speakerRepository, TopicRepository topicRepository) {
        this.speakerRepository = speakerRepository;
        this.topicRepository = topicRepository;
    }

    public SpeakerDataBuilder speaker(String name) {
        this.speaker = Speaker.builder().name(name).build();
        return this;
    }

    public SpeakerDataBuilder company(String company) {
        this.speaker.setCompany(company);
        return this;
    }

    public SpeakerDataBuilder topics(Topic... topics) {
        this.speaker.setTopics(Arrays.asList(topics));
        return this;
    }

    public Speaker build() {
        return this.speaker;
    }

    public Speaker save() {
        topicRepository.save(this.speaker.getTopics());
        return this.speakerRepository.save(this.speaker);
    }

}