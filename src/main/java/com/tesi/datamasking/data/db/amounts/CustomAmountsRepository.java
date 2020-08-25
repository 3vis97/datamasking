package com.tesi.datamasking.data.db.amounts;

import java.util.List;

public interface CustomAmountsRepository {

  void insertWithBatchInsert(List<Amounts> amountsList);

}
