package com.K_oin.Koin.Repository.boardRepository;

import com.K_oin.Koin.Entitiy.BoardEntity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<BoardComment, Long> {
}
