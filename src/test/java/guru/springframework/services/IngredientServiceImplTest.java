package guru.springframework.services;


import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class IngredientServiceImplTest {

  IngredientService ingredientService;
  IngredientToIngredientCommand ingredientToIngredientCommand;
  IngredientCommandToIngredient ingredientCommandToIngredient;

  @Mock
  RecipeRepository recipeRepository;

  @Mock
  UnitOfMeasureRepository unitOfMeasureRepository;



  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    ingredientService = new IngredientServiceImpl(ingredientToIngredientCommand, ingredientCommandToIngredient, recipeRepository, unitOfMeasureRepository);

  }

  @Test
  public void findByRecipeIdAndIngredientIdHappyPath() {
    //given
    Recipe recipe = new Recipe();
    recipe.setId(1L);
    Ingredient ingredient1 = new Ingredient();
    ingredient1.setId(1L);
    Ingredient ingredient2 = new Ingredient();
    ingredient2.setId(2L);
    Ingredient ingredient3 = new Ingredient();
    ingredient3.setId(3L);
    recipe.addIngredient(ingredient1);
    recipe.addIngredient(ingredient2);
    recipe.addIngredient(ingredient3);

    Optional<Recipe> recipeOptional = Optional.of(recipe);

    //when
    when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

    //then
    IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(1L, 2L);

    assertNotNull("ingredient not found", ingredientCommand);
    assertEquals(Long.valueOf(2L), ingredientCommand.getId());
    assertEquals(Long.valueOf(1L), ingredientCommand.getRecipeId());
    verify(recipeRepository, times(1)).findById(anyLong());
  }

  @Test
  public void saveIngredientCommandTest() {
    //given
    IngredientCommand command = new IngredientCommand();
    command.setId(2L);
    command.setRecipeId(1L);

    Optional<Recipe> recipeOptional = Optional.of(new Recipe());

    Recipe savedRecipe = new Recipe();
    savedRecipe.addIngredient(new Ingredient());
    savedRecipe.getIngredients().iterator().next().setId(2L);

    when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
    when(recipeRepository.save(any())).thenReturn(savedRecipe);
    //when
    IngredientCommand savedIngredientCommand = ingredientService.saveIngredientCommand(command);

    //then
    assertEquals(Long.valueOf(2L), savedIngredientCommand.getId());
    verify(recipeRepository, times(1)).findById(anyLong());
    verify(recipeRepository, times(1)).save(any(Recipe.class));
  }

}
