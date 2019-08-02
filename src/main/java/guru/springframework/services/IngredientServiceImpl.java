package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
  private final RecipeToRecipeCommand recipeToRecipeCommand;
  private final IngredientToIngredientCommand ingredientToIngredientCommand;
  private final IngredientCommandToIngredient ingredientCommandToIngredient;
  private final RecipeRepository recipeRepository;
  private final UnitOfMeasureRepository unitOfMeasureRepository;

  @Autowired
  public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
      IngredientCommandToIngredient ingredientCommandToIngredient,
      RecipeRepository recipeRepository,
      UnitOfMeasureRepository unitOfMeasureRepository,
      RecipeToRecipeCommand recipeToRecipeCommand) {
    this.ingredientToIngredientCommand = ingredientToIngredientCommand;
    this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    this.recipeRepository = recipeRepository;
    this.unitOfMeasureRepository = unitOfMeasureRepository;
    this.recipeToRecipeCommand = recipeToRecipeCommand;
  }

  @Override
  public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
    Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
    if (!recipeOptional.isPresent()) {
      //log error
      log.error("Recipe not found");
    }

    Optional<IngredientCommand> ingredientCommandOptional = recipeOptional.get().getIngredients().stream()
        .filter(ingredient -> ingredient.getId().equals(ingredientId))
        .map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

    if (!ingredientCommandOptional.isPresent()) {
      //not found
    }

    return ingredientCommandOptional.get();
  }

  @Override
  public IngredientCommand saveIngredientCommand(IngredientCommand command) {
    Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());
    if (!recipeOptional.isPresent()) {
      log.error("Recipe not found for id: " + command.getRecipeId());
      return new IngredientCommand();
    }
    else {
      Recipe recipe = recipeOptional.get();

      Optional<Ingredient> ingredientOptional = recipe.getIngredients()
          .stream()
          .filter(ingredient -> ingredient.getId().equals(command.getId()))
          .findFirst();
      if (ingredientOptional.isPresent()) {
        Ingredient ingredientToUpdate = ingredientOptional.get();
        ingredientToUpdate.setDescription(command.getDescription());
        ingredientToUpdate.setAmount(command.getAmount());
        ingredientToUpdate
            .setUnitOfMeasure(unitOfMeasureRepository.findById(command.getUnitOfMeasure().getId())
                .orElseThrow(() -> new RuntimeException("UOM Not Found")));
      } else {
        //new ingredient
        Ingredient newIngredient = ingredientCommandToIngredient.convert(command);
        newIngredient.setRecipe(recipe);
        recipe.addIngredient(newIngredient);
      }
      Recipe savedRecipe = recipeRepository.save(recipe);

      Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
          .filter(recipeIngredient -> recipeIngredient.getId().equals(command.getId()))
          .findFirst();

      if (!savedIngredientOptional.isPresent()) {
        savedIngredientOptional = savedRecipe.getIngredients().stream()
            .filter(ingredient -> ingredient.getDescription().equals(command.getDescription()))
            .filter(ingredient -> ingredient.getAmount().equals(command.getAmount()))
            .filter(ingredient -> ingredient.getUnitOfMeasure().getId().equals(command.getUnitOfMeasure().getId()))
            .findFirst();
      }

      return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
    }
  }

  @Transactional
  @Override
  public RecipeCommand deleteByRecipeIdAndId(Long recipeId, Long id) {
    Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
    if (!recipeOptional.isPresent()) {
      log.error("Recipe not found");
      return new RecipeCommand();
    }
    else {
      //find ingredient
      Recipe recipe = recipeOptional.get();
      Optional<Ingredient> ingredientOptional = recipe.getIngredients()
          .stream()
          .filter(ingredient -> ingredient.getId().equals(id))
          .findFirst();
      if (!ingredientOptional.isPresent()) {
        log.error("Ingredient not found");
        return new RecipeCommand();
      }
      else {
        recipe.getIngredients().remove(ingredientOptional.get());
        Recipe savedRecipe = recipeRepository.save(recipe);
        return recipeToRecipeCommand.convert(savedRecipe);
      }
    }
  }
}
