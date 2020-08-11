package com.tesi.datamasking.data.db.cedolini;

import com.tesi.datamasking.data.db.dipendenti.Dipendenti;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="cedolini_log")
public class CedoliniLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  public Long idDipendente;

  public String colonna1;

  public String colonna2;

  public String colonna3;

  public String colonna4;

  public String colonna5;

  public String colonna6;

  public String colonna7;

  public String colonna8;

  public String colonna9;

  public String colonna10;

  public Integer mese;

  public Integer anno;

  @ManyToOne
  private Dipendenti dipendente;


}
