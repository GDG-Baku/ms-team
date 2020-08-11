package az.gdg.msteam.client;

import az.gdg.msteam.model.client.auth.UserInfo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static az.gdg.msteam.model.client.auth.HttpHeader.X_AUTH_TOKEN;

@FeignClient(value = "ms-auth-client", url = "${client.service.url.ms-auth}")
public interface AuthenticationClient {
    @PostMapping
    UserInfo validateToken(@RequestHeader(X_AUTH_TOKEN) String token);
}
