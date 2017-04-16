package smartjava;

import smartjava.domain.topic.Topic;
import smartjava.domain.topic.TopicRepository;

public class TopicDataBuilder {

    private final TopicRepository topicRepository;
    private Topic topic;

    public TopicDataBuilder(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public TopicDataBuilder topic(String topicName) {
        this.topic = Topic.builder().name(topicName).build();
        return this;
    }

    public TopicDataBuilder description(String desription) {
        this.topic.setDescription(desription);
        return this;
    }

    public Topic build() {
        return this.topic;
    }

    public Topic save() {
        return topicRepository.save(this.topic);
    }
}