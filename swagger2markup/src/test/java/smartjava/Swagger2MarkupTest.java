package smartjava;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.github.swagger2markup.GroupBy;
import io.github.swagger2markup.Language;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureRestDocs(outputDir = "build/asciidoc/snippets")
@SpringBootTest(classes = {Swagger2MarkupApplication.class, SwaggerConfig.class})
@AutoConfigureMockMvc
public class Swagger2MarkupTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createSpringfoxSwaggerJson() throws Exception {
        String outputDir = System.getProperty("io.springfox.staticdocs.outputDir");
        MvcResult mvcResult = this.mockMvc.perform(get("/v2/api-docs")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        String swaggerJson = response.getContentAsString();
        Files.createDirectories(Paths.get(outputDir));
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputDir, "swagger.json"),
                StandardCharsets.UTF_8)) {
            writer.write(swaggerJson);
        }
    }

    @Test
    public void testAsciiDoc() throws IOException, URISyntaxException {
        //Given
        Path file = Paths.get(Swagger2MarkupTest.class.getResource("/yaml/swagger.yaml").toURI());
        Path outputDirectory = Paths.get("build/test/asciidoc/to_folder");
        FileUtils.deleteQuietly(outputDirectory.toFile());

        //When
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withPathsGroupedBy(GroupBy.TAGS)
                .withOutputLanguage(Language.EN)
                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
                .build();

        Swagger2MarkupConverter.from(file)
                .withConfig(config)
                .build()
                .toFolder(outputDirectory);
    }

    @Test
    public void testMarkup() throws IOException, URISyntaxException {
        //Given
        Path file = Paths.get(Swagger2MarkupTest.class.getResource("/yaml/swagger.yaml").toURI());
        Path outputDirectory = Paths.get("build/test/markdown/to_folder");
        FileUtils.deleteQuietly(outputDirectory.toFile());

        //When
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withPathsGroupedBy(GroupBy.TAGS)
                .withOutputLanguage(Language.EN)
                .withMarkupLanguage(MarkupLanguage.MARKDOWN)
                .build();

        Swagger2MarkupConverter.from(file)
                .withConfig(config)
                .build()
                .toFolder(outputDirectory);
    }

    @Test
    public void testConfluence() throws IOException, URISyntaxException {
        //Given
        Path file = Paths.get(Swagger2MarkupTest.class.getResource("/yaml/swagger.yaml").toURI());
        Path outputDirectory = Paths.get("build/test/confluence/to_folder");
        FileUtils.deleteQuietly(outputDirectory.toFile());

        //When
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withPathsGroupedBy(GroupBy.TAGS)
                .withOutputLanguage(Language.EN)
                .withMarkupLanguage(MarkupLanguage.CONFLUENCE_MARKUP)
                .build();

        Swagger2MarkupConverter.from(file)
                .withConfig(config)
                .build()
                .toFolder(outputDirectory);
    }
}