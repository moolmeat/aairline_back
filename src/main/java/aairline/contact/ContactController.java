package aairline.contact;

import aairline.common.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public CustomResponse<Void> sendContact(ContactRequestDto contactRequestDto) {
        return contactService.sendContact(contactRequestDto);
    }


}
