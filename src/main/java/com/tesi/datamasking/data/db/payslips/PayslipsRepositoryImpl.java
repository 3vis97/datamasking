package com.tesi.datamasking.data.db.payslips;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.List;

@Repository
@Transactional
public class PayslipsRepositoryImpl implements CustomPayslipsRepository {

  @PersistenceUnit
  private EntityManagerFactory entityManagerFactory;

  private final int defaultBatchSize = 960;

  @Override
  public void insertWithBatchInsert(List<Payslips> rows) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    try {
      EntityTransaction entityTransaction = entityManager.getTransaction();

      Iterator<Payslips> iterator = rows.iterator();
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
    } catch (Exception e) {
      throw e;
    } finally {
      entityManager.clear();
      entityManager.close();
    }
  }

  @Override
  public void updateWithBatchInsert(List<Payslips> rows) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    try {
      EntityTransaction entityTransaction = entityManager.getTransaction();

      Iterator<Payslips> iterator = rows.iterator();
      entityTransaction.begin();
      int cont = 0;
      while (iterator.hasNext()) {
        entityManager.merge(iterator.next());
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
    } catch (Exception e) {
      throw e;
    } finally {
      entityManager.clear();
      entityManager.close();
    }
  }
}
