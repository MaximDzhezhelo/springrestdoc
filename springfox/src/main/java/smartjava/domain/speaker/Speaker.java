package smartjava.domain.speaker;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;

import io.swagger.annotations.ApiModelProperty;

public class Speaker {
    @NotEmpty
    @ApiModelProperty(notes = "Model Name upper case without spaces", required = true)
    String name;

    @Max(value = 100)
    @ApiModelProperty(notes = "The age of person must be not more than 100", required = true)
    String age;

    @NotEmpty
    String company;

    public Speaker(String name, String age, String firm) {
        this.name = name;
        this.age = age;
        this.company = firm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}