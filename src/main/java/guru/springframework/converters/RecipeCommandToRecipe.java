package guru.springframework.converters;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Category;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import java.util.HashSet;
import java.util.Set;
import lombok.Synchronized;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {

  private IngredientCommandToIngredient ingredientCommandToIngredient;
  private NotesCommandToNotes notesCommandToNotes;
  private CategoryCommandToCategory categoryCommandToCategory;

  @Autowired
  public RecipeCommandToRecipe(IngredientCommandToIngredient ingredientCommandToIngredient,
       NotesCommandToNotes notesCommandToNotes,
       CategoryCommandToCategory categoryCommandToCategory) {
    this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    this.notesCommandToNotes = notesCommandToNotes;
    this.categoryCommandToCategory = categoryCommandToCategory;
  }

  @Synchronized
  @Nullable
  @Override
  public Recipe convert(RecipeCommand source) {
    if (source == null) {
      return null;
    }
    final Recipe recipe = new Recipe();
    recipe.setId(source.getId());
    recipe.setDirections(source.getDirections());
    recipe.setServings(source.getServings());
    recipe.setCookTime(source.getCookTime());
    recipe.setPrepTime(source.getPrepTime());
    recipe.setUrl(source.getUrl());
    recipe.setSource(source.getSource());
    recipe.setDescription(source.getDescription());
    recipe.setDifficulty(source.getDifficulty());
    recipe.setNotes(notesCommandToNotes.convert(source.getNotes()));
    recipe.setImage(source.getImage());

    Set<Category> categories = new HashSet<>();
    source.getCategories().forEach( command -> categories.add(categoryCommandToCategory.convert(command)));
    recipe.setCategories(categories);

    Set<Ingredient> ingredients = new HashSet<>();
    source.getIngredients().forEach(command -> ingredients.add(ingredientCommandToIngredient.convert(command)));
    recipe.setIngredients(ingredients);
    return  recipe;
  }
}
