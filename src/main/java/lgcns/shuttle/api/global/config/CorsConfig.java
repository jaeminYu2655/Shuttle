package lgcns.shuttle.api.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3001", "http://localhost:3000")
                .allowedMethods("GET","POST","OPTIONS")
                .allowedHeaders("*")    // 클라이언트가 서버에게 허용하는 헤더를 지정하는 옵션
                .exposedHeaders("authorization", "refreshtoken")   // 서버에서 클라이언트 응답할때 노출할 헤더
                .allowCredentials(true)
                .maxAge(3600);
    }
}
