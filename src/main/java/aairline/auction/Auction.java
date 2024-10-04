package aairline.auction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Data
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private AuctionItem auctionItem;

    private BigDecimal startPrice;
    private BigDecimal currentPrice;
    private Long highestBidderId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}