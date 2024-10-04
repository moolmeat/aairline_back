package aairline.auction;

import aairline.auth.entity.User;
import aairline.auth.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
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

        if (auction == null || auction.getEndTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("경매가 종료되었거나 존재하지 않습니다.");
        }

        if (bid.getBidAmount().compareTo(auction.getCurrentPrice()) <= 0) {
            throw new IllegalStateException("현재 가격보다 높은 금액을 입찰해야 합니다.");
        }

        if (bid.getBidAmount().compareTo(user.getPoint()) > 0) {
            throw new IllegalStateException("소지 금액이 부족합니다.");
        }

        // 입찰 처리
        auction.setCurrentPrice(bid.getBidAmount());
        auction.setHighestBidderId(user.getId());

        // Redis에 경매 업데이트
        redisTemplate.opsForValue().set("auction:" + auctionId, auction);

        // 필요할 경우 DB에 주기적으로 저장
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
        auction.setStartPrice(auctionItem.getStartingPrice());
        auction.setCurrentPrice(auctionItem.getStartingPrice());
        auction.setStartTime(LocalDateTime.now());
        auction.setEndTime(LocalDateTime.now().plusMinutes(30));

        // Redis와 DB에 저장
        auctionRepository.save(auction);
        redisTemplate.opsForValue().set("auction:" + auction.getId(), auction);

        return auction;
    }

    public Auction registerItem(UserDetails userDetails, AuctionItem auctionItem) {
        Optional<User> optionalUser = userRepository.findByEmail(userDetails.getUsername());
        User user = optionalUser.orElseThrow(() -> new IllegalStateException("유저를 찾을 수 없습니다."));

        // 상품 등록 후 DB에 저장
        auctionItemRepository.save(auctionItem);

        // 이후 필요한 경우 경매 시작 가능
        return null; // 아이템 등록 시에는 경매가 바로 생성되지 않음.
    }

    public Auction endAuction(UserDetails userDetails, Long auctionId) {
        Auction auction = redisTemplate.opsForValue().get("auction:" + auctionId);

        if (auction == null || auction.getEndTime().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("경매가 존재하지 않거나 종료되지 않았습니다.");
        }

        auction.setEndTime(LocalDateTime.now()); // 경매 종료

        // 종료된 경매 정보를 DB에 저장
        auctionRepository.save(auction);

        // 캐시에서 제거
        redisTemplate.delete("auction:" + auctionId);

        return auction;
    }

    private boolean shouldPersistToDatabase(Auction auction) {
        // 예를 들어, 최고 입찰가가 변경될 때마다 저장
        return true;
    }
}