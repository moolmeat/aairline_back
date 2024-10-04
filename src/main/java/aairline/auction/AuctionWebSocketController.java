package aairline.auction;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

@Controller
public class AuctionWebSocketController {

    private final AuctionService auctionService;

    public AuctionWebSocketController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    // 입찰 처리
    @MessageMapping("/bid/{auctionId}")
    @SendTo("/topic/auction/{auctionId}")
    public Auction processBid(@DestinationVariable Long auctionId,
        @AuthenticationPrincipal UserDetails userDetails,
        Bid bid) throws Exception {
        // 모든 로직은 서비스에서 처리하고, 유저 정보만 전달
        return auctionService.processBid(userDetails, auctionId, bid);
    }

    // 경매 시작 시 클라이언트에 전송
    @MessageMapping("/startAuction/{auctionId}")
    @SendTo("/topic/auction/{auctionId}")
    public Auction startAuction(@DestinationVariable Long auctionId,
        @AuthenticationPrincipal UserDetails userDetails) {
        // 경매 시작 처리 로직도 서비스로 넘김
        return auctionService.startAuction(userDetails, auctionId);
    }

    // 상품 등록 시 처리
    @MessageMapping("/registerItem")
    @SendTo("/topic/auctions")
    public Auction registerItem(@AuthenticationPrincipal UserDetails userDetails,
        AuctionItem auctionItem) {
        // 상품 등록 처리 로직도 서비스로 넘김
        return auctionService.registerItem(userDetails, auctionItem);
    }

    // 경매 종료 시 처리
    @MessageMapping("/endAuction/{auctionId}")
    @SendTo("/topic/auction/{auctionId}")
    public Auction endAuction(@DestinationVariable Long auctionId,
        @AuthenticationPrincipal UserDetails userDetails) {
        // 경매 종료 처리도 서비스로 넘김
        return auctionService.endAuction(userDetails, auctionId);
    }
}
