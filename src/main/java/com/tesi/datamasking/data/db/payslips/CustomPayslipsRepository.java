package com.tesi.datamasking.data.db.payslips;

import java.util.List;

public interface CustomPayslipsRepository {

  void insertWithBatchInsert(List<Payslips> payslipsList);

}
