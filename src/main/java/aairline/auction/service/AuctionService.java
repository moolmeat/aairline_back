package aairline.auction.service;

import aairline.auction.entity.Auction;
import aairline.auction.entity.Bid;
import aairline.auction.repository.AuctionItemRepository;
import aairline.auction.repository.AuctionRepository;
import aairline.auth.entity.User;
import aairline.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final AuctionItemRepository auctionItemRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Auction> redisTemplate;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository, UserRepository userRepository, AuctionItemRepository auctionItemRepository, RedisTemplate<String, Auction> redisTemplate) {
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
        this.auctionItemRepository = auctionItemRepository;
        this.redisTemplate = redisTemplate;
    }

    public Auction processBid(UserDetails userDetails, Long auctionId, Bid bid) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Auction auction = redisTemplate.opsForValue().get("auction:" + auctionId);
        if (auction == null || auction.isEnded()) {
            throw new IllegalStateException("경매가 종료되었거나 존재하지 않습니다.");
        }

        if (bid.getBidAmount().compareTo(auction.getCurrentPrice()) <= 0) {
            throw new IllegalStateException("현재 가격보다 높은 금액을 입찰해야 합니다.");
        }

        auction.setCurrentPrice(bid.getBidAmount());
        auction.setHighestBidderId(user.getId());
        redisTemplate.opsForValue().set("auction:" + auctionId, auction);

        return auction;
    }

    public Auction getAuctionById(Long auctionId) {
        return auctionRepository.findById(auctionId).orElseThrow(() -> new RuntimeException("Auction not found"));
    }
}
