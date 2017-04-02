package smartjava.domain.speaker;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpeakerRepository extends JpaRepository<Speaker, Long> {
    Optional<Speaker> findOne(long id);

    Optional<Speaker> findByName(String name);
}