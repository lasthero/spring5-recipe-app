package guru.springframework.controllers;

import static org.junit.Assert.*;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
public class IndexControllerTest {

  private IndexController indexController;

  @Mock
  RecipeService recipeService;

  @Mock
  Model model;

  @Before
  public void setUp() throws Exception {

    MockitoAnnotations.initMocks(this);
    indexController = new IndexController(recipeService);
  }

  @Test
  public void getIndexPage() {
    Set<Recipe> recipeData = new HashSet<>(Arrays.asList(new Recipe()));
    when(recipeService.getRecipes()).thenReturn(recipeData);
    assertEquals("index", indexController.getIndexPage(model));
    verify(recipeService, times(1)).getRecipes();
    verify(model, times(1)).addAttribute("recipes", recipeData);
  }
}