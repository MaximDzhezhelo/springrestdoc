package smartjava;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.config.EnableHypermediaSupport;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;

@SpringBootApplication
@EnableHypermediaSupport(type = HAL)
public class DemoApplication {

    private static final String SPRING_HATEOAS_OBJECT_MAPPER = "_halObjectMapper";

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
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

}