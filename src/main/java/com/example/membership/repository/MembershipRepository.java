package com.example.membership.repository;

import com.example.membership.entity.Membership;
import com.example.membership.entity.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Membership findByUserIdAndMembershipType(final String userId, final MembershipType membershipType);

    List<Membership> findAllByuserId(final String userId);
}
