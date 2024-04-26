package com.ssafy.backend.domain.community.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.backend.domain.community.dto.CommunityListResponse;
import com.ssafy.backend.domain.community.dto.CommunityDetailResponse;
import com.ssafy.backend.domain.community.dto.ImageInfo;
import com.ssafy.backend.domain.community.entity.enums.Category;
import com.ssafy.backend.global.util.NullSafeBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ssafy.backend.domain.community.entity.QComments.*;
import static com.ssafy.backend.domain.community.entity.QCommunity.*;
import static com.ssafy.backend.domain.community.entity.QImage.*;
import static com.ssafy.backend.domain.member.entity.QMember.*;

@Repository
@RequiredArgsConstructor
public class CommunityCustomRepositoryImpl implements CommunityCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommunityListResponse> selectCommunityList(String category, Long lastId) {
        return queryFactory
                .select(Projections.constructor(CommunityListResponse.class,
                        community.id,
                        community.category,
                        community.title,
                        community.content,
                        community.writer.nickname,
                        community.readCount,
                        ExpressionUtils.as(JPAExpressions.select(comments.count().intValue())
                                .from(comments)
                                .where(comments.community.eq(community)), "commentCount")
                ))
                .from(community)
                .join(community.writer, member)
                .where(isLowerThan(lastId), equalsCategory(category))
                .orderBy(community.id.desc())
                .limit(10)
                .fetch();
    }

    private BooleanBuilder isLowerThan(final Long communityId) {
        BooleanBuilder builder = new BooleanBuilder();
        if (communityId != null && communityId > 0) {
            builder.and(community.id.lt(communityId));
        }
        return builder;
    }

    private BooleanBuilder equalsCategory(final String category) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!category.isBlank()) {
            builder.and(community.category.eq(Category.valueOf(category)));
        }
        return builder;
    }

    @Override
    public CommunityDetailResponse selectCommunity(Long communityId) {
        CommunityDetailResponse communityDetailResponse = queryFactory
                .select(Projections.constructor(CommunityDetailResponse.class,
                        community.id,
                        community.category,
                        community.title,
                        community.content,
                        community.readCount,
                        community.writer.id,
                        community.writer.nickname,
                        community.writer.profileImage,
                        community.createdAt
                ))
                .from(community)
                .where(equalsCommunityId(communityId))
                .fetchOne();

        List<ImageInfo> images = queryFactory
                .select(Projections.constructor(ImageInfo.class,
                        image.id,
                        image.url
                ))
                .from(image)
                .where(equalsCommunityId(communityId))
//                .where(image.community.id.eq(communityId))
                .fetch();

        communityDetailResponse.setImages(images);

        return communityDetailResponse;
    }

    private BooleanBuilder equalsCommunityId(final Long communityId) {
        return NullSafeBuilder.build(() -> community.id.eq(communityId));
    }
}
