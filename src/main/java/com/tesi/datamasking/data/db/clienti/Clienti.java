package com.tesi.datamasking.data.db.clienti;

import com.tesi.datamasking.data.db.dipendenti.Dipendenti;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="clienti")
public class Clienti {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  public String ragioneSociale;

  public String partitaIva;

  public String telefono;

  public String indirizzo;

  public String citta;

  public String regione;

  public String cap;

  @OneToMany()
  @JoinColumn(name="idAzienda")
  private List<Dipendenti> dipendentiList;

  public List<Dipendenti> getDipendentiList() {
    return dipendentiList;
  }

  public void setDipendentiList(List<Dipendenti> dipendentiList) {
    this.dipendentiList = dipendentiList;
  }

}
