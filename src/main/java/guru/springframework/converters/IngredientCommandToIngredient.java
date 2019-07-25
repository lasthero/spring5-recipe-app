package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.domain.Ingredient;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {

  private UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure;

  @Autowired
  public IngredientCommandToIngredient(UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure) {
    this.unitOfMeasureCommandToUnitOfMeasure = unitOfMeasureCommandToUnitOfMeasure;
  }
  @Synchronized
  @Nullable
  @Override
  public Ingredient convert(IngredientCommand source) {
    if (source == null) {
      return null;
    }
    Ingredient ingredient = new Ingredient();
    ingredient.setId(source.getId());
    ingredient.setAmount(source.getAmount());
    ingredient.setDescription(source.getDescription());
    ingredient.setUnitOfMeasure(unitOfMeasureCommandToUnitOfMeasure.convert(source.getUnitOfMeasure()));
    return ingredient;
  }
}
