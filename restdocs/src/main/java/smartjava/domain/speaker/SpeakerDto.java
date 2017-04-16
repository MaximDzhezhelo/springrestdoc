package smartjava.domain.speaker;


import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class SpeakerDto {

    @NotBlank
    private String name;

    @NotEmpty
    private String company;

    public Speaker createSpeaker() {
        return Speaker.builder().name(name)
                .company(company)
                .build();
    }
}