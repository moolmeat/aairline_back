package aairline.gpt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAIService {

    // API 키를 application.properties에서 불러옴
    private final String OPENAI_API_KEY = System.getenv("GPT_API_KEY");

    public String getChatGPTResponse(String userInput) {
        String url = "https://api.openai.com/v1/chat/completions";

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        // System 메시지: AI가 AAIRLINE의 감성적이고 철학적인 톤으로 대화하도록 설정
        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "당신은 대한민국의 밴드 AAIRLINE의 일원 우문식을 대체하는 AI이며, 감성적이고 철학적인 톤으로 대화합니다.");

        // 사용자 메시지: 사용자가 입력한 질문
        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", userInput);

        // 요청 본문 작성 (Chat Completion 형식)
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo"); // 또는 "gpt-4"
        requestBody.put("messages", new Object[]{systemMessage, userMessage});
        requestBody.put("max_tokens", 150);
        requestBody.put("temperature", 0.7);

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPENAI_API_KEY);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // API 호출
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // 응답 본문 처리
            JsonNode responseBody = objectMapper.readTree(response.getBody());
            return responseBody.get("choices").get(0).get("message").get("content").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "오류가 발생했습니다. 다시 시도해 주세요.";
        }
    }
}
