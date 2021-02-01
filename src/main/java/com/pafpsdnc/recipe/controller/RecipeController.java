package com.pafpsdnc.recipe.controller;

import com.pafpsdnc.recipe.model.Recipe;
import com.pafpsdnc.recipe.repository.RecipeRepository;
import com.pafpsdnc.recipe.exception.RecipeNotFound;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private DataSource dataSource;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Recipe findById(@PathVariable Integer id) throws RecipeNotFound {
        return recipeRepository.findById(id).orElseThrow(RecipeNotFound::new);
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Recipe> findRecipes() {
        return recipeRepository.findAll();
    }

    @GetMapping("/pdf")
    @ResponseStatus(HttpStatus.OK)
    public void pdf(HttpServletResponse response) throws JRException, SQLException, IOException {
        InputStream recipeStream = new ClassPathResource("recipe.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(recipeStream);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource.getConnection());
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment; filename=\"recipe.pdf\"");
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }

    @GetMapping("/pdf/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void pdf(HttpServletResponse response, @PathVariable Integer id) throws JRException, SQLException, IOException {
        InputStream recipeStream = new ClassPathResource("recipe.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(recipeStream);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment; filename=\"recipe.pdf\"");
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Recipe createRecipe() {
        Recipe recipe = new Recipe();
        recipe.setPortions(1000);
        recipe.setName("name");
        recipe.setDescription("description");

        recipeRepository.save(recipe);

        return recipe;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Recipe updateRecipe(@PathVariable("id") Integer id) throws RecipeNotFound {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(RecipeNotFound::new);

        recipe.setName("name updated !!!");
        recipe.setDescription("description updated !!!");
        recipeRepository.save(recipe);

        return recipe;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteRecipe(@PathVariable("id") Integer id) throws RecipeNotFound {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(RecipeNotFound::new);
        recipeRepository.delete(recipe);

        return "La recette " + recipe.getId() + " a été supprimée";
    }
}