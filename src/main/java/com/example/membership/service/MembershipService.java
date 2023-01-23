package com.example.membership.service;

import com.example.membership.entity.Membership;
import com.example.membership.entity.MembershipType;
import com.example.membership.exception.MembershipErrorResult;
import com.example.membership.exception.MembershipException;
import com.example.membership.repository.MembershipRepository;
import org.springframework.stereotype.Service;

@Service
public class MembershipService {

    private MembershipRepository membershipRepository;

    public Membership addMembership(String userId, MembershipType membershipType, Integer point) {
        Membership result = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);
        if (result != null)
            throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
        return null;
    }
}
