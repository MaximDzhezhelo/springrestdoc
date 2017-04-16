package smartjava;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpeakerControllerTest {

    @Autowired
    WebApplicationContext applicationContext;

    MockMvc mockMvc;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    @Before
    public void init() {
        mockMvc = webAppContextSetup(applicationContext)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void testAllSpeakers() throws Exception {
        //Given-When
        ResultActions actions = mockMvc.perform(get("/speakers/{id}", 1L))
                .andDo(print());
        actions.andExpect(jsonPath("$.name", CoreMatchers.is("Name1")))
                .andExpect(jsonPath("$.age", CoreMatchers.is("1")))
                .andExpect(jsonPath("$.company", CoreMatchers.is("company")));

        //Then
        actions.andDo(document("{class-name}/{method-name}",
                responseFields(fieldWithPath("name").description("Speakers name"),
                        fieldWithPath("age").description("Age"),
                        fieldWithPath("company").description("The company that speaker is working on."))));
    }

}