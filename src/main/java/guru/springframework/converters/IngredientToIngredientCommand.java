package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.domain.Ingredient;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientToIngredientCommand implements Converter<Ingredient, IngredientCommand> {

  private UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

  @Autowired
  public void IngredientToIngredientCommand(UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
    this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
  }
  @Synchronized
  @Nullable
  @Override
  public IngredientCommand convert(Ingredient source) {
    if (source == null) {
      return null;
    }
    final IngredientCommand command = new IngredientCommand();
    command.setId(source.getId());
    command.setAmount(source.getAmount());
    command.setDescription(source.getDescription());
    command.setUnitOfMeasure(unitOfMeasureToUnitOfMeasureCommand.convert(source.getUnitOfMeasure()));
    return command;
  }
}
