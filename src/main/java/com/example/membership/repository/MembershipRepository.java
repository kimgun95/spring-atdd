package com.example.membership.repository;

import com.example.membership.entity.Membership;
import com.example.membership.entity.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Membership findByUserIdAndMembershipType(String userId, MembershipType membershipType);
}
