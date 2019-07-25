package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Category;
import javax.validation.constraints.Null;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CategoryCommandToCategory implements Converter<CategoryCommand, Category> {

  /*
  private RecipeCommandToRecipe recipeCommandToRecipe;

  public CategoryCommandToCategory(RecipeCommandToRecipe recipeCommandToRecipe)
  {
    this.recipeCommandToRecipe = recipeCommandToRecipe;
  } */
  @Synchronized
  @Nullable
  @Override
  public Category convert(CategoryCommand source) {
    if (source == null) {
      return null;
    }
    Category category = new Category();
    category.setId(source.getId());
    category.setDescription(source.getDescription());
    return category;
  }
}
