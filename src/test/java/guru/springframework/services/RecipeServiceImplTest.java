package guru.springframework.services;

import static org.junit.Assert.*;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositories.RecipeRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeServiceImplTest {

  RecipeService recipeService;

  @Mock
  RecipeRepository recipeRepository;

  @Mock
  RecipeToRecipeCommand recipeToRecipeCommand;

  @Mock
  RecipeCommandToRecipe recipeCommandToRecipe;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    recipeService = new RecipeServiceImpl(recipeRepository, recipeToRecipeCommand, recipeCommandToRecipe);
  }

  @Test
  public void getRecipeByIdTest() {
    Recipe recipe = new Recipe();
    recipe.setId(1L);
    Optional<Recipe> recipeOptional = Optional.of(recipe);
    when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

    Recipe recipeReturned = recipeService.findById(1L);

    assertNotNull("Null recipe", recipeReturned);
    verify(recipeRepository, times(1)).findById(anyLong());
    verify(recipeRepository, never()).findAll();
  }

  @Test
  public void getRecipes() {

    Recipe recipe = new Recipe();
    HashSet<Recipe> recipeData = new HashSet<>();
    recipeData.add(recipe);

    when(recipeRepository.findAll()).thenReturn(recipeData);
    Set<Recipe> recipes = recipeService.getRecipes();

    assertEquals(recipes.size(), 1);
    verify(recipeRepository, times(1)).findAll();
  }

  @Test
  public void getRecipeCommandByIdTest() {
    RecipeCommand command = new RecipeCommand();
    command.setId(1L);
    Recipe recipe = new Recipe();
    recipe.setId(1L);
    Optional<Recipe> recipeOptional = Optional.of(recipe);

    when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
    when(recipeToRecipeCommand.convert(recipe)).thenReturn(command);

    RecipeCommand commandReturned = recipeService.findCommandById(1L);

    assertNotNull("Null recipe command", commandReturned);
    assertEquals(command.getId(), commandReturned.getId());
    verify(recipeRepository, times(1)).findById(anyLong());
    verify(recipeRepository, never()).findAll();
  }

  @Test
  public void deleteByIDTest() {
    Recipe recipe = new Recipe();
    recipe.setId(1l);

    doNothing().when(recipeRepository).deleteById(anyLong());
    when(recipeRepository.findById(eq(1L))).thenReturn(Optional.of(recipe));
    when(recipeRepository.findById(eq(0L))).thenReturn(Optional.empty());

    Boolean deleteExistingId = recipeService.deleteById(1L);
    Boolean deleteNonExistingId = recipeService.deleteById(0L);

    assertEquals(true, deleteExistingId);
    assertEquals(false, deleteNonExistingId);
    verify(recipeRepository, times(2)).findById(anyLong());
  }


  @Test(expected = NotFoundException.class)
  public void getRecipeByIdTestNotFound() throws Exception {

    Optional<Recipe> recipeOptional = Optional.empty();
    when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

    Recipe recipe = recipeService.findById(1L);

  }
}