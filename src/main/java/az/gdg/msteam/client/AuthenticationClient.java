package az.gdg.msteam.client;

import static az.gdg.msteam.model.client.auth.HttpHeader.X_AUTH_TOKEN;

import az.gdg.msteam.model.client.auth.UserInfo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "ms-auth-client", url = "https://ms-gdg-auth.herokuapp.com")
public interface AuthenticationClient {
    @PostMapping("/auth/validate")
    UserInfo validateToken(@RequestHeader(X_AUTH_TOKEN) String token);
}
