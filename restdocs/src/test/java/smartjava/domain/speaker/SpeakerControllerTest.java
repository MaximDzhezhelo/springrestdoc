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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import smartjava.ConstrainedFields;
import smartjava.TestDataGenerator;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
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
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpeakerControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestDataGenerator given;

    @Autowired
    private SpeakerRepository speakerRepository;

    private MockMvc mockMvc;

    public static final String HAL_JSON_CHARSET_UTF_8 = "application/hal+json;charset=UTF-8";

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation).uris()
                        .withScheme("https")
                        .withHost("mydomain.com"))
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
        ResultActions action = mockMvc.perform(
                get("/speakers/{id}", josh.getId()).accept(MediaTypes.HAL_JSON));

        //Then
        action.andDo(print()).andExpect(status().isOk());
        action.andExpect(jsonPath("$.name", is(josh.getName())))
                .andExpect(jsonPath("$.company", is(josh.getCompany())));

        action.andDo(
                document("{class-name}/{method-name}",
                        responseFields(
                                fieldWithPath("name").description("Speakers name."),
                                fieldWithPath("company").description("The company where speaker is working on."),
                                fieldWithPath("_links").description("Link section."),
                                fieldWithPath("_links.self.href").description("Link to self section.")
                        ),
                        links(halLinks(), linkWithRel("self").description("Link to self section."))));
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
                        fieldWithPath("_embedded.speakers[]._links").description("Link section."),
                        fieldWithPath("_embedded.speakers[]._links.self.href").description("Link to self section.")
                )));
    }

    @Test
    public void testDeleteSpeaker() throws Exception {

    }

    @Test
    public void testCreateSpeaker_created() throws Exception {
        // Given
        SpeakerDto requestDto = given.speakerDto("Josh Long").company("Pivotal").build();
        String requestBody = given.asJsonString(requestDto);

        // When
        ResultActions action = mockMvc.perform(post("/speakers")
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
    public void testCreateSpeaker_created2() throws Exception {
        // Given
        SpeakerDto requestDto = given.speakerDto("Josh Long").company("Pivotal").build();
        String requestBody = given.asJsonString(requestDto);

        // When
        ResultActions action = mockMvc.perform(post("/speakers")
                .content(requestBody))
                .andDo(print());

        // Then
        assertEquals(1, speakerRepository.count());
        Speaker savedSpeaker = speakerRepository.findByName("Josh Long").get();

        assertEquals(requestDto.getName(), savedSpeaker.getName());
        assertEquals(requestDto.getCompany(), savedSpeaker.getCompany());

        action.andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/speakers/" + savedSpeaker.getId())));

        action.andDo(document("{class-name}/{method-name}",
                requestFields(
                        attributes(key("title").value("Request"), key("constraints").value("Request")),
                        fieldWithPath("name").description("DDD"),
                        fieldWithPath("company").description("COM")
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
                        fieldWithPath("content").description("Errors that was found during validation.")
                )));
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
                .andExpect(jsonPath("$._embedded.validationErrors[0].message", is("may not be null")))
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

}