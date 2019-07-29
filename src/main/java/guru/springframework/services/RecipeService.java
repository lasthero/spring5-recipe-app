package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import java.util.Set;

public interface RecipeService {

  Set<Recipe> getRecipes();

  Recipe findById(Long id);

  Boolean deleteById(Long id);

  RecipeCommand findCommandById(Long id);

  RecipeCommand saveRecipeCommand(RecipeCommand command);

}
