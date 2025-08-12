package com.rental_manager.roomie.account_module.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AccountDTO {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private boolean active;
    private boolean verified;
    private List<String> roles;
}
