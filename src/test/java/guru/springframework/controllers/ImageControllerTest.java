package guru.springframework.controllers;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ImageControllerTest {

  @Mock
  RecipeService recipeService;

  ImageController imageController;

  MockMvc mockMvc;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    imageController = new ImageController(recipeService);
    mockMvc = MockMvcBuilders.standaloneSetup(imageController)
        .setControllerAdvice(new ControllerExceptionHandler())
        .build();
  }

  @Test
  public void testGetImageNumberFormatException() throws Exception {
    mockMvc.perform(get("/recipe/asdf/recipeimage"))
        .andExpect(status().isBadRequest())
        .andExpect(view().name("400badRequest"));

  }
}