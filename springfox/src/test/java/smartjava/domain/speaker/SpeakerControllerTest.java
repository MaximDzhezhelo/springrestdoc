package smartjava.domain.speaker;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import smartjava.TestDataGenerator;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpeakerControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private TestDataGenerator given;

    @Autowired
    private SpeakerRepository speakerRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @After
    public void tearDown() {
        speakerRepository.deleteAll();
    }

    @Test
    public void testGetSpeaker() throws Exception {
        //Given
        Speaker josh = given.speaker("Josh Long").company("Pivotal").save();
        Speaker dummyRecord = given.speaker("NONE").company("NONE").save();

        //When
//        ResultActions action = mockMvc.perform(
//                get("/speakers/{id}", josh.getId()).accept(MediaTypes.HAL_JSON));
//
        //Then
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/speakers/{id}", josh.getId())
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.name", is("Josh Long")));

        actions.andExpect(jsonPath("$.status", is("I like Spring & Rest Docs.")));

//        action.andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//        action.andExpect(jsonPath("$.status", is("I like Spring & Rest Docs.")));
//        action.andExpect(jsonPath("$.name", is("Josh Long")));
//        action.andExpect(jsonPath("$.company", is("Pivotal")));
    }

    @Test
    public void testAllSpeakers() throws Exception {
        //Given
        Speaker josh = given.speaker("Josh Long").company("Pivotal").save();
        Speaker venkat = given.speaker("Venkat Subramaniam").company("Agile").save();

        //When
        ResultActions action = mockMvc.perform(
                get("/speakers").accept(HAL_JSON));

        //Then
        action.andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void testDeleteSpeaker() throws Exception {
        Speaker speakerToDelete = given.speaker("Speaker_to_delete").company("none").save();
        Speaker noiseRecord = given.speaker("SP").company("SP").save();
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.delete("/speakers/{id}", speakerToDelete.getId
                ()));
        actions.andDo(print()).andExpect(status().isOk());

        assertEquals(1, speakerRepository.count());
        assertEquals(noiseRecord.getId(), speakerRepository.findByName("SP").get().getId());

    }

}