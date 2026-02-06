package fr.univlille.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import fr.univlille.store.model.Client;
import fr.univlille.store.repository.ClientRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class ClientController {
    
    @Autowired
    private ClientRepository clientRepository;
    
    @GetMapping("/store/register")
    public String showRegister() {
        return "register";
    }
    
    @PostMapping("/store/register")
    public String register(@RequestParam String email, 
                          @RequestParam String password,
                          @RequestParam String nom,
                          @RequestParam String prenom) {
        
        Client client = new Client();
        client.setEmail(email);
        client.setPassword(password);
        client.setNom(nom);
        client.setPrenom(prenom);
        clientRepository.save(client);
        System.out.println("Nouveau client cree: " + email);
        
        return "redirect:/store/home";
    }
    
    @GetMapping("/store/login")
    public String showLogin() {
        return "login";
    }
    
    @PostMapping("/store/login")
    public String login(@RequestParam String email, 
                       @RequestParam String password, 
                       HttpSession session) {
        System.out.println("Tentative de connexion pour: " + email);
        Optional<Client> c = clientRepository.findById(email);
        if(c.isPresent()) {
            Client client = c.get();
            if(client.getPassword().equals(password)){
                System.out.println("Connexion reussie pour " + email);
                session.setAttribute("client", client);
                return "redirect:/store/home";
            }
        }
        System.out.println("Echec connexion");
        return "redirect:/store/login";
    }
    
    @GetMapping("/store/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/store/home";
    }
}
