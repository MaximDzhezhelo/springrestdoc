package smartjava.domain.speaker;


import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class SpeakerDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String company;

    public Speaker createSpeaker() {
        return Speaker.builder()
                .company(this.company)
                .name(this.name)
                .build();
    }

}