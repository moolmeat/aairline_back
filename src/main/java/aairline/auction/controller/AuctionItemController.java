package aairline.auction.controller;

import aairline.auction.dto.AuctionItemRequestDto;
import aairline.auction.entity.AuctionItem;
import aairline.auction.service.AuctionItemService;
import aairline.common.request.PagingRequestDto;
import aairline.common.response.CustomResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
