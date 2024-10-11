package aairline.auction.repository;

import aairline.auction.entity.Auction;
import aairline.auction.entity.AuctionItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Optional<Auction> findByAuctionItem(AuctionItem auctionItem);
    List<Auction> findAllByEndedFalse();
}
