package aairline.auction.controller;

import aairline.auction.dto.AuctionItemRequestDto;
import aairline.auction.entity.Auction;
import aairline.auction.entity.AuctionItem;
import aairline.auction.service.AuctionItemService;
import aairline.common.request.PagingRequestDto;
import aairline.common.response.CustomResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionItemController {

    private final AuctionItemService auctionItemService;

    @PostMapping("/item")
    public CustomResponse<Void> addAuctionItem(@RequestBody AuctionItemRequestDto auctionItemRequestDto) {
        return auctionItemService.addAuctionItem(auctionItemRequestDto);
    }

    @GetMapping
    public CustomResponse<List<AuctionItem>> getAuctionItems(
        @RequestParam int page,
        @RequestParam int size
    ) {
        return auctionItemService.getAuctionItems(page, size);
    }

    @GetMapping("/{auctionItemId}")
    public CustomResponse<Auction> getAuctionById(@PathVariable Long auctionItemId) {
        Auction auction = auctionItemService.getAuctionById(auctionItemId);
        return CustomResponse.success("옥션 조회에 성공하였습니다.", auction, 200);
    }

    @Scheduled(fixedRate = 60000)  // 1분마다 실행
    public void checkAuctionEnd() {
        auctionItemService.checkAuctionEnd();
    }
}
