package guru.springframework.services;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import java.io.IOException;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class ImageServiceImplTest {

  @Mock
  RecipeRepository recipeRepository;

  ImageService imageService;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    imageService = new ImageServiceImpl(recipeRepository);
  }
  @Test
  public void saveImageFile() throws IOException {
    //given
    Long id = 1L;
    MultipartFile multipartFile = new MockMultipartFile("imageFile", "testing.txt", "text/plain",
        "Spring Framework Guru".getBytes());
    Recipe recipe = new Recipe();
    recipe.setId(id);
    Optional<Recipe> recipeOptional = Optional.of(recipe);

    when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
    ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

    //when
    imageService.saveImageFile(id, multipartFile);

    //then
    verify(recipeRepository, times(1)).save(argumentCaptor.capture());
    Recipe savedRecipe = argumentCaptor.getValue();
    assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
  }
}