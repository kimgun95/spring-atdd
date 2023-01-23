package com.example.membership.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

    private MockMvc mockMvc;
    private Gson gson;
    private MembershipController target;

    @Test
    public void mockMvc는Null아님() {
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }
}
