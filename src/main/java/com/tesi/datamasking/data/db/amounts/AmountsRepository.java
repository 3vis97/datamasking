package com.tesi.datamasking.data.db.amounts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AmountsRepository extends JpaRepository<Amounts, String>, CustomAmountsRepository {
}
