package team.segroup.etms.coursesys.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.segroup.etms.interceptor.Authenticator;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final Authenticator authenticator = new Authenticator();

    @Bean
    public Authenticator getAuthenticator() {
        return authenticator;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAuthenticator())
            .addPathPatterns("/**")
            .excludePathPatterns("/actuator/**");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(Jsr310Converters.StringToLocalDateConverter.INSTANCE);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowedOrigins("*")
            .allowCredentials(true);
    }
}
