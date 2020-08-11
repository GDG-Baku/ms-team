package az.gdg.msteam.config;

import az.gdg.msteam.client.AuthenticationClient;
import az.gdg.msteam.client.StorageClient;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {AuthenticationClient.class, StorageClient.class})
public class FeignConfig {
}
