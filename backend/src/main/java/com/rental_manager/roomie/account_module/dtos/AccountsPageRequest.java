package com.rental_manager.roomie.account_module.dtos;

import com.rental_manager.roomie.utils.searching_with_pagination.PagingRequest;
import lombok.Getter;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
public class AccountsPageRequest extends PagingRequest {

    private String sortField;
    private List<String> phrases;

    public AccountsPageRequest(int pageNumber, int pageSize, Sort.Direction direction, String sortField, List<String> phrases) {
        super(pageNumber, pageSize, direction);
        this.sortField = sortField;
        this.phrases = phrases;
    }
}
