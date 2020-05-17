package az.gdg.msteam.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "ms-storage-client", url = "${client.service.url.ms-storage}")
public interface DriveClient {
    @GetMapping
    Map<String, String> getImages();
}
