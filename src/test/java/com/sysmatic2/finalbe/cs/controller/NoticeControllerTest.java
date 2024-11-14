//package com.sysmatic2.finalbe.cs.controller;
//
//import com.sysmatic2.finalbe.cs.dto.NoticeDTO;
//import com.sysmatic2.finalbe.cs.service.NoticeService;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(NoticeController.class)
//class NoticeControllerTest {
//
//  @Autowired
//  private MockMvc mockMvc;
//
//  @MockBean
//  private NoticeService noticeService;
//
//  @Test
//  void testListNotices() throws Exception {
//    NoticeDTO notice1 = new NoticeDTO();
//    notice1.setId(1L);
//    notice1.setTitle("First Notice");
//
//    Page<NoticeDTO> noticePage = new PageImpl<>(List.of(notice1));
//    when(noticeService.getAllNotices(0, 10)).thenReturn(noticePage);
//
//    mockMvc.perform(get("/api/notices")
//                    .param("page", "0")
//                    .param("size", "10"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.content[0].title").value("First Notice"));
//
//    verify(noticeService, times(1)).getAllNotices(0, 10);
//  }
//
//  @Test
//  void testViewNotice() throws Exception {
//    NoticeDTO noticeDTO = new NoticeDTO();
//    noticeDTO.setId(1L);
//    noticeDTO.setTitle("Sample Notice");
//
//    when(noticeService.getNoticeById(1L)).thenReturn(Optional.of(noticeDTO));
//
//    mockMvc.perform(get("/api/notices/1"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.title").value("Sample Notice"));
//
//    verify(noticeService, times(1)).getNoticeById(1L);
//  }
//
//  @Test
//  void testCreateNotice() throws Exception {
//    NoticeDTO noticeDTO = new NoticeDTO();
//    noticeDTO.setTitle("New Notice");
//
//    when(noticeService.saveNotice(any(NoticeDTO.class))).thenReturn(noticeDTO);
//
//    mockMvc.perform(post("/api/notices")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content("{\"title\":\"New Notice\"}"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.title").value("New Notice"));
//
//    verify(noticeService, times(1)).saveNotice(any(NoticeDTO.class));
//  }
//
//  @Test
//  void testEditNotice() throws Exception {
//    NoticeDTO noticeDTO = new NoticeDTO();
//    noticeDTO.setId(1L);
//    noticeDTO.setTitle("Updated Notice");
//
//    when(noticeService.saveNotice(any(NoticeDTO.class))).thenReturn(noticeDTO);
//
//    mockMvc.perform(put("/api/notices/1")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content("{\"title\":\"Updated Notice\"}"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.title").value("Updated Notice"));
//
//    verify(noticeService, times(1)).saveNotice(any(NoticeDTO.class));
//  }
//
//  @Test
//  void testDeleteNotice() throws Exception {
//    doNothing().when(noticeService).deleteNotice(1L);
//
//    mockMvc.perform(delete("/api/notices/1"))
//            .andExpect(status().isOk());
//
//    verify(noticeService, times(1)).deleteNotice(1L);
//  }
//}
