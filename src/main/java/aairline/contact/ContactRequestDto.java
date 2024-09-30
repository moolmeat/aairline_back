package aairline.contact;

import lombok.Data;

@Data
public class ContactRequestDto {

    private String name;

    private String email;

    private String category;

    private String content;
}
