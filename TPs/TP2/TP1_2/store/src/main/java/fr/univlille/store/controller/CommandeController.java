package fr.univlille.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.univlille.store.model.Client;
import fr.univlille.store.model.Commande;
import fr.univlille.store.model.Ligne;
import fr.univlille.store.model.Produit;
import fr.univlille.store.repository.CommandeRepository;
import fr.univlille.store.repository.LigneRepository;
import fr.univlille.store.repository.ClientRepository;
import fr.univlille.store.repository.ProduitRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import java.util.List;

@Controller
public class CommandeController {

    private static final Logger logger = LoggerFactory.getLogger(CommandeController.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.kafka.topic.order-submitted}")
    private String kafkaTopic;

    @Autowired
    private CommandeRepository commandeRepository;
    
    @Autowired
    private LigneRepository ligneRepository;
    
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProduitRepository produitRepository;
    
    @GetMapping("/store/commandes")
    public String listCommandes(HttpSession session, Model model) {
        Client sessionClient = (Client) session.getAttribute("client");
        if(sessionClient == null){
            logger.warn("Client non connecte");
            return "redirect:/store/login";
        }
        
        // je recharge le client sinon ca bug avec les commandes
        Optional<Client> clientOpt = clientRepository.findById(sessionClient.getEmail());
        if(!clientOpt.isPresent()){
            return "redirect:/store/login";
        }
        Client client = clientOpt.get();
        
        List<Commande> commandes = commandeRepository.findByClient(client);
        logger.info("Nombre de commandes: {}", commandes.size());
        model.addAttribute("client", client);
        model.addAttribute("commandes", commandes);
        return "commandes";
    }
    
    @PostMapping("/store/commandes/create")
    public String createCommande(HttpSession session) {
        Client sessionClient = (Client) session.getAttribute("client");
        if(sessionClient == null) return "redirect:/store/login";

        Optional<Client> clientOpt = clientRepository.findById(sessionClient.getEmail());
        if(!clientOpt.isPresent()) return "redirect:/store/login";

        Commande commande = new Commande();
        commande.setClient(clientOpt.get());
        commandeRepository.save(commande);
        logger.info("Commande creee avec id: {}", commande.getId());
        return "redirect:/store/commandes/" + commande.getId();
    }
    
    @GetMapping("/store/commandes/{id}")
    public String voirCommande(@PathVariable Long id, HttpSession session, Model model) {
        Client sessionClient = (Client) session.getAttribute("client");
        if(sessionClient == null) return "redirect:/store/login";

        Optional<Client> clientOpt = clientRepository.findById(sessionClient.getEmail());
        if(!clientOpt.isPresent()) return "redirect:/store/login";
        Client client = clientOpt.get();

        Optional<Commande> cmd = commandeRepository.findById(id);
        if(cmd.isPresent()){
            Commande commande = cmd.get();
            if(commande.getClient().getEmail().equals(client.getEmail())){
                model.addAttribute("commande", commande);
                model.addAttribute("client", client);
                model.addAttribute("produits", produitRepository.findAll());
                return "commande-detail";
            }
        }
        return "redirect:/store/commandes";
    }
    
    @PostMapping("/store/commandes/{id}/lignes/add")
    public String ajouterLigne(@PathVariable Long id,
                               @RequestParam Long produitId,
                               @RequestParam int quantite,
                               HttpSession session) {
        Client client = (Client) session.getAttribute("client");
        if(client==null) return "redirect:/store/login";

        Optional<Produit> produitOpt = produitRepository.findById(produitId);
        if(!produitOpt.isPresent() || quantite <= 0) {
            logger.warn("Echec ajout ligne: donnees invalides");
            return "redirect:/store/commandes/" + id + "?error=invalid_line";
        }

        Produit produit = produitOpt.get();
        if(quantite > produit.getQuantiteStock()) {
            logger.warn("Stock insuffisant pour {}: demande={}, stock={}", produit.getLibelle(), quantite, produit.getQuantiteStock());
            return "redirect:/store/commandes/" + id + "?error=stock_insuffisant";
        }

        Optional<Commande> cmd = commandeRepository.findById(id);
        if(cmd.isPresent()){
            Commande commande = cmd.get();
            if(commande.getClient().getEmail().equals(client.getEmail())){
                Ligne ligne = new Ligne();
                ligne.setLibelle(produit.getLibelle());
                ligne.setQuantite(quantite);
                ligne.setPrixUnitaire(produit.getPrixUnitaire());
                ligne.setCommande(commande);
                ligneRepository.save(ligne);

                produit.setQuantiteStock(produit.getQuantiteStock() - quantite);
                produitRepository.save(produit);
                logger.info("Ajout ligne: {}, qte: {}, stock restant: {}", produit.getLibelle(), quantite, produit.getQuantiteStock());

                String kafkaMessage = commande.getId() + ";" + produit.getLibelle().toLowerCase() + ";" + quantite;
                kafkaTemplate.send(kafkaTopic, kafkaMessage);
                logger.info("Message Kafka publie: {}", kafkaMessage);
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
                // restituer le stock
                produitRepository.findAll().stream()
                    .filter(p -> p.getLibelle().equals(ligne.getLibelle()))
                    .findFirst()
                    .ifPresent(p -> {
                        p.setQuantiteStock(p.getQuantiteStock() + ligne.getQuantite());
                        produitRepository.save(p);
                    });
                logger.info("Suppression ligne id: {}", ligneId);
                ligneRepository.delete(ligne);
            }
        }
        return "redirect:/store/commandes/" + commandeId;
    }
    

    @GetMapping("/store/commandes/{id}/print")
    public String imprimerCommande(@PathVariable Long id, HttpSession session, Model model) {
        Client sessionClient = (Client) session.getAttribute("client");
        if(sessionClient == null) return "redirect:/store/login";

        Optional<Client> clientOpt = clientRepository.findById(sessionClient.getEmail());
        if(!clientOpt.isPresent()) return "redirect:/store/login";
        Client client = clientOpt.get();

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
