package com.example.membership.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class MembershipRepositoryTest {

    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    public void MembershipRepository는Null값이아님() {
        assertThat(membershipRepository).isNotNull();
    }
}
