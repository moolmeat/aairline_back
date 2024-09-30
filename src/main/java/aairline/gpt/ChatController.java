package aairline.gpt;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private OpenAIService openAIService;

    @PostMapping
    public String getChatResponse(@RequestBody String userInput) {
        return openAIService.getChatGPTResponse(userInput);
    }
}
