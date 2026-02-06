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
import fr.univlille.store.repository.ClientRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import java.util.List;

@Controller
public class CommandeController {
    
    @Autowired
    private CommandeRepository commandeRepository;
    
    @Autowired
    private LigneRepository ligneRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @GetMapping("/store/commandes")
    public String listCommandes(HttpSession session, Model model) {
        Client sessionClient = (Client) session.getAttribute("client");
        if(sessionClient == null){
            System.out.println("Client non connecte");
            return "redirect:/store/login";
        }
        
        // je recharge le client sinon ca bug avec les commandes
        Optional<Client> clientOpt = clientRepository.findById(sessionClient.getEmail());
        if(!clientOpt.isPresent()){
            return "redirect:/store/login";
        }
        Client client = clientOpt.get();
        
        List<Commande> commandes = commandeRepository.findByClient(client);
        System.out.println("Nombre de commandes: " + commandes.size());
        model.addAttribute("client", client);
        model.addAttribute("commandes", commandes);
        return "commandes";
    }
    
    @PostMapping("/store/commandes/create")
    public String createCommande(HttpSession session) {
        Client client = (Client) session.getAttribute("client");
        if(client==null) return "redirect:/store/login";
        
        Commande commande = new Commande();
        commande.setClient(client);
        commandeRepository.save(commande);
        System.out.println("Commande creee avec id: " + commande.getId());
        return "redirect:/store/commandes";
    }
    
    @GetMapping("/store/commandes/{id}")
    public String voirCommande(@PathVariable Long id, HttpSession session, Model model) {
        Client client = (Client) session.getAttribute("client");
        if(client==null){
            return "redirect:/store/login";
        }
        
        Optional<Commande> cmd = commandeRepository.findById(id);
        if(cmd.isPresent()){
            Commande commande = cmd.get();
            if(commande.getClient().getEmail().equals(client.getEmail())){
                model.addAttribute("commande", commande);
                model.addAttribute("client", client);
                return "commande-detail";
            }
        }
        return "redirect:/store/commandes";
    }
    
    // ajouter ligne
    @PostMapping("/store/commandes/{id}/lignes/add")
    public String ajouterLigne(@PathVariable Long id,
                               @RequestParam String libelle,
                               @RequestParam int quantite,
                               @RequestParam double prixUnitaire,
                               HttpSession session) {
        Client client = (Client) session.getAttribute("client");
        if(client==null) return "redirect:/store/login";
        
        System.out.println("Ajout ligne: " + libelle + ", qte: " + quantite);
        Optional<Commande> cmd = commandeRepository.findById(id);
        if(cmd.isPresent()){
            Commande commande = cmd.get();
            if(commande.getClient().getEmail().equals(client.getEmail())){
                Ligne ligne = new Ligne();
                ligne.setLibelle(libelle);
                ligne.setQuantite(quantite);
                ligne.setPrixUnitaire(prixUnitaire);
                ligne.setCommande(commande);
                ligneRepository.save(ligne);
            }
        }
        return "redirect:/store/commandes/" + id;
    }
    
    @PostMapping("/store/commandes/{commandeId}/lignes/{ligneId}/delete")
    public String supprimerLigne(@PathVariable Long commandeId,
                                 @PathVariable Long ligneId,
                                 HttpSession session) {
        Client client = (Client) session.getAttribute("client");
        if(client == null) return "redirect:/store/login";
        
        Optional<Ligne> l = ligneRepository.findById(ligneId);
        if(l.isPresent()){
            Ligne ligne = l.get();
            if(ligne.getCommande().getClient().getEmail().equals(client.getEmail())){
                System.out.println("Suppression ligne id: " + ligneId);
                ligneRepository.delete(ligne);
            }
        }
        return "redirect:/store/commandes/" + commandeId;
    }
    

    @GetMapping("/store/commandes/{id}/print")
    public String imprimerCommande(@PathVariable Long id, HttpSession session, Model model) {
        Client client = (Client) session.getAttribute("client");
        if(client==null) return "redirect:/store/login";
        
        Optional<Commande> cmd = commandeRepository.findById(id);
        if(cmd.isPresent()){
            Commande commande = cmd.get();
            if(commande.getClient().getEmail().equals(client.getEmail())){
                model.addAttribute("commande", commande);
                model.addAttribute("client", client);
                return "commande-print";
            }
        }
        return "redirect:/store/commandes";
    }
}
