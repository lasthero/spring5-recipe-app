package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
  private final IngredientToIngredientCommand ingredientToIngredientCommand;
  private final IngredientCommandToIngredient ingredientCommandToIngredient;
  private final RecipeRepository recipeRepository;
  private final UnitOfMeasureRepository unitOfMeasureRepository;

  @Autowired
  public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
      IngredientCommandToIngredient ingredientCommandToIngredient,
      RecipeRepository recipeRepository,
      UnitOfMeasureRepository unitOfMeasureRepository) {
    this.ingredientToIngredientCommand = ingredientToIngredientCommand;
    this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    this.recipeRepository = recipeRepository;
    this.unitOfMeasureRepository = unitOfMeasureRepository;
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
        recipe.addIngredient(ingredientCommandToIngredient.convert(command));
      }
      Recipe savedRecipe = recipeRepository.save(recipe);

      return ingredientToIngredientCommand.convert(savedRecipe.getIngredients().stream()
        .filter(recipeIngredient -> recipeIngredient.getId().equals(command.getId()))
          .findFirst().get());
    }
  }
}
