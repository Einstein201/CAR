package fr.univlille.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.univlille.store.model.Client;
import fr.univlille.store.repository.ClientRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
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
                          @RequestParam String prenom,
                          HttpSession session) {
        if (clientRepository.existsById(email)) {
            logger.warn("Echec inscription: email deja utilise {}", email);
            return "redirect:/store/register?error=email_exists";
        }

        Client client = new Client();
        client.setEmail(email);
        client.setPassword(passwordEncoder.encode(password));
        client.setNom(nom);
        client.setPrenom(prenom);
        clientRepository.save(client);
        session.setAttribute("client", client);
        logger.info("Nouveau client cree et connecte: {}", email);

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
        logger.info("Tentative de connexion pour: {}", email);
        Optional<Client> c = clientRepository.findById(email);
        if(c.isPresent()) {
            Client client = c.get();
            if(passwordEncoder.matches(password, client.getPassword())){
                logger.info("Connexion reussie pour {}", email);
                session.setAttribute("client", client);
                return "redirect:/store/home";
            }
        }
        logger.warn("Echec connexion pour {}", email);
        return "redirect:/store/login?error=true";
    }
    
    @GetMapping("/store/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/store/home";
    }
}
