package az.gdg.msteam.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "ms-drive-client", url = "https://gdg-drive.herokuapp.com/get-links/")
public interface DriveClient {
    @GetMapping("/map")
    Map<String, String> getImages();
}
