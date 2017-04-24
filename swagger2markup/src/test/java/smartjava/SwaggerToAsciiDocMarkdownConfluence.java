package smartjava;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import io.github.swagger2markup.GroupBy;
import io.github.swagger2markup.Language;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;

import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SwaggerToAsciiDocMarkdownConfluence {

    private static final String BUILD_SWAGGER_2_MARKUP_PATH = "build/swagger2markup/";
    private String resource;
    private String outputPath;
    private MarkupLanguage markupLanguage;

    public SwaggerToAsciiDocMarkdownConfluence(String resource, String outputPath, MarkupLanguage markupLanguage) {
        this.resource = resource;
        this.outputPath = outputPath;
        this.markupLanguage = markupLanguage;
    }

    @Parameters
    public static Collection<Object[]> testParams() {
        return Arrays.asList(new Object[][]{
                {"/swagger.yaml", "yaml/asciidoc", MarkupLanguage.ASCIIDOC},
                {"/swagger.yaml", "yaml/markdown", MarkupLanguage.MARKDOWN},
                {"/swagger.yaml", "yaml/confluence", MarkupLanguage.CONFLUENCE_MARKUP},

                {"/swagger.json", "json/asciidoc", MarkupLanguage.ASCIIDOC},
                {"/swagger.json", "json/markdown", MarkupLanguage.MARKDOWN},
                {"/swagger.json", "json/confluence", MarkupLanguage.CONFLUENCE_MARKUP}
        });
    }

    @Test
    public void test() throws URISyntaxException {
        //Given
        Path file = Paths.get(SwaggerToAsciiDocMarkdownConfluence.class.getResource(resource).toURI());
        Path outputDirectory = Paths.get(BUILD_SWAGGER_2_MARKUP_PATH + outputPath);
        FileUtils.deleteQuietly(outputDirectory.toFile());

        //When
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withPathsGroupedBy(GroupBy.TAGS)
                .withOutputLanguage(Language.EN)
                .withMarkupLanguage(markupLanguage)
                .build();

        Swagger2MarkupConverter.from(file)
                .withConfig(config)
                .build()
                .toFolder(outputDirectory);
    }

}