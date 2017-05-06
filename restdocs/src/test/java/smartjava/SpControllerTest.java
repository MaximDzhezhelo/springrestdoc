package smartjava;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import smartjava.domain.speaker.Speaker;
import smartjava.domain.speaker.SpeakerRepository;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
public class SpControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SpeakerRepository speakerRepository;

    @After
    public void tearDown() {
        speakerRepository.deleteAll();
    }

    @Test
    public void testGetSpeaker() throws Exception {
        // Given
        Speaker josh = Speaker.builder().name("Josh").company("pivotal").build();
        Speaker noise = Speaker.builder().name("Noise").company("Noise").build();
        speakerRepository.save(josh);
        speakerRepository.save(noise);


        // When
        ResultActions actions = mockMvc.perform(get("/speakers/{id}", josh.getId()))
                .andDo(print());

        // Then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Josh")))
                .andExpect(jsonPath("$.company", is("pivotal")));

        actions.andDo(document("{class-name}/{method-name}",
                responseFields(
                        fieldWithPath("name").description("The speakers name"),
                        fieldWithPath("company").description("The speaker's company"),
                        fieldWithPath("status").description("Only good statuses :)"),
                        fieldWithPath("_links").description("HATEOAS links")
                ),
                links(halLinks(),
                        linkWithRel("self").description("The link to Speaker resource"),
                        linkWithRel("topics").description("The link to Speaker's topics")
                        )
                ));
    }
}