package com.fpoly.shoes.controller;


import com.fpoly.shoes.model.Category;
import com.fpoly.shoes.model.Product;
import com.fpoly.shoes.services.CategoryServices;
import com.fpoly.shoes.services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryServices categoryServices;

    @Autowired
    private ProductServices productServices;

    @GetMapping({"/", ""})
    public String listCategory(Model model) {
        List<Category> listCategory = categoryServices.listAll();
        model.addAttribute("categories", listCategory);
        return "category/index";
    }

    @GetMapping("/view/{id}")
    public String getCategoryById(@PathVariable Long id, Model model) {
        Category category = categoryServices.getCategoryById(id);
        if (category != null) {
            model.addAttribute("category", category);
            return "category/view";
        } else {
            return "error/404";
        }
    }

    @GetMapping("/create")
    public String showCreateCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "category/create";
    }

    @PostMapping("/create")
    public String saveCategory(@ModelAttribute Category category) {
        categoryServices.saveCategory(category);
        return "redirect:/category";
    }

    @GetMapping("/update/{id}")
    public String showUpdateCategoryForm(@PathVariable Long id, Model model) {
        Category category = categoryServices.getCategoryById(id);
        if (category != null) {
            model.addAttribute("category", category);
            return "category/update";
        } else {
            return "error/404";
        }
    }

    @PostMapping("/update")
    public String updateCategory(@ModelAttribute Category category) {
        Category updatedCategory = categoryServices.updateCategory(category);
        if (updatedCategory != null) {
            return "redirect:/category";
        } else {
            return "error/404";
        }
    }

    @GetMapping("/delete/{id}")
    public String confirmDeleteCategory(@PathVariable Long id, Model model) {
        Category category = categoryServices.getCategoryById(id);
        List<Product> products = productServices.findByCategoryId(id);
        if (products != null && !products.isEmpty()) {
            model.addAttribute("category", category);
            model.addAttribute("products", products);
            return "category/confirm-delete";
        } else {
            categoryServices.deleteCategory(id);
            return "redirect:/category";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, @RequestParam boolean confirm) {
        if (confirm) {
            productServices.deleteByCategoryId(id);
            categoryServices.deleteCategory(id);
        }
        return "redirect:/category";
    }
}