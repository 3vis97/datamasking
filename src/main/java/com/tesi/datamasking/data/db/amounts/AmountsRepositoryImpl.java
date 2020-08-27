package com.tesi.datamasking.data.db.amounts;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.List;

@Transactional
@Repository
public class AmountsRepositoryImpl implements CustomAmountsRepository {

  @PersistenceUnit
  private EntityManagerFactory entityManagerFactory;

  private final int defaultBatchSize = 1000;

  @Override
  public void insertWithBatchInsert(List<Amounts> rows) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction entityTransaction = entityManager.getTransaction();

    Iterator<Amounts> iterator = rows.iterator();
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
    entityManagerFactory.getCache().evictAll();
    entityManager.close();
  }
}
