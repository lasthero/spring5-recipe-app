package guru.springframework.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CategoryTest {

  Category category;

  @Before
  public void setUp(){
    category = new Category();
  }

  @Test
  public void getId() {

    Long idVal = 4L;
    category.setId(idVal);
    assertEquals(idVal, category.getId());
  }

  @Test
  public void getDescription() {
  }

  @Test
  public void getRecipes() {
  }
}