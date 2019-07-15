package guru.springframework.bootstrap;

import guru.springframework.domain.Category;
import guru.springframework.domain.Difficulty;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Notes;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

  private RecipeRepository recipeRepository;
  private CategoryRepository categoryRepository;
  private UnitOfMeasureRepository unitOfMeasureRepository;
  private final String PERFECT_GUACAMOLE_DIRECTIONS = "1 Cut avocado, remove flesh: Cut the avocados in half. Remove seed. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.\n"
      + "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\n"
      + "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n"
      + "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n"
      + "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n"
      + "4 Cover with plastic and chill to store: Place plastic wrap on the surface of the guacamole cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.\n"
      + "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving";
  private final String PERFECT_GUACAMOLE_NOTES = "Variations\n"
      + "For a very quick guacamole just take a 1/4 cup of salsa and mix it in with your mashed avocados.\n"
      + "\n"
      + "Feel free to experiment! One classic Mexican guacamole has pomegranate seeds and chunks of peaches in it (a Diana Kennedy favorite). Try guacamole with added pineapple, mango, or strawberries (see our Strawberry Guacamole).";

  @Autowired
  public DataLoader(RecipeRepository recipeRepository,
      CategoryRepository categoryRepository,
      UnitOfMeasureRepository unitOfMeasureRepository) {
    this.recipeRepository = recipeRepository;
    this.categoryRepository = categoryRepository;
    this.unitOfMeasureRepository = unitOfMeasureRepository;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent)
  {
    if (recipeRepository.count() == 0) {
      // System.out.println(System.getProperty("user.dir"));
      initData();
    }
  }

  private void initData(){

    Recipe perfectGuacamole = new Recipe();
    perfectGuacamole.setDescription("Perfect Guacamole");
    perfectGuacamole.setSource("Simply Recipes");
    perfectGuacamole.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
    perfectGuacamole.setDifficulty(Difficulty.EASY);
    perfectGuacamole.setCategories(new HashSet<>(Arrays.asList(categoryRepository.findByDescription("Mexican").get())));
    perfectGuacamole.setPrepTime(10);
    perfectGuacamole.setCookTime(0);
    perfectGuacamole.setServings(4);
    perfectGuacamole.setIngredients(GetPerfectGuacamoleIngredients(perfectGuacamole));
    perfectGuacamole.setImage(GetPerfectGuacamoleImage());
    perfectGuacamole.setDirections(PERFECT_GUACAMOLE_DIRECTIONS);
    Notes perfectGuacamoleNotes = new Notes();
    perfectGuacamoleNotes.setRecipeNotes(PERFECT_GUACAMOLE_NOTES);
    perfectGuacamoleNotes.setRecipe(perfectGuacamole);
    perfectGuacamole.setNotes(perfectGuacamoleNotes);


    recipeRepository.save(perfectGuacamole);

  }

  private Set<Ingredient> GetPerfectGuacamoleIngredients(Recipe perfectGuacamoleRecipe) {
    HashSet<Ingredient> ingredients = new HashSet<>();
    Ingredient ripeAvocados = new Ingredient();
    ripeAvocados.setDescription("Ripe Avocados");
    ripeAvocados.setAmount(new BigDecimal(2));
    ripeAvocados.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Count").get());
    ripeAvocados.setRecipe(perfectGuacamoleRecipe);
    ingredients.add(ripeAvocados);

    Ingredient kosherSalt = new Ingredient();
    kosherSalt.setDescription("Kosher Salt");
    kosherSalt.setAmount(new BigDecimal(0.5));
    kosherSalt.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Teaspoon").get());
    kosherSalt.setRecipe(perfectGuacamoleRecipe);
    ingredients.add(kosherSalt);

    Ingredient limeJuice = new Ingredient();
    limeJuice.setDescription("Lime Juice");
    limeJuice.setAmount(new BigDecimal(1));
    limeJuice.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Tablespoon").get());
    limeJuice.setRecipe(perfectGuacamoleRecipe);
    ingredients.add(limeJuice);

    Ingredient mincedOnion = new Ingredient();
    mincedOnion.setDescription("Minced onions");
    mincedOnion.setAmount(new BigDecimal(2));
    mincedOnion.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Tablespoon").get());
    mincedOnion.setRecipe(perfectGuacamoleRecipe);
    ingredients.add(mincedOnion);

    Ingredient chiles = new Ingredient();
    chiles.setDescription("Chiles");
    chiles.setAmount(new BigDecimal(2));
    chiles.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Count").get());
    chiles.setRecipe(perfectGuacamoleRecipe);
    ingredients.add(chiles);

    Ingredient cilantro = new Ingredient();
    cilantro.setDescription("Cilantro");
    cilantro.setAmount(new BigDecimal(2));
    cilantro.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Tablespoon").get());
    cilantro.setRecipe(perfectGuacamoleRecipe);
    ingredients.add(cilantro);

    Ingredient blackPepper = new Ingredient();
    blackPepper.setDescription("Black pepper");
    blackPepper.setAmount(new BigDecimal(2));
    blackPepper.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Dash").get());
    blackPepper.setRecipe(perfectGuacamoleRecipe);
    ingredients.add(cilantro);

    Ingredient tomato = new Ingredient();
    tomato.setDescription("Ripe tomato");
    tomato.setAmount(new BigDecimal(0.5));
    tomato.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Count").get());
    tomato.setRecipe(perfectGuacamoleRecipe);
    ingredients.add(tomato);

    return ingredients;
  }

  private Byte[] GetPerfectGuacamoleImage() {
    try {
      BufferedImage inputImage = ImageIO.read(new File(System.getProperty("user.dir") + "/src/main/resources/static/images/guacamole.jpg"));
      ByteArrayOutputStream content = new ByteArrayOutputStream();
      ImageIO.write(inputImage, "jpg", content);

      return ArrayUtils.toObject(content.toByteArray());
    }
    catch (IOException ex){
      System.out.println("Error while loading images: "+ ex.toString());
    }
    return null;
  }
}
