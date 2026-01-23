package fr.univlille.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import fr.univlille.store.model.Client;
import fr.univlille.store.model.Commande;
import fr.univlille.store.model.Ligne;
import fr.univlille.store.repository.CommandeRepository;
import fr.univlille.store.repository.LigneRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class CommandeController {
    
    @Autowired
    private CommandeRepository commandeRepository;
    
    @Autowired
    private LigneRepository ligneRepository;
    
    // affichag la liste  commandes

    @GetMapping("/store/commandes")
    public String listCommandes(HttpSession session, Model model) {
        
        // recuperer  client connecte
        Client client = (Client) session.getAttribute("client");
        
        // si client pas connecte on redirige vers login
        if (client == null) {

            return "redirect:/store/login";
        }
        
        // on passe les donnees a la vue
        model.addAttribute("client", client);
        model.addAttribute("commandes", client.getCommandes());

        return "commandes";
    }
    
    // creation  nouvelle commande
    @PostMapping("/store/commandes/create")
    public String createCommande(HttpSession session) {
        
        // on recupere le client
        Client client = (Client) session.getAttribute("client");
        
        // verification si client connecte
        if (client == null) {
            return "redirect:/store/login";
        }
        
        // creation commande
        Commande commande = new Commande();
        commande.setClient(client);
        
        // sauvegarde en base de donnees
        commandeRepository.save(commande);
        
        return "redirect:/store/commandes";
    }
    
    // voir les details d'une commande
    @GetMapping("/store/commandes/{id}")
    public String voirCommande(@PathVariable Long id, HttpSession session, Model model) {
        // recuperer le client
        Client client = (Client) session.getAttribute("client");
        
        // verif connexion
        if (client == null) {
            return "redirect:/store/login";
        }
        
        // chercher la commande
        Optional<Commande> commandeOpt = commandeRepository.findById(id);
        
        if (commandeOpt.isPresent()) {
            Commande commande = commandeOpt.get();
            
            // verifier que la commande appartient au client
            if (commande.getClient().getEmail().equals(client.getEmail())) {
                model.addAttribute("commande", commande);
                model.addAttribute("client", client);
                return "commande-detail";
            }
        }
        
        // sl y a probleme retour a la list
        return "redirect:/store/commandes";
    }
    
    // ajouter une ligne a une commande
    @PostMapping("/store/commandes/{id}/lignes/add")
    public String ajouterLigne(@PathVariable Long id,
                               @RequestParam String libelle,
                               @RequestParam int quantite,
                               @RequestParam double prixUnitaire,
                               HttpSession session) {
       
        Client client = (Client) session.getAttribute("client");
        if (client == null) {
            return "redirect:/store/login";
        }
        
        // cherche la commande
        Optional<Commande> commandeOpt = commandeRepository.findById(id);
        
        if (commandeOpt.isPresent()) {
            Commande commande = commandeOpt.get();
            
            // verif que commande appartient au client
            if (commande.getClient().getEmail().equals(client.getEmail())) {
                // creer la ligne
                Ligne ligne = new Ligne();
                ligne.setLibelle(libelle);
                ligne.setQuantite(quantite);
                ligne.setPrixUnitaire(prixUnitaire);
                ligne.setCommande(commande);
                
                // sauvegarder
                ligneRepository.save(ligne);
            }
        }
        
        return "redirect:/store/commandes/" + id;
    }
    
    // supprimer une ligne
    @PostMapping("/store/commandes/{commandeId}/lignes/{ligneId}/delete")
    public String supprimerLigne(@PathVariable Long commandeId,
                                 @PathVariable Long ligneId,
                                 HttpSession session) {
        // verif connexion
        Client client = (Client) session.getAttribute("client");
        if (client == null) {
            return "redirect:/store/login";
        }
        
        // chercher la ligne
        Optional<Ligne> ligneOpt = ligneRepository.findById(ligneId);
        
        if (ligneOpt.isPresent()) {
            Ligne ligne = ligneOpt.get();
            
            // verif que la ligne appartient a une commande du client
            if (ligne.getCommande().getClient().getEmail().equals(client.getEmail())) {
                // supprimer la ligne
                ligneRepository.delete(ligne);
            }
        }
        
        return "redirect:/store/commandes/" + commandeId;
    }
    

    @GetMapping("/store/commandes/{id}/print")
    public String imprimerCommande(@PathVariable Long id, HttpSession session, Model model) {

        // recuperer le client
        Client client = (Client) session.getAttribute("client");
        
        // verif connexion

            if (client == null) {
                return "redirect:/store/login";
            }
        
        // chech commande
        Optional<Commande> commandeOpt = commandeRepository.findById(id);
        
        if (commandeOpt.isPresent()) {
            Commande commande = commandeOpt.get();
            
            // verifier que la commande appartient au client
            if (commande.getClient().getEmail().equals(client.getEmail())) {
                model.addAttribute("commande", commande);
                model.addAttribute("client", client);
                return "commande-print";
            }
        }
        
        // si probleme retour a la liste
        return "redirect:/store/commandes";
    }
}
