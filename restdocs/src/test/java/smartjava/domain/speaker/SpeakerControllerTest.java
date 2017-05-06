package smartjava.domain.speaker;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import smartjava.ConstrainedFields;
import smartjava.TestDataGenerator;
import smartjava.domain.topic.Topic;
import smartjava.domain.topic.TopicRepository;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.options;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
//@Ignore
public class SpeakerControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestDataGenerator given;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private TopicRepository topicRepository;

    private MockMvc mockMvc;

    public static final String HAL_JSON_CHARSET_UTF_8 = "application/hal+json;charset=UTF-8";

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    @Before
    public void init() {
        mockMvc = webAppContextSetup(context)
                .apply(documentationConfiguration(this.restDocumentation)
                        .uris()
                        .withScheme("https")
                        .withHost("mydomain.com")
                )
                .defaultRequest(options("/")
                        .accept(HAL_JSON_CHARSET_UTF_8)
                        .contentType(HAL_JSON_CHARSET_UTF_8))
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
        Speaker noiseData = given.speaker("Noise").company("Noise").save();

        //When
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.get("/speakers/{id}", josh.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Josh Long")))
                .andExpect(jsonPath("$.company", is("Pivotal")))
                .andDo(print());

//        actions.andDo(document("{class-name}/{method-name}"));
        actions.andDo(document("{class-name}/{method-name}",
                responseFields(
                        fieldWithPath("name").description("Speaker's name"),
                        fieldWithPath("status").description("Speaker's name"),
                        fieldWithPath("company").description("Speaker's name"),
                        subsectionWithPath("_links").description("<<resources-tags-links, Links>> to Speakers HATEOAS")
                ),
                links(halLinks(),
                        linkWithRel("self").description("Link to self"),
                        linkWithRel("topics").description("Link to speaker's topics.")),
                pathParameters(
                        parameterWithName("id").description("Required identifier of Speaker")
                )
        ));
    }

    @Test
    public void testGetAllSpeakers() throws Exception {
        //Given
        Speaker josh = given.speaker("Josh Long").company("Pivotal").save();
        Speaker venkat = given.speaker("Venkat Subramaniam").company("Agile").save();

        //When
        ResultActions action = mockMvc.perform(
                get("/speakers").accept(MediaTypes.HAL_JSON));

        //Then
        action.andDo(print()).andExpect(status().isOk());
        action.andDo(document("{class-name}/{method-name}",
                responseFields(
                        fieldWithPath("_embedded").description("'speakers' array with Speakers resources."),
                        fieldWithPath("_embedded.speakers").description("Array with returned Speaker resources."),
                        fieldWithPath("_embedded.speakers[].name").description("Speaker's name."),
                        fieldWithPath("_embedded.speakers[].company").description("Speaker's company."),
                        fieldWithPath("_embedded.speakers[].status").description("Speaker's status name."),
                        fieldWithPath("_embedded.speakers[]._links").description("Link section."),
                        subsectionWithPath("_embedded.speakers[]._links").description("<<resources-tags-links, " +
                                "HATEOAS Links>> to Speaker actions")
                )

        ));
    }

    @Test
    public void testDeleteSpeaker() throws Exception {
        //Given
        Speaker speakerToDelete = given.speaker("TO_DELETE").company("COMPANY").save();

        //When
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.delete("/speakers/{id}", speakerToDelete.getId()))
                .andDo(print());

        //Then
        actions.andExpect(status().isNoContent());
        actions.andDo(document("{class-name}/{method-name}"));
        assertEquals(0, speakerRepository.count());
    }

    @Test
    public void testCreateSpeaker_created() throws Exception {
        // Given
        SpeakerDto requestDto = given.speakerDto("Josh Long").company("Pivotal").build();
        String requestBody = given.asJsonString(requestDto);

        // When
        ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders.post("/speakers")
                .content(requestBody))
                .andDo(print());

        // Then
        assertEquals(1, speakerRepository.count());
        Speaker savedSpeaker = speakerRepository.findByName("Josh Long").get();

        assertEquals(requestDto.getName(), savedSpeaker.getName());
        assertEquals(requestDto.getCompany(), savedSpeaker.getCompany());

        action.andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/speakers/" + savedSpeaker.getId())));

        ConstrainedFields constrainedFields = new ConstrainedFields(SpeakerDto.class);

        action.andDo(document("{class-name}/{method-name}",
                requestFields(
                        attributes(key("title").value("SpeakerDTO Constrains")),
                        constrainedFields.name("name").description("The speaker's name."),
                        constrainedFields.name("company").description("The company speaker is working on.")
                ),
                responseHeaders(
                        headerWithName("Location").description("URI path to created resource."))));
    }

    @Test
    public void testCreateSpeaker_conflict() throws Exception {
        //Given
        Speaker josh = given.speaker("Josh Long").company("Pivotal").save();
        String requestBody = given.asJsonString(given.speakerDto("Josh Long").company("Pivotal").build());

        //When
        ResultActions action = mockMvc.perform(post("/speakers").content(requestBody));
        action.andDo(print());

        //Then
        assertEquals(1, speakerRepository.count());
        action.andExpect(status().isConflict());
        action.andExpect(jsonPath("content", is("Entity  Speaker with name 'Josh Long' already present in DB.")));
        action.andDo(document("{class-name}/{method-name}",
                responseFields(
                        fieldWithPath("content").description("Errors that were found during validation."))));
    }

    @Test
    public void testCreateSpeaker_validationFails() throws Exception {
        //Given
        Speaker josh = given.speaker("Josh Long").company("Pivotal").save();
        String requestBody = given.asJsonString(given.speakerDto("Josh Long").build());

        //When
        ResultActions action = mockMvc.perform(post("/speakers").content(requestBody));
        action.andDo(print());

        //Then
        assertEquals(1, speakerRepository.count());
        action.andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$._embedded.length()", is(1)))
                .andExpect(jsonPath("$._embedded.validationErrors[0].property", is("company")))
                .andExpect(jsonPath("$._embedded.validationErrors[0].message", is("may not be empty")))
                .andExpect(jsonPath("$._embedded.validationErrors[0].invalidValue", is("null")));

        action.andDo(document("{class-name}/{method-name}",
                responseFields(
                        fieldWithPath("_embedded.validationErrors").description("Errors that were found during " +
                                "validation."),
                        fieldWithPath("_embedded.validationErrors[].property").description("Invalid property name of " +
                                "posted json entity."),
                        fieldWithPath("_embedded.validationErrors[].message").description("The message, extracted " +
                                "from validation provider exception."),
                        fieldWithPath("_embedded.validationErrors[].invalidValue").description("Invalid value that " +
                                "had not passed validation"))));
    }

    @Test
    public void testGetSpeakerTopics() throws Exception {
        // Given
        Topic[] topics = {given.topic("Java9").description("Comming soon.").build(),
                given.topic("Spring").description("Pivotal").build()};

        Speaker savedSpeaker = given.speaker("SpeakerName").company("Company")
                .topics(topics).save();

        // When
        ResultActions actions = mockMvc.perform(get("/speakers/{id}/topics", savedSpeaker.getId()))
                .andDo(print());

        // Then
        assertEquals(1, speakerRepository.count());
        assertEquals(2, topicRepository.count());
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.topics.length()", is(topics.length)))
                .andExpect(jsonPath("$._embedded.topics[1].name").value(is("Spring")))
                .andExpect(jsonPath("$._embedded.topics[*].name",
                        containsInAnyOrder("Spring", "Java9")))
                .andExpect(jsonPath("$._embedded.topics[*].description",
                        containsInAnyOrder("Comming soon.", "Pivotal"))
                );
        actions.andDo(document("{class-name}/{method-name}",
                responseFields(
                        fieldWithPath("_embedded").description("'topics' array with Topic resources"),
                        fieldWithPath("_embedded.topics").description("Array of topics that are associated with " +
                                "speaker"),
                        fieldWithPath("_embedded.topics[].name").description("Topic name"),
                        fieldWithPath("_embedded.topics[].description").description("Topic description"),
                        fieldWithPath("_embedded.topics[]._links").description("Link section"),
                        subsectionWithPath("_embedded.topics[]._links").description("HATEOAS links")

                )));
    }

}