package aairline.auction.controller;

import aairline.auction.dto.BidRequestDto;
import aairline.auction.entity.Auction;
import aairline.auction.service.AuctionService;
import aairline.auction.entity.Bid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuctionWebSocketController {

    private final AuctionService auctionService;

    @Autowired
    public AuctionWebSocketController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @MessageMapping("/placeBid")
    @SendTo("/topic/auction/{auctionId}")
    public Auction processBid(@DestinationVariable Long auctionId, @AuthenticationPrincipal UserDetails userDetails, BidRequestDto bidRequestDto) throws Exception {
        Bid bid = new Bid();
        bid.setBidAmount(bidRequestDto.getBidAmount());
        return auctionService.processBid(userDetails, auctionId, bid);
    }

    @MessageMapping("/subscribeAuction")
    @SendTo("/topic/auction/{auctionId}")
    public Auction getAuction(@DestinationVariable Long auctionId) {
        // 경매 정보를 서버에서 가져와서 실시간으로 방송
        return auctionService.getAuctionById(auctionId);
    }
}

