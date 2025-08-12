package com.rental_manager.roomie.utils.searching_with_pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Sort;


@Getter
@AllArgsConstructor
public abstract class PagingRequest {

    private int pageNumber;
    private int pageSize;
    private Sort.Direction direction;
}
