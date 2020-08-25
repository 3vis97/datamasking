package com.tesi.datamasking.data.db.customers;

import java.util.List;

public interface CustomCustomerRepository {

  void insertWithBatchInsert(List<Customers> customerList,
      int batchSize);

}
