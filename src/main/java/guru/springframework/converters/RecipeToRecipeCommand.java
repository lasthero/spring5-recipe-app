package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import java.util.HashSet;
import java.util.Set;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {

  private IngredientToIngredientCommand ingredientToIngredientCommand;
  private NotesToNotesCommand notesToNotesCommand;
  private CategoryToCategoryCommand categoryToCategoryCommand;

  @Autowired
  public RecipeToRecipeCommand(IngredientToIngredientCommand ingredientToIngredientCommand,
      NotesToNotesCommand notesToNotesCommand,
      CategoryToCategoryCommand categoryToCategoryCommand) {
   this.ingredientToIngredientCommand = ingredientToIngredientCommand;
   this.notesToNotesCommand = notesToNotesCommand;
   this.categoryToCategoryCommand = categoryToCategoryCommand;
  }

  @Synchronized
  @Nullable
  @Override
  public RecipeCommand convert(Recipe source) {
    if (source == null) {
      return null;
    }
    final RecipeCommand command = new RecipeCommand();
    command.setId(source.getId());
    command.setDirections(source.getDirections());
    command.setServings(source.getServings());
    command.setCookTime(source.getCookTime());
    command.setPrepTime(source.getPrepTime());
    command.setUrl(source.getUrl());
    command.setSource(source.getSource());
    command.setDescription(source.getDescription());
    command.setDifficulty(source.getDifficulty());
    command.setNotes(notesToNotesCommand.convert(source.getNotes()));

    Set<CategoryCommand> categoryCommands = new HashSet<>();
    source.getCategories().forEach( category -> categoryCommands.add(categoryToCategoryCommand.convert(category)));
    command.setCategories(categoryCommands);

    Set<IngredientCommand> ingredientCommands = new HashSet<>();
    source.getIngredients().forEach(ingredient -> ingredientCommands.add(ingredientToIngredientCommand.convert(ingredient)));
    command.setIngredients(ingredientCommands);
    return command;
  }
}
