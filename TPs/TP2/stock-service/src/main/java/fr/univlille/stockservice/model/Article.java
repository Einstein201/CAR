package fr.univlille.stockservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Article {

    @Id
    private String libelle;
    private int quantiteStock;

    public Article() {
    }

    public Article(String libelle, int quantiteStock) {
        this.libelle = libelle;
        this.quantiteStock = quantiteStock;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(int quantiteStock) {
        this.quantiteStock = quantiteStock;
    }
}
