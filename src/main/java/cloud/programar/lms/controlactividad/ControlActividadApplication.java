package cloud.programar.lms.controlactividad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;

@SpringBootApplication
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ControlActividadApplication {

    @Bean
    public CurieProvider curieProvider() {
        return new DefaultCurieProvider("cl", new UriTemplate("http://programar.cloud/rels/{rel}"));
    }
    

    public static void main(String[] args) {
        SpringApplication.run(ControlActividadApplication.class, args);
    }
}
