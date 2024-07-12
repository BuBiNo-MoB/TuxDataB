package com.database.TuxDataB.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByDistributionId(Long distributionId);
    List<Comment> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.user.id = ?1")
    void deleteByUserId(Long userId);
}
