package com.codegym.controller;

import com.codegym.model.Cart;
import com.codegym.model.Product;
import com.codegym.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@SessionAttributes("cart")
public class ProductController {
    @Autowired
    private IProductService productService;

    @ModelAttribute("cart")
    public Cart setupCart() {
        return new Cart();
    }

    @GetMapping("/shop")
    public ModelAndView showShop() {
        ModelAndView modelAndView = new ModelAndView("/shop");
        modelAndView.addObject("products", productService.findAll());
        return modelAndView;
    }

    @GetMapping("/products/{id}")
    public ModelAndView showProduct(
            @PathVariable Long id
    ) {
        Optional<Product> productOptional = productService.findById(id);

        ModelAndView modelAndView = new ModelAndView("/show");
        modelAndView.addObject("product", productOptional.orElse(null));
        return modelAndView;
    }

    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id,
                            @ModelAttribute Cart cart,
                            @RequestParam("action") String action,
                             RedirectAttributes redirectAttributes) {
        Optional<Product> productOptional = productService.findById(id);
        if (!productOptional.isPresent()) {
            return "/error_404";
        }

        cart.addProduct(productOptional.get());
        redirectAttributes.addFlashAttribute("success", "Add to cart successfully");
        if (action.equals("show")) {
            return "redirect:/shopping-cart";
        }
        return "redirect:/shop";
    }

    @GetMapping("/minus/{id}")
    public String minusFromCart(@PathVariable Long id,
                            @ModelAttribute Cart cart,
                            @RequestParam("action") String action,
                            RedirectAttributes redirectAttributes) {
        Optional<Product> productOptional = productService.findById(id);
        if (!productOptional.isPresent()) {
            return "/error_404";
        }

        cart.minusProduct(productOptional.get());
        redirectAttributes.addFlashAttribute("success", "Minus from cart successfully");
        if (action.equals("show")) {
            return "redirect:/shopping-cart";
        }
        return "redirect:/shop";
    }
}
