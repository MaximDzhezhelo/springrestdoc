package smartjava.domain.speaker;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.ModelResultMatchers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class SpControllerTest {

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private MockMvc mockMvc;

    @After
    public void tearDown() {
        speakerRepository.deleteAll();
    }

    @Test
    public void testGetSpeaker() throws Exception {
        // Given
        Speaker noise = Speaker.builder().name("Noise").company("Noise").build();
        Speaker josh = Speaker.builder().name("Josh").company("pivotal").build();
        speakerRepository.save(noise);
        speakerRepository.save(josh);

        // When
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/speakers/{id}", josh.getId()))
                .andDo(MockMvcResultHandlers.print());

        // Then
        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Josh")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.company", is("pivotal")));

    }
}
