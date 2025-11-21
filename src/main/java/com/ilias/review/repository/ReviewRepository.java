package com.ilias.review.repository;

import com.ilias.review.model.dao.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBookId(Long bookId);

    @Query("select avg(r.rating) from Review r where r.bookId = :bookId")
    Optional<Double> findAverageRatingByBookId(@Param("bookId") Long bookId);
}
