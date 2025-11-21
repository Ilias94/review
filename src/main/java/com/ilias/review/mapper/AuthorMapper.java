package com.ilias.review.mapper;

import com.ilias.review.model.dto.AuthorResponseDto;
import com.ilias.review.model.dto.GutendexAuthorDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthorMapper {
    List<AuthorResponseDto> fromExternalApiToAuthorDto(List<GutendexAuthorDto> gutendexAuthorDto);
}
