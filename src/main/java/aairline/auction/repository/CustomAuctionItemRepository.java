package aairline.auction.repository;

import aairline.auction.entity.AuctionItem;
import java.util.List;

public interface CustomAuctionItemRepository {
    List<AuctionItem> findAuctionItems(int page, int size);
}
