package com.rental_manager.roomie.account_module.utils;

import com.rental_manager.roomie.account_module.dtos.RegisterClientDTO;
import com.rental_manager.roomie.entities.Account;

public class AccountConverter {

    public static Account convertRegisterClientDTOToAccount(RegisterClientDTO registerClientDTO) {
        return new Account(registerClientDTO.getFirstName(), registerClientDTO.getLastName(),
                registerClientDTO.getUsername(), registerClientDTO.getEmail(), registerClientDTO.getPassword());
    }
}
