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

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
public class SpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpeakerRepository speakerRepository;

    @After
    public void tearDown() {
        speakerRepository.deleteAll();
    }

    @Test
    public void testGetSpeaker() throws Exception {
        // Given
        Speaker josh = Speaker.builder().name("Josh Long").company("Pivotal").build();
        speakerRepository.save(josh);

        // When
        ResultActions actions = mockMvc.perform(get("/speakers/{id}", josh.getId()))
                .andDo(print());

        // Then
        actions.andExpect(jsonPath("$.name", is("Josh Long")))
                .andExpect(jsonPath("$.company", is("Pivotal")));

        // Document
        actions.andDo(document("{class-name}/{method-name}",
                responseFields(
                        fieldWithPath("name").description("The name of the speaker"),
                        fieldWithPath("company").description("The company speaker is working on"),
                        fieldWithPath("status").description("Hope only good one"),
                        subsectionWithPath("_links").description("HATEOAS links")
                ),
                links(
                        linkWithRel("self").description("Link to Speaker"),
                        linkWithRel("topics").description("The topics speaker is working with")
                )
                ));

    }

}