package aairline.common.auction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Bid {
    private Long userId;
    private BigDecimal bidAmount;
    private LocalDateTime bidTime;
}