package com.K_oin.Koin.DTO.boardDTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardDTO {
    private String title;         // 제목
    private String body;
    private String boardType;    // 게시판 유형 (예: 자유게시판, 공지사항 등)
    private boolean anonymous;   // 익명 여부
}
