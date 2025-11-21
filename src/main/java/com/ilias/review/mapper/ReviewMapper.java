package com.ilias.review.mapper;

import com.ilias.review.model.dao.Review;
import com.ilias.review.model.dto.ReviewResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewMapper {
    ReviewResponseDto toDto(Review review);

    List<ReviewResponseDto> toDtoList(List<Review> reviews);
}
