package com.K_oin.Koin.Controller;

import com.K_oin.Koin.DTO.ApiResponse;
import com.K_oin.Koin.DTO.commentDTOs.CommentDetailDTO;
import com.K_oin.Koin.Service.boardServices.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/management/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/createComment")
    public ResponseEntity<ApiResponse<CommentDetailDTO>> createComment(@RequestBody CommentDetailDTO commentDetailDTO, Authentication authentication) {

        try {
            commentService.createComment(commentDetailDTO, authentication.getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, null, "댓글 생성 실패: " + e.getMessage()));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, commentDetailDTO, "댓글 생성 성공"));
    }

    @PostMapping("/createReplyComment")
    public ResponseEntity<ApiResponse<CommentDetailDTO>> createCommentReply(@RequestBody CommentDetailDTO commentDetailDTO, Authentication authentication) {

        try {
            commentService.createReplyComment(commentDetailDTO, authentication.getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, null, "대댓글 생성 실패: " + e.getMessage()));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, commentDetailDTO, "대댓글 생성 성공"));
    }

    @PostMapping("/CommentLike/{commentId}")
    public ResponseEntity<ApiResponse<String>> likeComment(@PathVariable Long commentId, Authentication authentication) {
        String message = "";

        try {
            message = commentService.likeComment(commentId, authentication.getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "댓글 번호: " + commentId, "댓글 좋아요 실패: " + e.getMessage()));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "댓글 번호: " + commentId, message));
    }

    @PostMapping("/replyCommentLike/{replyCommentId}")
    public ResponseEntity<ApiResponse<String>> likeReplyComment(@PathVariable Long replyCommentId, Authentication authentication) {
        String message = "";

        try {
            message = commentService.likeReplyComment(replyCommentId, authentication.getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "대댓글 번호: " + replyCommentId, "대댓글 좋아요 실패: " + e.getMessage()));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "대댓글 번호: " + replyCommentId, message));
    }

    @GetMapping("/replyComment/{commentId}")
    public ResponseEntity<ApiResponse<List<CommentDetailDTO>>> getCommentReply(@PathVariable Long commentId) {

        List<CommentDetailDTO> replies;

        try {
            replies = commentService.getCommentReply(commentId);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, null, "대댓글 목록 조회 실패: " + e.getMessage()));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, replies, "대댓글 목록 조회 성공"));
    }

    @DeleteMapping("/deleteComment/{commentId}")
    public ResponseEntity<ApiResponse<String>> deleteComment(@PathVariable Long commentId, Authentication authentication) {

        try {
            commentService.deleteComment(commentId, authentication.getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, null, "댓글 삭제 실패: " + e.getMessage()));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "댓글 Id: " + commentId, "댓글 삭제 성공"));

    }

    @DeleteMapping("/deleteReplyComment/{replyCommentId}")
    public ResponseEntity<ApiResponse<String>> deleteReplyComment(@PathVariable Long replyCommentId, Authentication authentication) {

        try {
            commentService.deleteReplyComment(replyCommentId, authentication.getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, null, "대댓글 삭제 실패: " + e.getMessage()));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "대댓글 Id: " + replyCommentId, "대댓글 삭제 성공"));

    }

}
