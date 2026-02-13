package fr.univlille.store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/store/home")
    public String home(HttpSession session, Model model) {

        // recup client de la session
        Object client = session.getAttribute("client");
        model.addAttribute("client", client);
        return "home";
    }
}
