package com.example.membership.service;

import com.example.membership.entity.Membership;
import com.example.membership.entity.MembershipType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {


    private String userId;
    private MembershipType membershipType;
    private Integer poing;

    @BeforeEach
    void setUp() {
        userId = "userId";
        membershipType = MembershipType.NAVER;
        poing = 10000;
    }

    @Test
    public void 멤버십등록실패_이미존재() {
        //given
        doReturn(Membership.builder().build()).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);
        //when
        final MembershipException result = assertThrows(MembershipException.class, () -> target.addMembership(userId, membershipType, point));
        //then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
    }
}
