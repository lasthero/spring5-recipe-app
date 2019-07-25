package guru.springframework.converters;

import static org.junit.Assert.*;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

public class UnitOfMeasureCommandToUnitOfMeasureTest {


  public static final String DESCRIPTION = "description";
  public static final Long LONG_VALUE = new Long(1L);

  UnitOfMeasureCommandToUnitOfMeasure converter;

  @Before
  public void setUp() {
    converter = new UnitOfMeasureCommandToUnitOfMeasure();
  }

  @Test
  public void testNullParameter() {
    assertNull(converter.convert(null));
  }

  @Test
  public void testEmptyObject() {
    assertNotNull(converter.convert(new UnitOfMeasureCommand()));
  }

  @Test
  public void convert() {
    //given
    UnitOfMeasureCommand command = new UnitOfMeasureCommand();
    command.setId(LONG_VALUE);
    command.setDescription(DESCRIPTION);

    //when
    UnitOfMeasure uom = converter.convert(command);

    //then
    assertNotNull(uom);
    assertEquals(LONG_VALUE, uom.getId());
    assertEquals(DESCRIPTION, uom.getDescription());
  }
}