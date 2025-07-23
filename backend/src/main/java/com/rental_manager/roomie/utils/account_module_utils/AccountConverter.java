package com.rental_manager.roomie.utils.account_module_utils;

import com.rental_manager.roomie.account_module.dtos.AccountOnPageDTO;
import com.rental_manager.roomie.account_module.dtos.RegisterClientDTO;
import com.rental_manager.roomie.entities.Account;

public class AccountConverter {

    public static Account convertRegisterClientDTOToAccount(RegisterClientDTO registerClientDTO) {
        return new Account(registerClientDTO.getFirstName(), registerClientDTO.getLastName(),
                registerClientDTO.getUsername(), registerClientDTO.getEmail(), registerClientDTO.getPassword());
    }

    public static AccountOnPageDTO convertAccountToAccountOnPageDto(Account account) {
        return new AccountOnPageDTO(account.getUsername(), account.getFirstName(), account.getLastName());
    }
}
