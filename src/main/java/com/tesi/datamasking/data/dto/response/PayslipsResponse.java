package com.tesi.datamasking.data.dto.response;

import com.tesi.datamasking.data.db.payslips.PayslipsDto;

import java.util.List;

public class PayslipsResponse extends GenericRestResponse {

  public List<PayslipsDto> payslipList;

  public PayslipsDto payslip;
}
