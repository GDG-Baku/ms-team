package az.gdg.msteam.config;

import az.gdg.msteam.client.AuthenticationClient;
import az.gdg.msteam.client.DriveClient;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {AuthenticationClient.class, DriveClient.class})
public class FeignConfig {
}
