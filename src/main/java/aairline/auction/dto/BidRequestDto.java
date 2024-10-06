package aairline.auction.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BidRequestDto {
    private Long auctionId;
    private BigDecimal bidAmount;
}
