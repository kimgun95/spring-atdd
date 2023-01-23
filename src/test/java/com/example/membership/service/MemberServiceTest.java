package com.example.membership.service;

import com.example.membership.dto.MembershipResponse;
import com.example.membership.entity.Membership;
import com.example.membership.entity.MembershipType;
import com.example.membership.exception.MembershipErrorResult;
import com.example.membership.exception.MembershipException;
import com.example.membership.repository.MembershipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {


    private String userId;
    private MembershipType membershipType;
    private Integer point;
    @Mock
    private MembershipRepository membershipRepository;
    @InjectMocks
    private MembershipService target;
    private Membership membership;

    @BeforeEach
    void setUp() {
        userId = "userId";
        membershipType = MembershipType.NAVER;
        point = 10000;
        membership = Membership.builder()
                .id(-1L)
                .userId(userId)
                .point(point)
                .membershipType(MembershipType.NAVER)
                .build();
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

    @Test
    public void 멤버십등록성공() {
        // given
        doReturn(null).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);
        doReturn(membership).when(membershipRepository).save(any(Membership.class));

        // when
        final MembershipResponse result = target.addMembership(userId, membershipType, point);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);

        // verify
        verify(membershipRepository, times(1)).findByUserIdAndMembershipType(userId, membershipType);
        verify(membershipRepository, times(1)).save(any(Membership.class));
    }

}
