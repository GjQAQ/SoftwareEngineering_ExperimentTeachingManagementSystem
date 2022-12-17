package team.segroup.etms.filesys.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
@EnableWebMvc
public class SwaggerConfig {
    @Bean
    public Docket createApi() {
        return new Docket(DocumentationType.OAS_30)
            .apiInfo(addApiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("team.segroup.etms.filesys"))
            .build();
    }

    private ApiInfo addApiInfo() {
        return new ApiInfoBuilder()
            .title("User Management Module")
            .description("API Doc")
            .version("0.1")
            .contact(new Contact(
                "Group One",
                "https://120.78.65.145",
                "group@tongji.edu.cn"))
            .build();
    }
}