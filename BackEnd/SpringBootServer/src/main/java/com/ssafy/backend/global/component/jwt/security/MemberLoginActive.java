package com.ssafy.backend.global.component.jwt.security;

import com.ssafy.backend.domain.member.entity.enums.MemberRole;

public record MemberLoginActive(
        Long id,
        String email,
        String name,
        String nickname,
        MemberRole role
) {
}
