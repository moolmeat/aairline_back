package aairline.common.auction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Auction {
    private Long id;
    private String itemName;
    private BigDecimal startPrice;
    private BigDecimal currentPrice;
    private Long highestBidderId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}