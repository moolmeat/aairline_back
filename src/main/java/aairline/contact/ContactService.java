package aairline.contact;

import aairline.common.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    @Transactional
    public CustomResponse<Void> sendContact(ContactRequestDto contactRequestDto) {
        Contact contact = new Contact(contactRequestDto.getName(), contactRequestDto.getEmail(),
            contactRequestDto.getCategory(), contactRequestDto.getContent());
        contactRepository.save(contact);
        return CustomResponse.success("contact 생성에 성공하였습니다.", null, 201);
    }
}
