package aairline.contact;

import aairline.common.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public CustomResponse<Void> sendContact(@RequestBody ContactRequestDto contactRequestDto) {
        return contactService.sendContact(contactRequestDto);
    }

}
