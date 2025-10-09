package com.K_oin.Koin.DTO.commentDTOs;

import com.K_oin.Koin.DTO.userDTOs.BoardAuthorDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDetailDTO {
    private Long BoardId;
    private Long commentId;
    private Long replyCommentId;
    private LocalDateTime createdDate;
    private BoardAuthorDTO author;
    private boolean anonymous;
    private String body;
}
