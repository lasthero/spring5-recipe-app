package guru.springframework.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UnitOfMeasureServiceImplTest {

  UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
  UnitOfMeasureService unitOfMeasureService;

  @Mock
  UnitOfMeasureRepository unitOfMeasureRepository;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    unitOfMeasureService = new UnitOfMeasureServiceImpl(unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureCommand);

  }

  @Test
  public void listAllUomsTest() {
    //given
    Set<UnitOfMeasure> uomSet = new HashSet<>();
    UnitOfMeasure uom1 = new UnitOfMeasure();
    uom1.setId(1L);
    uomSet.add(uom1);
    UnitOfMeasure uom2 = new UnitOfMeasure();
    uom2.setId(2L);
    uomSet.add(uom2);

    //when
    when(unitOfMeasureRepository.findAll()).thenReturn(uomSet);
    Set<UnitOfMeasureCommand> uomCommandSet = unitOfMeasureService.listAllUoms();

    //then
    assertEquals(uomSet.size(), uomCommandSet.size());
    verify(unitOfMeasureRepository, times(1)).findAll();
  }
}