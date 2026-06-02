package org.example.nidabutik.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id) {
        return "redirect:/product.html?id=" + id;
    }
}
