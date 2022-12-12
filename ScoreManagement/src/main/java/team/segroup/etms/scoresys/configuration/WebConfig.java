package team.segroup.etms.scoresys.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.format.FormatterRegistry;
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
            .addPathPatterns("/**");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(Jsr310Converters.StringToLocalDateTimeConverter.INSTANCE);
    }
}
