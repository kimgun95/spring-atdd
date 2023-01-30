package com.example.membership.service;

import com.example.membership.dto.MembershipAddResponse;
import com.example.membership.dto.MembershipDetailResponse;
import com.example.membership.entity.Membership;
import com.example.membership.entity.MembershipType;
import com.example.membership.exception.MembershipErrorResult;
import com.example.membership.exception.MembershipException;
import com.example.membership.repository.MembershipRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembershipService {

    private MembershipRepository membershipRepository;

    public MembershipAddResponse addMembership(final String userId, final MembershipType membershipType, final Integer point) {
        final Membership result = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);
        if (result != null)
            throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);

        final Membership membership = Membership.builder()
                .userId(userId)
                .point(point)
                .membershipType(membershipType)
                .build();
        final Membership savedMembership = membershipRepository.save(membership);

        return MembershipAddResponse.builder()
                .id(savedMembership.getId())
                .membershipType(savedMembership.getMembershipType())
                .build();
    }

    public List<MembershipDetailResponse> getMembershipList(final String userId) {
        final List<Membership> membershipList = membershipRepository.findAllByuserId(userId);
        return membershipList.stream()
                .map(membership -> MembershipDetailResponse.builder()
                        .id(membership.getId())
                        .membershipType(membership.getMembershipType())
                        .point(membership.getPoint())
                        .createdAt(membership.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
