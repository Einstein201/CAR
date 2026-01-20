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
        client.setNom(nom);
        client.setPrenom(prenom);

        client.setEmail(email);
        client.setPassword(password);
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
        Optional<Client> client = clientRepository.findById(email);

        // verif si client existe et bon mot de passe
        
        if (client.isPresent() && client.get().getPassword().equals(password)) {
            session.setAttribute("client", client.get());
            return "redirect:/store/home";
        }

        // sinon retour login
        return "redirect:/store/login";
    }
    
    @GetMapping("/store/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/store/home";
    }
}
