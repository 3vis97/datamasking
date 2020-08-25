package com.tesi.datamasking.data.db.customers;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.List;

@Transactional
public class CustomersRepositoryImpl implements CustomCustomerRepository {

  @Autowired
  private EntityManagerFactory entityManagerFactory;

  private final int defaultBatchSize = 50;

  @Override
  public void insertWithBatchInsert(List<Customers> rows) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction entityTransaction = entityManager.getTransaction();

    Iterator<Customers> iterator = rows.iterator();
    entityTransaction.begin();
    int cont = 0;
    while (iterator.hasNext()) {
      entityManager.persist(iterator.next());
      cont++;
      if (cont % defaultBatchSize == 0) {
        entityManager.flush();
        entityManager.clear();
        entityTransaction.commit();
        entityTransaction.begin();
      }
    }
    entityTransaction.commit();
  }
}
