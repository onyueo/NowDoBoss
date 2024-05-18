package com.ssafy.backend.domain.chat.controller;

import com.ssafy.backend.domain.chat.dto.request.CreateChatRoomRequest;
import com.ssafy.backend.domain.chat.dto.request.MyChatRoomListRequest;
import com.ssafy.backend.domain.chat.dto.response.*;
import com.ssafy.backend.domain.chat.service.ChatMessageService;
import com.ssafy.backend.domain.chat.service.ChatRoomService;
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

@Tag(name = "채팅방", description = "채팅방 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-rooms")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @Operation(
            summary = "채팅방 목록 조회",
            description = "채팅방 목록을 조회하는 기능입니다."
    )
    @GetMapping
    public ResponseEntity<Message<List<ChatRoomListResponse>>> selectMyChatRooms(Long lastId) {
        List<ChatRoomListResponse> response = chatRoomService.selectChatRooms(lastId);
        return ResponseEntity.ok().body(Message.success(response));
    }

    @Operation(
            summary = "내 채팅방 목록 조회",
            description = "내 채팅방 목록 조회에 필요한 정보를 입력하여 내 채팅방 목록을 조회하는 기능입니다."
    )
    @GetMapping("/my-rooms")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<List<MyChatRoomListResponse>>> selectMyChatRooms(@AuthenticationPrincipal MemberLoginActive loginActive,
                                                                                   MyChatRoomListRequest request) {
        List<MyChatRoomListResponse> response = chatRoomService.selectMyChatRooms(loginActive.id(), request);
        return ResponseEntity.ok().body(Message.success(response));
    }

    @Operation(
            summary = "채팅방 상세정보 조회",
            description = "채팅방 상세정보를 조회하는 기능입니다."
    )
    @GetMapping("/{chatRoomId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<ChatRoomResponse>> selectChatRoomDetail(@PathVariable Long chatRoomId) {
        ChatRoomResponse response = chatRoomService.selectChatRoomDetail(chatRoomId);
        return ResponseEntity.ok().body(Message.success(response));
    }

    @Operation(
            summary = "채팅방 생성",
            description = "채팅방에 필요한 정보를 입력하여 채팅방을 생성하는 기능입니다."
    )
    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<CreateChatRoomResponse>> createChatRoom(@AuthenticationPrincipal MemberLoginActive loginActive,
                                                        @Validated @RequestBody CreateChatRoomRequest request) {

        Long chatRoomId = chatRoomService.createChatRoom(loginActive.id(), request);
        CreateChatRoomResponse response = new CreateChatRoomResponse(chatRoomId);
        return ResponseEntity.ok().body(Message.success(response));
    }

    @Operation(
            summary = "인기 채팅방 조회",
            description = "인기 채팅방 조회에 필요한 정보를 입력하여 조회하는 기능입니다."
    )
    @GetMapping("/popular-room")
    public ResponseEntity<Message<List<ChatRoomResponse>>> selectPopularChatRoom(String category) {
        List<ChatRoomResponse> response = chatRoomService.selectPopularChatRoom(category);
        return ResponseEntity.ok().body(Message.success(response));
    }

    @Operation(
            summary = "채팅방 나가기",
            description = "채팅방을 나가는 기능입니다."
    )
    @DeleteMapping("/{chatRoomId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<Void>> exitChatRoom(@AuthenticationPrincipal MemberLoginActive loginActive,
                                                      @PathVariable Long chatRoomId) {

        chatRoomService.exitChatRoom(loginActive.id(), chatRoomId);
        return ResponseEntity.ok().body(Message.success());
    }

    @Operation(
            summary = "채팅방 입장",
            description = "채팅방에 입장하는 기능입니다."
    )
    @PostMapping("/{chatRoomId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<EnterChatRoomResponse>> enterChatRoom(@AuthenticationPrincipal MemberLoginActive loginActive,
                      @PathVariable Long chatRoomId) {
        EnterChatRoomResponse response = chatRoomService.enterChatRoom(loginActive.id(), chatRoomId);
        return ResponseEntity.ok().body(Message.success(response));
    }

    @Operation(
            summary = "채팅 메시지 내역",
            description = "채팅 메시지 내역을 불러오는 기능입니다."
    )
    @GetMapping("/{chatRoomId}/messages")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<List<ChatMessageResponse>>> selectChatMessages(@PathVariable Long chatRoomId,
                                                                                 Long lastId) {

        List<ChatMessageResponse> response = chatMessageService.selectChatMessages(chatRoomId, lastId);
        return ResponseEntity.ok().body(Message.success(response));
    }
}
