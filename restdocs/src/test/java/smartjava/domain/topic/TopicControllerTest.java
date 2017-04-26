package smartjava.domain.topic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import smartjava.TestDataGenerator;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(value = "build/generated-snippets")
public class TopicControllerTest {

    @Autowired
    TestDataGenerator given;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testGetTopic() throws Exception {
        // Given
        Topic topic = given.topic("SpringRestDocs").description("Test driven documentation").save();

        // When
        ResultActions actions = mockMvc.perform(get("/topics/{id}", topic.getId()))
                .andDo(print());

        // Then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(topic.getName())))
                .andExpect(jsonPath("$.description", is(topic.getDescription())))
                .andDo(document("{class-name}/{method-name}",
                        responseFields(
                                fieldWithPath("name").description("The name of the Topic."),
                                fieldWithPath("description").description("Desc."),
                                fieldWithPath("_links").description("HATEOAS links")),
                        links(halLinks(),
                                linkWithRel("self").description("The link to self resource"))));
    }
}