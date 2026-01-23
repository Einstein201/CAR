package fr.univlille.store.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Commande {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Client client;
    
    @OneToMany(mappedBy = "commande")
    private List<Ligne> lignes = new ArrayList<>();
    
    public Commande() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    public List<Ligne> getLignes() {
        return lignes;
    }
    
    public void setLignes(List<Ligne> lignes) {
        this.lignes = lignes;
    }
    
    public double getTotal() {
        double total = 0;
        for(Ligne ligne : lignes){
            total += ligne.getTotal();
        }
        return total;
    }
}
