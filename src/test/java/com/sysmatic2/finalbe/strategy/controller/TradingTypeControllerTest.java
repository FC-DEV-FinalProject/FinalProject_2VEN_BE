package com.sysmatic2.finalbe.strategy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sysmatic2.finalbe.config.SecurityConfig;
import com.sysmatic2.finalbe.exception.TradingTypeNotFoundException;
import com.sysmatic2.finalbe.strategy.dto.TradingTypeAdminRequestDto;
import com.sysmatic2.finalbe.strategy.dto.TradingTypeAdminResponseDto;
import com.sysmatic2.finalbe.strategy.service.TradingTypeService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TradingTypeController.class)
@Import(SecurityConfig.class)
class TradingTypeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradingTypeService tradingTypeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("매매유형 목록 조회 - 정상 호출")
    @WithMockUser
    void getAllTradingTypes_shouldReturnOkStatus() throws Exception {
        Map<String, Object> response = Map.of("data", "Sample Data");
        when(tradingTypeService.findAllTradingTypes(0, 2, null)).thenReturn(response);

        mockMvc.perform(get("/api/admin/trading-types")
                        .param("page", "0")
                        .param("pageSize", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Sample Data"));
    }

    @Test
    @DisplayName("매매유형 목록 조회 - 잘못된 파라미터로 호출 시 400 반환")
    @WithMockUser
    void getAllTradingTypes_shouldReturnBadRequestWhenParamsInvalid() throws Exception {
        mockMvc.perform(get("/api/admin/trading-types")
                        .param("page", "-1")
                        .param("pageSize", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("매매유형 ID로 상세 조회 - 정상 호출")
    @WithMockUser
    void getTradingTypeById_shouldReturnTradingType() throws Exception {
        TradingTypeAdminResponseDto dto = new TradingTypeAdminResponseDto();
        dto.setTradingTypeId(1);
        dto.setTradingTypeName("Sample Type");
        when(tradingTypeService.findTradingTypeById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/admin/trading-types/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tradingTypeId").value(1))
                .andExpect(jsonPath("$.data.tradingTypeName").value("Sample Type"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("매매유형 ID로 조회 - 존재하지 않는 ID로 호출 시 404 반환")
    @WithMockUser
    void getTradingTypeById_shouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        int invalidId = 999;
        when(tradingTypeService.findTradingTypeById(invalidId))
                .thenThrow(new TradingTypeNotFoundException(invalidId));

        mockMvc.perform(get("/api/admin/trading-types/" + invalidId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("매매유형 등록 - 정상 호출")
    @WithMockUser
    void createTradingType_shouldReturnCreatedStatus() throws Exception {
        TradingTypeAdminRequestDto requestDto = new TradingTypeAdminRequestDto();
        requestDto.setTradingTypeName("New Type");
        requestDto.setTradingTypeIcon("New Icon");

        mockMvc.perform(post("/api/admin/trading-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value("CREATE_SUCCESS"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("매매유형 등록 - 유효하지 않은 데이터로 호출 시 400 반환")
    @WithMockUser
    void createTradingType_shouldReturnBadRequestWhenRequestInvalid() throws Exception {
        TradingTypeAdminRequestDto invalidDto = new TradingTypeAdminRequestDto();
        invalidDto.setTradingTypeIcon("Icon Only");

        mockMvc.perform(post("/api/admin/trading-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("매매유형 삭제 - 정상 호출")
    @WithMockUser
    void deleteTradingType_shouldReturnOkStatus() throws Exception {
        mockMvc.perform(delete("/api/admin/trading-types/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("DELETE_SUCCESS"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("매매유형 삭제 - 존재하지 않는 ID로 호출 시 404 반환")
    @WithMockUser
    void deleteTradingType_shouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        int invalidId = 999;
        Mockito.doThrow(new TradingTypeNotFoundException(invalidId))
                .when(tradingTypeService).deleteTradingType(invalidId);

        mockMvc.perform(delete("/api/admin/trading-types/" + invalidId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("매매유형 수정 - 정상 호출")
    @WithMockUser
    void updateTradingType_shouldReturnOkStatus() throws Exception {
        TradingTypeAdminRequestDto requestDto = new TradingTypeAdminRequestDto();
        requestDto.setTradingTypeName("Updated Type");
        requestDto.setTradingTypeIcon("Updated Icon");

        mockMvc.perform(put("/api/admin/trading-types/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("UPDATE_SUCCESS"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("매매유형 수정 - 존재하지 않는 ID로 호출 시 404 반환")
    @WithMockUser
    void updateTradingType_shouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        TradingTypeAdminRequestDto requestDto = new TradingTypeAdminRequestDto();
        requestDto.setTradingTypeName("Updated Type");
        requestDto.setTradingTypeIcon("Updated Icon");

        int invalidId = 999;
        Mockito.doThrow(new TradingTypeNotFoundException(invalidId))
                .when(tradingTypeService).updateTradingType(eq(invalidId), any(TradingTypeAdminRequestDto.class));

        mockMvc.perform(put("/api/admin/trading-types/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("매매유형 수정 - 유효하지 않은 데이터로 호출 시 400 반환")
    @WithMockUser
    void updateTradingType_shouldReturnBadRequestWhenRequestInvalid() throws Exception {
        TradingTypeAdminRequestDto invalidDto = new TradingTypeAdminRequestDto();
        invalidDto.setTradingTypeIcon("Icon Only");

        mockMvc.perform(put("/api/admin/trading-types/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}