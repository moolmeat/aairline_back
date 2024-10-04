package aairline.auction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.Data;

@Entity
@Data
public class AuctionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private BigDecimal startingPrice;
    private String description;
    private String category;
    private String seller;

    public AuctionItem() {
    }

    public AuctionItem(Long id, String itemName, BigDecimal startingPrice, String description, String category, String seller) {
        this.id = id;
        this.itemName = itemName;
        this.startingPrice = startingPrice;
        this.description = description;
        this.category = category;
        this.seller = seller;
    }
}