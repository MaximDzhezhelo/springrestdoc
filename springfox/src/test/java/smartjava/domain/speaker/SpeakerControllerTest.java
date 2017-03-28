package smartjava.domain.speaker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import smartjava.TestDataGenerator;

import static org.junit.Assert.*;

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
        ResultActions action = mockMvc.perform(
                MockMvcRequestBuilders.get("/speakers/{id}", josh.getId()).accept(MediaTypes.HAL_JSON));

        //Then
        action.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testAllSpeakers() throws Exception {
        //Given
        Speaker josh = given.speaker("Josh Long").company("Pivotal").save();
        Speaker venkat = given.speaker("Venkat Subramaniam").company("Agile").save();

        //When
        ResultActions action = mockMvc.perform(
                MockMvcRequestBuilders.get("/speakers").accept(MediaTypes.HAL_JSON));

        //Then
        action.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteSpeaker() throws Exception {

    }

}