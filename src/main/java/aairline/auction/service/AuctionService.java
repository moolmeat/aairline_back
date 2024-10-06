package aairline.auction.service;

import aairline.auction.entity.Auction;
import aairline.auction.entity.AuctionItem;
import aairline.auction.entity.Bid;
import aairline.auction.repository.AuctionItemRepository;
import aairline.auction.repository.AuctionRepository;
import aairline.auth.entity.User;
import aairline.auth.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuctionService {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final AuctionItemRepository auctionItemRepository;
    private final RedisTemplate<String, Auction> redisTemplate;

    private final Map<Long, Auction> auctionMap = new ConcurrentHashMap<>();

    @Autowired
    public AuctionService(UserRepository userRepository, AuctionRepository auctionRepository, AuctionItemRepository auctionItemRepository, RedisTemplate<String, Auction> redisTemplate) {
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.auctionItemRepository = auctionItemRepository;
        this.redisTemplate = redisTemplate;
    }

    public Auction processBid(UserDetails userDetails, Long auctionId, Bid bid) {
        Optional<User> optionalUser = userRepository.findByEmail(userDetails.getUsername());
        User user = optionalUser.orElseThrow(() -> new IllegalStateException("유저를 찾을 수 없습니다."));

        Auction auction = redisTemplate.opsForValue().get("auction:" + auctionId);
        if (auction == null || auction.isEnded()) {
            throw new IllegalStateException("경매가 종료되었거나 존재하지 않습니다.");
        }

        if (bid.getBidAmount().compareTo(auction.getCurrentPrice()) <= 0) {
            throw new IllegalStateException("현재 가격보다 높은 금액을 입찰해야 합니다.");
        }

        if (bid.getBidAmount().compareTo(user.getPoint()) > 0) {
            throw new IllegalStateException("소지 금액이 부족합니다.");
        }

        auction.setCurrentPrice(bid.getBidAmount());
        auction.setHighestBidderId(user.getId());

        redisTemplate.opsForValue().set("auction:" + auctionId, auction);

        if (shouldPersistToDatabase(auction)) {
            auctionRepository.save(auction);
        }

        return auction;
    }

    public Auction startAuction(UserDetails userDetails, Long auctionItemId) {
        Optional<User> optionalUser = userRepository.findByEmail(userDetails.getUsername());
        User user = optionalUser.orElseThrow(() -> new IllegalStateException("유저를 찾을 수 없습니다."));

        AuctionItem auctionItem = auctionItemRepository.findById(auctionItemId)
            .orElseThrow(() -> new IllegalStateException("경매 아이템을 찾을 수 없습니다."));

        Auction auction = new Auction();
        auction.setAuctionItem(auctionItem);
        auction.setCurrentPrice(auctionItem.getStartingPrice());

        auctionRepository.save(auction);
        redisTemplate.opsForValue().set("auction:" + auction.getId(), auction);

        return auction;
    }

    public Auction endAuction(UserDetails userDetails, Long auctionId) {
        Auction auction = redisTemplate.opsForValue().get("auction:" + auctionId);
        if (auction == null || auction.isEnded()) {
            throw new IllegalStateException("경매가 존재하지 않거나 종료되지 않았습니다.");
        }

        auction.setEnded(true);
        auctionRepository.save(auction);
        redisTemplate.delete("auction:" + auctionId);

        return auction;
    }

    private boolean shouldPersistToDatabase(Auction auction) {
        return true;
    }
}
