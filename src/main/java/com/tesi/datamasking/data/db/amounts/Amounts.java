package com.tesi.datamasking.data.db.amounts;

import com.tesi.datamasking.context.DataCrypt;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "amounts")
public class Amounts {

  @Id
  public String code;

  @DataCrypt(dataType = DataCrypt.DataType.AMOUNT)
  public BigDecimal price;

  public String job;

  public BigDecimal originalPrice;

}
