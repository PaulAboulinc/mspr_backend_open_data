package com.pafpsdnc.recipe.controller;

import com.pafpsdnc.recipe.model.Recipe;
import com.pafpsdnc.recipe.repository.RecipeRepository;
import com.pafpsdnc.recipe.exception.RecipeNotFound;
import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/recipes")
public class RecipeController {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private DataSource dataSource;

    private final Logger logger = LoggerFactory.getLogger(RecipeController.class);

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Recipe findById(@PathVariable Integer id) throws RecipeNotFound {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(RecipeNotFound::new);
        String log = "Show : " + recipe.toString();
        logger.trace(log);

        return recipe;
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Recipe> findRecipes() {
        String log = "Json with all recipes";
        logger.trace(log);

        return recipeRepository.findAll();
    }

    @PostMapping("/pdf")
    @ResponseStatus(HttpStatus.OK)
    public void recipesPdf(HttpServletResponse response, @RequestBody Map<String, Object> responseData) throws JRException, SQLException, IOException {
        InputStream recipeStream = new ClassPathResource("recipe.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(recipeStream);

        String log = "PDF with all recipes";
        if (responseData.containsKey("ids")) {
            ArrayList<Integer> idsList = (ArrayList<Integer>) responseData.get("ids");
            if (!idsList.isEmpty()) {
                String ids = idsList.stream().distinct().sorted().map(String::valueOf).collect(Collectors.joining(","));
                log = "PDF of recipes : " + ids;
            }
        }

        logger.trace(log);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, responseData, dataSource.getConnection());
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

        String log = "Create : " + recipe.toString();
        logger.trace(log);

        return recipe;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Recipe updateRecipe(@PathVariable("id") Integer id) throws RecipeNotFound {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(RecipeNotFound::new);

        recipe.setName("name updated !!!");
        recipe.setDescription("description updated !!!");
        recipeRepository.save(recipe);

        String log = "Update : " + recipe.toString();
        logger.trace(log);

        return recipe;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteRecipe(@PathVariable("id") Integer id) throws RecipeNotFound {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(RecipeNotFound::new);
        recipeRepository.delete(recipe);

        String log = "Delete : " + recipe.toString();
        logger.trace(log);

        return "La recette " + recipe.getId() + " a bien été supprimée";
    }
}
