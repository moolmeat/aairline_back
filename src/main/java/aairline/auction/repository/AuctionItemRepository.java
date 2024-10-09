package aairline.auction.repository;

import aairline.auction.entity.AuctionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionItemRepository extends JpaRepository<AuctionItem, Long>, CustomAuctionItemRepository {

}
