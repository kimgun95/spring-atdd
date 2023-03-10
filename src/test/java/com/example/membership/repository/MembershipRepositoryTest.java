package com.example.membership.repository;

import com.example.membership.entity.Membership;
import com.example.membership.entity.MembershipType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MembershipRepositoryTest {

    @Autowired
    private MembershipRepository membershipRepository;
    private static Membership getMembership(MembershipType membershipType, String userId, int point) {
        return Membership.builder()
                .userId(userId)
                .membershipType(membershipType)
                .point(point)
                .build();
    }
    @Test
    public void 멤버십등록() {
        //given
        final Membership membership = getMembership(MembershipType.NAVER, "userId", 10000);
        //when
        final Membership result = membershipRepository.save(membership);
        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo("userId");
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(result.getPoint()).isEqualTo(10000);
    }
    @Test
    public void 멤버십이존재하는지테스트() {
        //given
        final Membership membership = getMembership(MembershipType.NAVER, "userId", 10000);
        //when
        membershipRepository.save(membership);
        final Membership findResult = membershipRepository.findByUserIdAndMembershipType("userId", MembershipType.NAVER);
        //then
        assertThat(findResult).isNotNull();
        assertThat(findResult.getId()).isNotNull();
        assertThat(findResult.getUserId()).isEqualTo("userId");
        assertThat(findResult.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(findResult.getPoint()).isEqualTo(10000);

    }
    @Test
    public void 멤버십조회_사이즈가0() {
        //given

        //when
        List<Membership> result = membershipRepository.findAllByuserId("userId");
        //then
        assertThat(result.size()).isEqualTo(0);
    }
    @Test
    public void 멤버십조회_사이즈가2() {
        //given
        final Membership naverMembership = getMembership(MembershipType.NAVER, "userId", 10000);
        final Membership kakaoMembership = getMembership(MembershipType.KAKAO, "userId", 10000);

        membershipRepository.save(naverMembership);
        membershipRepository.save(kakaoMembership);
        //when
        List<Membership> result = membershipRepository.findAllByuserId("userId");
        //then
        assertThat(result.size()).isEqualTo(2);
    }
    @Test
    public void 멤버십추가후삭제() {
        //given
        final Membership membership = getMembership(MembershipType.NAVER, "userId", 10000);
        final Membership savedMembership = membershipRepository.save(membership);
        //when
        membershipRepository.deleteById(savedMembership.getId());
        //then
    }
}
