package com.cagan.walletapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Wallet API")
                        .description("Wallet API management REST API")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Cagan")
                                .email("cagan@example.com")
                                .url("https://github.com/cagan"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Yerel Geliştirme Sunucusu"),
                        new Server()
                                .url("https://api.example.com")
                                .description("Prodüksiyon Sunucusu")
                ));
    }
}