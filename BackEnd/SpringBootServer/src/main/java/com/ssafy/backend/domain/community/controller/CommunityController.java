package com.ssafy.backend.domain.community.controller;

import com.ssafy.backend.domain.community.dto.request.*;
import com.ssafy.backend.domain.community.dto.response.CommentListResponse;
import com.ssafy.backend.domain.community.dto.response.CommunityDetailResponse;
import com.ssafy.backend.domain.community.dto.response.CommunityListResponse;
import com.ssafy.backend.domain.community.dto.response.PopularCommunityListResponse;
import com.ssafy.backend.domain.community.service.CommentService;
import com.ssafy.backend.domain.community.service.CommunityService;
import com.ssafy.backend.global.common.dto.Message;
import com.ssafy.backend.global.component.jwt.security.MemberLoginActive;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "커뮤니티", description = "커뮤니티 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/community")
public class CommunityController {
    private final CommunityService communityService;
    private final CommentService commentService;

    @Operation(
            summary = "게시글 작성",
            description = "커뮤니티 게시글을 작성하는 기능입니다."
    )
    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<Void>> createCommunity(@AuthenticationPrincipal MemberLoginActive loginActive,
                                          @Validated @RequestBody CreateCommunityRequest request) {

        communityService.createCommunity(loginActive.id(), request);
        return ResponseEntity.ok().body(Message.success());
    }

    @Operation(
            summary = "게시글 목록 조회",
            description = "커뮤니티 게시글 목록을 조회하는 기능입니다."
    )
    @GetMapping
    public ResponseEntity<Message<List<CommunityListResponse>>> selectCommunityList(CommunityListRequest request) {
        return ResponseEntity.ok().body(Message.success(communityService.selectCommunityList(request)));
    }

    @Operation(
            summary = "인기 게시글 조회",
            description = "커뮤니티 인기 게시글을 조회하는 기능입니다."
    )
    @GetMapping("/popular")
    public ResponseEntity<Message<List<PopularCommunityListResponse>>> selectPopularCommunityList() {
        return ResponseEntity.ok().body(Message.success(communityService.selectPopularCommunityList()));
    }

    @Operation(
            summary = "게시글 상세 조회",
            description = "커뮤니티 게시글을 상세 조회하는 기능입니다."
    )
    @GetMapping("/{communityId}")
    public ResponseEntity<Message<CommunityDetailResponse>> selectCommunity(@PathVariable Long communityId) {
        return ResponseEntity.ok().body(Message.success(communityService.selectCommunity(communityId)));
    }

    @Operation(
            summary = "게시글 삭제",
            description = "커뮤니티 게시글을 삭제하는 기능입니다."
    )
    @DeleteMapping("/{communityId}")
    public ResponseEntity<Message<Void>> deleteCommunity(@PathVariable Long communityId) {
        communityService.deleteCommunity(communityId);
        return ResponseEntity.ok().body(Message.success());
    }

    @Operation(
            summary = "게시글 수정",
            description = "커뮤니티 게시글을 수정하는 기능입니다."
    )
    @PatchMapping("/{communityId}")
    public ResponseEntity<Message<Void>> updateCommunity(@PathVariable Long communityId,
                                                         @Validated @RequestBody UpdateCommunityRequest request) {
        communityService.updateCommunity(communityId, request);
        return ResponseEntity.ok().body(Message.success());
    }

    @Operation(
            summary = "댓글 작성",
            description = "커뮤니티 댓글을 작성하는 기능입니다."
    )
    @PostMapping("/{communityId}/comment")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<Void>> createComment(@AuthenticationPrincipal MemberLoginActive loginActive,
                                                       @PathVariable Long communityId,
                                                       @Validated @RequestBody CreateCommentRequest request) {
        commentService.createComment(loginActive.id(), communityId, request.content());
        return ResponseEntity.ok().body(Message.success());
    }

    @Operation(
            summary = "댓글 목록 조회",
            description = "커뮤니티 댓글 목록을 조회하는 기능입니다."
    )
    @GetMapping("/{communityId}/comment")
    public ResponseEntity<Message<List<CommentListResponse>>> selectCommentList(@PathVariable Long communityId, Long lastId) {
        return ResponseEntity.ok().body(Message.success(commentService.selectCommentList(communityId, lastId)));
    }

    @Operation(
            summary = "댓글 삭제 ",
            description = "커뮤니티 댓글을 삭제하는 기능입니다."
    )
    @DeleteMapping("/{communityId}/comment/{commentId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<Void>> deleteComment(@PathVariable Long communityId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().body(Message.success());
    }

    @Operation(
            summary = "댓글 수정 ",
            description = "커뮤니티 댓글을 수정하는 기능입니다."
    )
    @PatchMapping("/{communityId}/comment/{commentId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<Void>> updateComment(@PathVariable Long commentId,
                                        @Validated @RequestBody UpdateCommentRequest request) {
        commentService.updateComment(commentId, request);
        return ResponseEntity.ok().body(Message.success());
    }
}
