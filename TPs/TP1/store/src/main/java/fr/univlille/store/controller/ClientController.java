package fr.univlille.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import fr.univlille.store.model.Client;
import fr.univlille.store.repository.ClientRepository;

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
        return "redirect:/store/home";
    }
}
