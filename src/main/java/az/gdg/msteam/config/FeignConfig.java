package az.gdg.msteam.config;

import az.gdg.msteam.client.AuthenticationClient;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {AuthenticationClient.class})
public class FeignConfig {
}
