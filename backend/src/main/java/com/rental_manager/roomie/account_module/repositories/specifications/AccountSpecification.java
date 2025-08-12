package com.rental_manager.roomie.account_module.repositories.specifications;

import com.rental_manager.roomie.entities.Account;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AccountSpecification {

    public static Specification<Account> usernameOrFirstNameOrLastNameMatches(List<String> phrases) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            for(String phrase: phrases) {
                predicates.add(cb.like(cb.lower(root.get("username")), "%" + phrase.toLowerCase() + "%"));
                predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + phrase.toLowerCase() + "%"));
                predicates.add(cb.like(cb.lower(root.get("lastName")), "%" + phrase.toLowerCase() + "%"));
            }

            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}
