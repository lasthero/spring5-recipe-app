package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

  private final RecipeRepository recipeRepository;
  private final RecipeToRecipeCommand recipeToRecipeCommand;
  private final RecipeCommandToRecipe recipeCommandToRecipe;

  @Autowired
  public RecipeServiceImpl(RecipeRepository recipeRepository,
      RecipeToRecipeCommand recipeToRecipeCommand,
      RecipeCommandToRecipe recipeCommandToRecipe) {
    this.recipeRepository = recipeRepository;
    this.recipeToRecipeCommand = recipeToRecipeCommand;
    this.recipeCommandToRecipe = recipeCommandToRecipe;
  }
  @Override
  public Set<Recipe> getRecipes() {
    Set<Recipe> recipeSet = new HashSet<>();
    recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
    return  recipeSet;
  }

  @Override
  @Transactional
  public RecipeCommand saveRecipeCommand(RecipeCommand command) {
    Recipe recipe = recipeCommandToRecipe.convert(command);
    Recipe savedRecipe = recipeRepository.save(recipe);

    return  recipeToRecipeCommand.convert(savedRecipe);
  }

  @Override
  public Recipe findById(Long id) {
    Optional<Recipe> recipeOptional = recipeRepository.findById(id);

    if (!recipeOptional.isPresent()) {
      throw new RuntimeException("Recipe not found");
    }
    return recipeOptional.get();
  }

  @Transactional
  @Override
  public RecipeCommand findCommandById(Long id) {
    Recipe recipe = this.findById(id);

    return recipeToRecipeCommand.convert(recipe);
  }

}
