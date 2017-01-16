package cloud.programar.lms.controlactividad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ControlActividadApplication {


    public static void main(String[] args) {
        SpringApplication.run(ControlActividadApplication.class, args);
    }
}
