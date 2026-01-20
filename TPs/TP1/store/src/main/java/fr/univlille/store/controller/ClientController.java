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
        // creation nouveau client
        Client client = new Client();
        client.setEmail(email);
        client.setPassword(password);
        client.setNom(nom);
        client.setPrenom(prenom);
        
        // enregistremnt dans la base
        clientRepository.save(client);
        
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

        // re cherche  client par email
        Optional<Client> clientOpt = clientRepository.findById(email);

        // verif si l client existe
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();

            // verificaton mot de passe
            if (client.getPassword().equals(password)) {
                // add  client on the session
                session.setAttribute("client", client);
                return "redirect:/store/home";
            }
        }

        // si erreur retour au login
        return "redirect:/store/login";
    }
    
    @GetMapping("/store/logout")
    public String logout(HttpSession session) {
        
        // detruir la session
        session.invalidate();
        return "redirect:/store/home";
    }
}
