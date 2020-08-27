package com.tesi.datamasking.data.db.customers;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface CustomersRepository extends JpaRepository<Customers, Long>, CustomCustomerRepository {
}
