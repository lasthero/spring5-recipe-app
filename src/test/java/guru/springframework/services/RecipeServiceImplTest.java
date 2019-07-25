package guru.springframework.services;

import static org.junit.Assert.*;

import guru.springframework.domain.Recipe;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeServiceImplTest {

  RecipeService recipeService;

  @Mock
  RecipeRepository recipeRepository;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    recipeService = new RecipeServiceImpl(recipeRepository);
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
}