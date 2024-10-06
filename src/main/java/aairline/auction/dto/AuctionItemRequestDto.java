package aairline.auction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AuctionItemRequestDto {
    private String itemName;
    private BigDecimal startingPrice;
    private String description;
    private String category;
    private String seller;
    private LocalDateTime startTime;
    private LocalDateTime limitTime;
}
