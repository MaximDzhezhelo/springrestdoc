package smartjava.domain.speaker;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import smartjava.TestDataGenerator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .defaultRequest(options("/")
                        .accept(MediaTypes.HAL_JSON)
                        .contentType("application/hal+json;charset=UTF-8"))
                .build();
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
        ResultActions actions = mockMvc.perform(get("/speakers/{id}", josh.getId()))
                .andDo(print());

        //Then
        actions.andExpect(jsonPath("$.name", is("Josh Long")));
        actions.andExpect(jsonPath("$.status", is("I like Spring & Rest Docs.")));
        actions.andExpect(jsonPath("$.company", is("Pivotal")));
    }

    @Test
    public void testGetSpeakers() throws Exception {
        //Given
        Speaker josh = given.speaker("Josh Long").company("Pivotal").save();
        Speaker venkat = given.speaker("Venkat Subramaniam").company("Agile").save();

        //When
        ResultActions action = mockMvc.perform(get("/speakers").accept(HAL_JSON));

        //Then
        action.andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void testDeleteSpeaker() throws Exception {
        //Given
        Speaker speakerToDelete = given.speaker("Speaker_to_delete").company("none").save();
        Speaker noiseRecord = given.speaker("SP").company("SP").save();

        //When
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.delete("/speakers/{id}",
                speakerToDelete.getId()));

        //Then
        actions.andDo(print()).andExpect(status().isNoContent());
        assertEquals(1, speakerRepository.count());
        assertEquals(noiseRecord.getId(), speakerRepository.findByName("SP").get().getId());
    }

    @Test
    public void testUpdateSpeaker() throws Exception {
        // Given
        Speaker speakerToUpdate = given.speaker("Speaker_to_update").company("Company_to_update").save();
        String requestBody = given.asJsonString(given.speakerDto("Updated Name").company("Updated Company").build());

        // When
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.put("/speakers/{id}", speakerToUpdate.getId())
                .content(requestBody)
                .contentType(MediaTypes.HAL_JSON));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk());

        assertEquals(1, speakerRepository.count());
        Speaker speaker = speakerRepository.findOne(speakerToUpdate.getId()).get();
        assertThat("Updated Company", is(speaker.getCompany()));
        assertThat("Updated Name", is(speaker.getName()));
    }

}