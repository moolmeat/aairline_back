package aairline.auction.repository;

import aairline.auction.entity.AuctionItem;
import aairline.auction.entity.QAuctionItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CustomAuctionItemRepositoryImpl implements CustomAuctionItemRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final JPAQueryFactory queryFactory;
    private final QAuctionItem qAuctionItem = QAuctionItem.auctionItem;

    public CustomAuctionItemRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<AuctionItem> findAuctionItems(int page, int size) {
        return queryFactory
            .selectFrom(qAuctionItem)
            .orderBy(qAuctionItem.startTime.desc())
            .offset((long) (page - 1) * size)
            .limit(size)
            .fetch();
    }
}
