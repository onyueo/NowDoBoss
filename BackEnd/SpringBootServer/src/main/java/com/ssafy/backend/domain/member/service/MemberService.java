package com.ssafy.backend.domain.member.service;

import com.ssafy.backend.domain.member.dto.MemberSignupRequest;

public interface MemberService {

    void signupMember(MemberSignupRequest signupRequest);
}
