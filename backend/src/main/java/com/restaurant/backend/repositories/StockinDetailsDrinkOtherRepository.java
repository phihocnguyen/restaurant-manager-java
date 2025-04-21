package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.entities.StockinDetailsDrinkOther;
import com.restaurant.backend.domains.entities.StockinDetailsDrinkOtherId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockinDetailsDrinkOtherRepository extends JpaRepository<StockinDetailsDrinkOther, StockinDetailsDrinkOtherId> {
}
