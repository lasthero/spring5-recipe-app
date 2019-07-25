package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

  private final RecipeRepository recipeRepository;

  @Autowired
  public RecipeServiceImpl(RecipeRepository recipeRepository) {
    this.recipeRepository = recipeRepository;
  }
  @Override
  public Set<Recipe> getRecipes() {
    Set<Recipe> recipeSet = new HashSet<>();
    recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
    return  recipeSet;
  }

  @Override
  public Recipe findById(Long id) {
    Optional<Recipe> recipeOptional = recipeRepository.findById(id);

    if (!recipeOptional.isPresent()) {
      throw new RuntimeException("Recipe not found");
    }
    return recipeOptional.get();
  }
}
