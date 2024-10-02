package aairline.common.auction;

import java.math.BigDecimal;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class AuctionWebSocketController {

    private final AuctionService auctionService;

    public AuctionWebSocketController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    // 입찰 처리
    @MessageMapping("/bid")
    @SendTo("/topic/auction/{auctionId}")
    public Auction processBid(@DestinationVariable Long auctionId, Bid bid, BigDecimal userBalance) throws Exception {
        return auctionService.processBid(auctionId, bid, userBalance);
    }

    // 경매 시작 시 클라이언트에 전송
    @MessageMapping("/startAuction/{auctionId}")
    @SendTo("/topic/auction/{auctionId}")
    public Auction startAuction(@DestinationVariable Long auctionId) {
        return auctionService.startAuction(auctionId);
    }
}
