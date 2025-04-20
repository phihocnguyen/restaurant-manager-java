package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.entities.Account;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
//    @Query("SELECT a FROM Account a WHERE a.accUsername = :username")
//    Optional<Account> findCustomAccount(@Param("username") String accUsername);
    Optional<Account> findOneByAccUsername(String accUsername);

    boolean existsByAccUsername(String accUsername);

    Optional<Account> findOneByAccEmail(String email);
}
