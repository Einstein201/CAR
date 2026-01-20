package fr.univlille.store.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Client {
    
    @Id
    private String email;
    private String password;
    private String nom;
    private String prenom;
    
    @OneToMany(mappedBy = "client")
    private List<Commande> commandes;
    
    public Client() {
    }
       public String getNom() {
        return nom;
    }
    
     public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
        public String getPassword() {
            return password;
        }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public List<Commande> getCommandes() {
        return commandes;
    }
    
    public void setCommandes(List<Commande> commandes) {
        this.commandes = commandes;
    }
 
}
