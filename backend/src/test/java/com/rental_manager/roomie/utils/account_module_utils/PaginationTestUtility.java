package com.rental_manager.roomie.utils.account_module_utils;

import org.springframework.data.domain.Sort;

public class PaginationTestUtility {

    public static final String PAGE_NUMBER_FIELD = "pageNumber";
    public static final String PAGE_SIZE_FIELD = "pageSize";
    public static final String DIRECTION_FIELD = "direction";
    public static final String SORT_FIELD = "sortField";
    public static final String DIRECTION_ASC = Sort.Direction.ASC.name();
    public static final String DIRECTION_DESC = Sort.Direction.DESC.name();

    //Account Page Request DTO
    public static final String PHRASES_FIELD = "phrases";
}
