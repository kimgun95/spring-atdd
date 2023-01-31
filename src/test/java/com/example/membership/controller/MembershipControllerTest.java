package com.example.membership.controller;

import com.example.membership.common.GlobalExceptionHandler;
import com.example.membership.dto.MembershipAccumulateRequest;
import com.example.membership.dto.MembershipDetailResponse;
import com.example.membership.dto.MembershipAddRequest;
import com.example.membership.dto.MembershipAddResponse;
import com.example.membership.entity.MembershipType;
import com.example.membership.exception.MembershipErrorResult;
import com.example.membership.exception.MembershipException;
import com.example.membership.service.MembershipService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.example.membership.constants.MembershipConstants.USER_ID_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

    private final String membershipsUrl = "/api/v1/memberships";
    private final String membershipsDetailUrl = "/api/v1/memberships/{id}";
    private final String membershipsAccumulateUrl = "/api/v1/memberships/{id}/accumulate";
    private MockMvc mockMvc;
    private Gson gson;
    @InjectMocks
    private MembershipController target;
    @Mock
    private MembershipService membershipService;
    private String url;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        gson = new Gson();
    }
    private MembershipAddRequest membershipRequest(final Integer point, final MembershipType membershipType) {
        return MembershipAddRequest.builder()
                .point(point)
                .membershipType(membershipType)
                .build();
    }
    private MembershipAccumulateRequest membershipAccumulateRequest(final Integer point) {
        return MembershipAccumulateRequest.builder()
                .point(point)
                .build();
    }
    private static Stream<Arguments> invalidMembershipAddParameter() {
        return Stream.of(
                Arguments.of(null, MembershipType.NAVER),
                Arguments.of(-1, MembershipType.NAVER),
                Arguments.of(10000, null)
        );
    }
    @Test
    public void 멤버십등록실패_사용자식별값이헤더에없음() throws Exception {
        // given

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(membershipsUrl)
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }
    @ParameterizedTest
    @MethodSource("invalidMembershipAddParameter")
    public void 멤버십등록실패_잘못된파라미터(final Integer point, final MembershipType membershipType) throws Exception {
        // given
        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(membershipsUrl)
                        .header(USER_ID_HEADER, "userId")
                        .content(gson.toJson(membershipRequest(point, membershipType)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }
    @Test
    public void 멤버십등록실패_MemberService에서에러Throw() throws Exception {
        // given
        doThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER))
                .when(membershipService)
                .addMembership("userId", MembershipType.NAVER, 10000);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(membershipsUrl)
                        .header(USER_ID_HEADER, "userId")
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }
    @Test
    public void 멤버십등록성공() throws Exception {
        // given
        final MembershipAddResponse membershipAddResponse = MembershipAddResponse.builder()
                .id(-1L)
                .membershipType(MembershipType.NAVER).build();

        doReturn(membershipAddResponse).when(membershipService).addMembership("userId", MembershipType.NAVER, 10000);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(membershipsUrl)
                        .header(USER_ID_HEADER, "userId")
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isCreated());

        final MembershipAddResponse response = gson.fromJson(resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), MembershipAddResponse.class);

        assertThat(response.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(response.getId()).isNotNull();
    }
    @Test
    public void 멤버십조회실패_사용자식별값이헤더에없음() throws Exception {
        //given
        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(membershipsUrl)
        );
        //then
        resultActions.andExpect(status().isBadRequest());
    }
    @Test
    public void 멤버십조회성공() throws Exception {
        //given
        doReturn(Arrays.asList(
                MembershipDetailResponse.builder().build(),
                MembershipDetailResponse.builder().build(),
                MembershipDetailResponse.builder().build()
        )).when(membershipService).getMembershipList("userId");
        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(membershipsUrl)
                        .header(USER_ID_HEADER, "userId")
        );
        //then
        resultActions.andExpect(status().isOk());
    }
    @Test
    public void 멤버십상세조회실패_사용자식별값이헤더에없음() throws Exception{
        //given
        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(membershipsDetailUrl, -1)
        );
        //then
        resultActions.andExpect(status().isBadRequest());
    }
    @Test
    public void 멤버십상세조회실패_멤버십이존재하지않음() throws Exception {
        //given
        doThrow(new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND))
                .when(membershipService).getMembership(-1L, "userId");
        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(membershipsDetailUrl, -1)
                        .header(USER_ID_HEADER, "userId")
        );
        //then
        resultActions.andExpect(status().isNotFound());
    }
    @Test
    public void 멤버십상세조회실패_본인아님() throws Exception {
        //given
        doThrow(new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER))
                .when(membershipService).getMembership(-1L, "userId");
        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(membershipsDetailUrl, -1)
                        .header(USER_ID_HEADER, "userId")
        );
        //then
        resultActions.andExpect(status().isBadRequest());
    }
    @Test
    public void 멤버십상세조회성공() throws Exception {
        //given
        doReturn(MembershipDetailResponse.builder()
                .id(-1L)
                .membershipType(MembershipType.NAVER)
                .build())
                .when(membershipService).getMembership(-1L, "userId");
        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(membershipsDetailUrl, -1)
                        .header(USER_ID_HEADER, "userId")
        );
        //then
        resultActions.andExpect(status().isOk());
        final MembershipDetailResponse response = gson.fromJson(resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), MembershipDetailResponse.class);

        assertThat(response.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(response.getId()).isNotNull();
    }
    @Test
    public void 멤버십삭제실패_사용자식별값이헤더에없음() throws Exception {
        //given
        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(membershipsDetailUrl, -1)
        );
        //then
        resultActions.andExpect(status().isBadRequest());
    }
    @Test
    public void 멤버십삭제성공() throws Exception {
        //given
        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(membershipsDetailUrl, -1)
                        .header(USER_ID_HEADER, "userId")
        );
        //then
        resultActions.andExpect(status().isNoContent());
    }
    @Test
    public void 멤버십적립실패_사용자식별값이헤더에없음() throws Exception {
        //given

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(membershipsAccumulateUrl, -1)
                        .content(gson.toJson(membershipAccumulateRequest(10000)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(status().isBadRequest());
    }
}
