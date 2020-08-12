package com.tesi.datamasking.data.db.dipendenti;

import com.tesi.datamasking.context.DataCrypt;
import com.tesi.datamasking.data.db.clienti.Clienti;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dipendenti")
public class Dipendenti {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @ManyToOne
  @JoinColumn(name = "idAzienda")
  public Clienti clienti;

  @DataCrypt
  public String nome;

  @DataCrypt
  public String cognome;

  public String telefono;

  @DataCrypt(dataType = DataCrypt.DataType.EMAIL)
  public String email;

  public String indirizzo;

  public String citta;

  public String regione;

  @DataCrypt(dataType = DataCrypt.DataType.NUMBER)
  public Integer cap;

  //  @OneToMany(mappedBy = "dipendente")
  //  private List<CedoliniLog> cedoliniLogs;
  //
  //  public List<CedoliniLog> getCedoliniLogs() {
  //    return cedoliniLogs;
  //  }
  //
  //  public void setCedoliniLogs(List<CedoliniLog> cedoliniLogs) {
  //    this.cedoliniLogs = cedoliniLogs;
  //  }
}
