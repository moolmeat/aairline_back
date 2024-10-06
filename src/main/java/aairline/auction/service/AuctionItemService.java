package aairline.auction.service;

import aairline.auction.dto.AuctionItemRequestDto;
import aairline.auction.entity.AuctionItem;
import aairline.auction.repository.AuctionItemRepository;
import aairline.common.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionItemService {

    private final AuctionItemRepository auctionItemRepository;

    public CustomResponse<Void> addAuctionItem(AuctionItemRequestDto auctionItemRequestDto) {
        AuctionItem auctionItem = new AuctionItem();
        auctionItem.setItemName(auctionItemRequestDto.getItemName());
        auctionItem.setDescription(auctionItemRequestDto.getDescription());
        auctionItem.setCategory(auctionItemRequestDto.getCategory());
        auctionItem.setStartingPrice(auctionItemRequestDto.getStartingPrice());
        auctionItem.setStartTime(auctionItemRequestDto.getStartTime());
        auctionItem.setLimitTime(auctionItemRequestDto.getLimitTime());
        auctionItemRepository.save(auctionItem);
        return CustomResponse.success("경매물건 등록에 성공하였습니다.", null, 201);
    }
}
