package ma.projet.ihk.suiviprojet_ihk.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Système de Suivi de Projets")
                        .description("Application de gestion et suivi de projets, phases, employés et facturation")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Équipe IHK")
                                .email("contact@ihk.ma")
                                .url("https://www.ihk.ma"))
                        .license(new License()
                                .name("Licence IHK")
                                .url("https://www.ihk.ma/licence")));
    }
}