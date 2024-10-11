package aairline.auction.service;

import aairline.auction.dto.AuctionItemRequestDto;
import aairline.auction.entity.Auction;
import aairline.auction.entity.AuctionItem;
import aairline.auction.repository.AuctionItemRepository;
import aairline.auction.repository.AuctionRepository;
import aairline.common.response.CustomResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionItemService {

    private final AuctionItemRepository auctionItemRepository;
    private final AuctionRepository auctionRepository;

    public CustomResponse<Void> addAuctionItem(AuctionItemRequestDto auctionItemRequestDto) {
        AuctionItem auctionItem = new AuctionItem();
        auctionItem.setItemName(auctionItemRequestDto.getItemName());
        auctionItem.setDescription(auctionItemRequestDto.getDescription());
        auctionItem.setCategory(auctionItemRequestDto.getCategory());
        auctionItem.setStartingPrice(auctionItemRequestDto.getStartingPrice());
        auctionItemRepository.save(auctionItem);

        // Auction 생성
        Auction auction = new Auction();
        auction.setAuctionItem(auctionItem);  // 방금 생성한 AuctionItem 참조
        auction.setCurrentPrice(auctionItem.getStartingPrice());  // 시작 가격을 설정
        auction.setEnded(false);  // 경매 진행 중으로 설정
        auction.setStartTime(auctionItemRequestDto.getStartTime());
        auction.setLimitTime(auctionItemRequestDto.getLimitTime());
        auctionRepository.save(auction);  // Auction 저장

        return CustomResponse.success("경매물건 등록에 성공하였습니다.", null, 201);
    }

    public  CustomResponse<List<AuctionItem>> getAuctionItems(int page, int size) {
        List<AuctionItem> items = auctionItemRepository.findAuctionItems(page, size);
        return CustomResponse.success("경매물건 조회에 성공하였습니다.", items, 200);
    }

    public Auction getAuctionById(Long auctionItemId) {
        AuctionItem auctionItem = auctionItemRepository.findById(auctionItemId).orElseThrow();
        return auctionRepository.findByAuctionItem(auctionItem).orElseThrow();
    }

    public void checkAuctionEnd() {
        List<Auction> activeAuctions = auctionRepository.findAllByEndedFalse();  // 아직 끝나지 않은 경매 목록 가져오기
        for (Auction auction : activeAuctions) {
            if (auction.getLimitTime().isBefore(LocalDateTime.now())) {
                auction.setEnded(true);
                auctionRepository.save(auction);  // 경매 종료 상태 업데이트
            }
        }
    }
}
