package aairline.common.auction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class AuctionService {

    private final Map<Long, Auction> auctionMap = new ConcurrentHashMap<>();

    public Auction processBid(Long auctionId, Bid bid) {
        Auction auction = auctionMap.get(auctionId);
        if (auction == null || auction.getEndTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Auction has ended or does not exist.");
        }

        if (bid.getBidAmount().compareTo(auction.getCurrentPrice()) > 0) {
            auction.setCurrentPrice(bid.getBidAmount());
            auction.setHighestBidderId(bid.getUserId());
            auctionMap.put(auctionId, auction);
        }

        return auction;
    }

    public Auction startAuction(Long auctionId) {
        Auction auction = new Auction();
        auction.setId(auctionId);
        auction.setStartPrice(new BigDecimal("1000"));
        auction.setCurrentPrice(new BigDecimal("1000"));
        auction.setEndTime(LocalDateTime.now().plusMinutes(30)); // 경매 30분으로 설정
        auctionMap.put(auctionId, auction);

        return auction;
    }
}
