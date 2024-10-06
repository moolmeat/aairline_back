package aairline.auction.controller;

import aairline.auction.dto.AuctionItemRequestDto;
import aairline.auction.service.AuctionItemService;
import aairline.common.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
