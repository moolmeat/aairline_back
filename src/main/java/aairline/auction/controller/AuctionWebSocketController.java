package aairline.auction.controller;

import aairline.auction.dto.BidRequestDto;
import aairline.auction.entity.Auction;
import aairline.auction.entity.AuctionItem;
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

    @MessageMapping("/bid/{auctionId}")
    @SendTo("/topic/auction/{auctionId}")
    public Auction processBid(@DestinationVariable Long auctionId,
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody BidRequestDto bidRequestDto) throws Exception {
        Bid bid = new Bid();
        bid.setBidAmount(bidRequestDto.getBidAmount());
        return auctionService.processBid(userDetails, auctionId, bid);
    }

    @MessageMapping("/startAuction/{auctionId}")
    @SendTo("/topic/auction/{auctionId}")
    public Auction startAuction(@DestinationVariable Long auctionId,
        @AuthenticationPrincipal UserDetails userDetails) {
        return auctionService.startAuction(userDetails, auctionId);
    }

    @MessageMapping("/endAuction/{auctionId}")
    @SendTo("/topic/auction/{auctionId}")
    public Auction endAuction(@DestinationVariable Long auctionId,
        @AuthenticationPrincipal UserDetails userDetails) {
        return auctionService.endAuction(userDetails, auctionId);
    }
}
