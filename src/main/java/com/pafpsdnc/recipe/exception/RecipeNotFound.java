package com.pafpsdnc.recipe.exception;

import javassist.NotFoundException;

public class RecipeNotFound extends NotFoundException {
    public RecipeNotFound(String msg) {
        super(msg);
    }

    public RecipeNotFound() {
        super("La recette n'a pas été trouvée");
    }
}
