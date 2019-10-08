package guru.springframework.controllers;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.services.RecipeService;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class RecipeControllerTest {

  @Mock
  RecipeService recipeService;

  @Mock
  RecipeRepository recipeRepository;

  RecipeController recipeController;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    recipeController = new RecipeController(recipeService);
  }

  @Test
  public void testGetRecipe() throws Exception {
    Recipe recipe = new Recipe();
    recipe.setId(1L);

    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();

    when(recipeService.findById(anyLong())).thenReturn(recipe);

    mockMvc.perform(get("/recipe/1/show"))
        .andExpect(status().isOk())
        .andExpect(view().name("recipe/show"))
        .andExpect(model().attributeExists("recipe"));
  }

  @Test
  public void testGetNewRecipeForm() throws Exception {
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();

    mockMvc.perform(get("/recipe/new"))
        .andExpect(status().isOk())
        .andExpect(view().name("recipe/recipeform"))
        .andExpect(model().attributeExists("recipe"));
  }

  @Test
  public void testPostNewRecipeForm() throws Exception {
    RecipeCommand command = new RecipeCommand();
    command.setId(1L);

    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();

    when(recipeService.saveRecipeCommand(any())).thenReturn(command);

    mockMvc.perform(post("/recipe").contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("id", "").param("description", "something"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/recipe/1/show"));
  }

  @Test
  public void testGetRecipeNotFound() throws Exception{

    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();

    when(recipeService.findById(anyLong())).thenThrow(NotFoundException.class);

    mockMvc.perform(get("/recipe/1/show"))
        .andExpect(status().isNotFound())
        .andExpect(view().name("404error"));
  }



}
