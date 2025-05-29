package knu.oceanbackend.external;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class AiServerClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendImageToAi(MultipartFile image, Long userId, Long postId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new MultipartInputStreamFileResource(image.getBytes(), image.getOriginalFilename()));
            body.add("user_id", userId.toString());

            if (postId != null) {
                body.add("post_id", postId.toString());
            }

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            String aiServerUrl = "http://localhost:8001/ai";

            restTemplate.postForEntity(aiServerUrl, requestEntity, String.class);

        } catch (IOException e) {
            throw new RuntimeException("AI 서버 전송 실패", e);
        }
    }
}
