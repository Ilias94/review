package com.ilias.review.mapper;

import com.ilias.review.model.dto.BookDetailsResponseDto;
import com.ilias.review.model.dto.BookSearchResponseDto;
import com.ilias.review.model.dto.GutendexRawBookDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = AuthorMapper.class)
public interface GutendexBookMapper {
    BookSearchResponseDto fromExternalApiToBookSearchDto(GutendexRawBookDto raw);

    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    BookDetailsResponseDto toBookDetailsDto(GutendexRawBookDto raw);

    List<BookSearchResponseDto> fromExternalListToBookSearchDtoList(List<GutendexRawBookDto> rawList);
}
