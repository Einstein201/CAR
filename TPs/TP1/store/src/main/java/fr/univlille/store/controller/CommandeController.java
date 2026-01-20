package fr.univlille.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import fr.univlille.store.model.Client;
import fr.univlille.store.model.Commande;
import fr.univlille.store.repository.CommandeRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class CommandeController {
    
    @Autowired
    private CommandeRepository commandeRepository;
    
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
}
