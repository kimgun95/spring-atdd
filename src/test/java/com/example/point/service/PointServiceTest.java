package com.example.point.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PointServiceTest {

    @InjectMocks
    private RatePointService ratePointService;
    private static Stream<Arguments> validPointAddParameter() {
        return Stream.of(
                Arguments.of(10000, 100),
                Arguments.of(20000, 200),
                Arguments.of(30000, 300)
        );
    }
    @ParameterizedTest
    @MethodSource("validPointAddParameter")
    public void _1프로적립성공(final int price, final int savedPrice) {
        //given
        //when
        final int result = ratePointService.calculateAmount(price);
        //then
        assertThat(result).isEqualTo(savedPrice);
    }
}
