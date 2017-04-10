package smartjava;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Predicates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.config.EnableHypermediaSupport;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;
import static springfox.documentation.builders.PathSelectors.ant;

@SpringBootApplication
@EnableSwagger2
@EnableHypermediaSupport(type = HAL)
public class SpringFoxApplication {

	private static final String SPRING_HATEOAS_OBJECT_MAPPER = "_halObjectMapper";

	public static void main(String[] args) {
		SpringApplication.run(SpringFoxApplication.class, args);
	}

	@Autowired
	@Qualifier(SPRING_HATEOAS_OBJECT_MAPPER)
	private ObjectMapper springHateoasObjectMapper;

	@Primary
	@Bean(name = "objectMapper")
	ObjectMapper objectMapper() {
		springHateoasObjectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		springHateoasObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		springHateoasObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		return springHateoasObjectMapper;
	}

	@Bean
	public Docket restApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.groupName("Module API")
				.select()
//				.paths(any())
//	Otherwise we are including springboot actuator endpoints
				.paths(Predicates.and(ant("/**"),
						Predicates.not(ant("/error")),
						Predicates.not(ant("/management/**")),
						Predicates.not(ant("/management*"))))
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Swagger Speakers")
				.description("Speakers API Description")
				.contact(new Contact("Tsypuk Roman", "https://tsypuk.github.io/springrestdoc", "tsypuk.conf@gmail.com"))
				.license("Apache 2.0")
				.licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
				.version("1.0.0")
				.build();
	}
}