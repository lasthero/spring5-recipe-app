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
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Profile("default")
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

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
  private final String CHICKEN_TACO_DIRECTIONS = "1 Prepare a gas or charcoal grill for medium-high, direct heat.\n"
      + "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. Add the chicken to the bowl and toss to coat all over.\n"
      + "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n"
      + "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n"
      + "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat. As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n"
      + "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n"
      + "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.\n";
  private final String CHICKEN_TACO_NOTES = "Look for ancho chile powder with the Mexican ingredients at your grocery store, or buy it online. (If you can't find ancho chili powder, you replace the ancho chili, the oregano, and the cumin with 2 1/2 tablespoons regular chili powder, though the flavor won't be quite the same.)";
  @Autowired
  public RecipeBootstrap(RecipeRepository recipeRepository,
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
    addPerfectGuacamoleIngredients(perfectGuacamole);
    perfectGuacamole.setImage(GetRecipeImage("guacamole.jpg"));
    perfectGuacamole.setDirections(PERFECT_GUACAMOLE_DIRECTIONS);
    Notes perfectGuacamoleNotes = new Notes();
    perfectGuacamoleNotes.setRecipeNotes(PERFECT_GUACAMOLE_NOTES);
    perfectGuacamole.setNotes(perfectGuacamoleNotes);

    recipeRepository.save(perfectGuacamole);
    recipeRepository.save(ChickenTacoRecipe());
  }

  private Recipe ChickenTacoRecipe(){
    Recipe chickenTacoRecipe = new Recipe();
    chickenTacoRecipe.setDescription("Spicy Grilled Chicken Tacos");
    chickenTacoRecipe.setSource("Simply Recipes");
    chickenTacoRecipe.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
    chickenTacoRecipe.setDifficulty(Difficulty.KIND_OF_HARD);
    chickenTacoRecipe.setCategories(new HashSet<>(Arrays.asList(categoryRepository.findByDescription("Mexican").get(), categoryRepository.findByDescription("Fast Food").get())));
    chickenTacoRecipe.setPrepTime(20);
    chickenTacoRecipe.setCookTime(15);
    chickenTacoRecipe.setServings(6);

    Ingredient chiliPower = new Ingredient();
    chiliPower.setDescription("Ancho Chili Powder");
    chiliPower.setAmount(new BigDecimal(2));
    chiliPower.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Teaspoon").get());
    chiliPower.setRecipe(chickenTacoRecipe);
    chickenTacoRecipe.addIngredient(chiliPower);

    Ingredient oregano = new Ingredient();
    oregano.setDescription("Dried Oregano");
    oregano.setAmount(new BigDecimal(1));
    oregano.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Teaspoon").get());
    oregano.setRecipe(chickenTacoRecipe);
    chickenTacoRecipe.addIngredient(oregano);

    Ingredient cumin = new Ingredient();
    cumin.setDescription("Dried Cumin");
    cumin.setAmount(new BigDecimal(1));
    cumin.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Teaspoon").get());
    cumin.setRecipe(chickenTacoRecipe);
    chickenTacoRecipe.addIngredient(cumin);

    Ingredient sugar = new Ingredient();
    sugar.setDescription("Sugar");
    sugar.setAmount(new BigDecimal(1));
    sugar.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Teaspoon").get());
    sugar.setRecipe(chickenTacoRecipe);
    chickenTacoRecipe.addIngredient(sugar);

    Ingredient salt = new Ingredient();
    salt.setDescription("Salt");
    salt.setAmount(new BigDecimal(0.5));
    salt.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Teaspoon").get());
    salt.setRecipe(chickenTacoRecipe);
    chickenTacoRecipe.addIngredient(salt);

    Ingredient garlic = new Ingredient();
    garlic.setDescription("Garlic");
    garlic.setAmount(new BigDecimal(1));
    garlic.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Count").get());
    garlic.setRecipe(chickenTacoRecipe);
    chickenTacoRecipe.addIngredient(garlic);

    Ingredient orangeZest = new Ingredient();
    orangeZest.setDescription("Orange Zest");
    orangeZest.setAmount(new BigDecimal(1));
    orangeZest.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Tablespoon").get());
    orangeZest.setRecipe(chickenTacoRecipe);
    chickenTacoRecipe.addIngredient(orangeZest);

    Ingredient orangeJuice= new Ingredient();
    orangeJuice.setDescription("Orange Juice");
    orangeJuice.setAmount(new BigDecimal(3));
    orangeJuice.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Tablespoon").get());
    orangeJuice.setRecipe(chickenTacoRecipe);
    chickenTacoRecipe.addIngredient(orangeJuice);

    Ingredient oliveOil= new Ingredient();
    oliveOil.setDescription("Olive Oil");
    oliveOil.setAmount(new BigDecimal(2));
    oliveOil.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Tablespoon").get());
    oliveOil.setRecipe(chickenTacoRecipe);
    chickenTacoRecipe.addIngredient(oliveOil);

    Ingredient chickenThighs= new Ingredient();
    chickenThighs.setDescription("Boneless Chicken Thighs");
    chickenThighs.setAmount(new BigDecimal(6));
    chickenThighs.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Count").get());
    chickenThighs.setRecipe(chickenTacoRecipe);
    chickenTacoRecipe.addIngredient(chickenThighs);

    chickenTacoRecipe.setImage(GetRecipeImage("grilled_chicken_tacos.jpg"));
    chickenTacoRecipe.setDirections(CHICKEN_TACO_DIRECTIONS);
    Notes notes = new Notes();
    notes.setRecipeNotes(CHICKEN_TACO_NOTES);
    chickenTacoRecipe.setNotes(notes);

    return chickenTacoRecipe;
  }
  private void addPerfectGuacamoleIngredients(Recipe perfectGuacamoleRecipe) {

    Ingredient ripeAvocados = new Ingredient();
    ripeAvocados.setDescription("Ripe Avocados");
    ripeAvocados.setAmount(new BigDecimal(2));
    ripeAvocados.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Count").get());
    ripeAvocados.setRecipe(perfectGuacamoleRecipe);
    perfectGuacamoleRecipe.addIngredient(ripeAvocados);

    Ingredient kosherSalt = new Ingredient();
    kosherSalt.setDescription("Kosher Salt");
    kosherSalt.setAmount(new BigDecimal(0.5));
    kosherSalt.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Teaspoon").get());
    kosherSalt.setRecipe(perfectGuacamoleRecipe);
    perfectGuacamoleRecipe.addIngredient(kosherSalt);

    Ingredient limeJuice = new Ingredient();
    limeJuice.setDescription("Lime Juice");
    limeJuice.setAmount(new BigDecimal(1));
    limeJuice.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Tablespoon").get());
    limeJuice.setRecipe(perfectGuacamoleRecipe);
    perfectGuacamoleRecipe.addIngredient(limeJuice);

    Ingredient mincedOnion = new Ingredient();
    mincedOnion.setDescription("Minced onions");
    mincedOnion.setAmount(new BigDecimal(2));
    mincedOnion.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Tablespoon").get());
    mincedOnion.setRecipe(perfectGuacamoleRecipe);
    perfectGuacamoleRecipe.addIngredient(mincedOnion);

    Ingredient chiles = new Ingredient();
    chiles.setDescription("Chiles");
    chiles.setAmount(new BigDecimal(2));
    chiles.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Count").get());
    chiles.setRecipe(perfectGuacamoleRecipe);
    perfectGuacamoleRecipe.addIngredient(chiles);

    Ingredient cilantro = new Ingredient();
    cilantro.setDescription("Cilantro");
    cilantro.setAmount(new BigDecimal(2));
    cilantro.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Tablespoon").get());
    cilantro.setRecipe(perfectGuacamoleRecipe);
    perfectGuacamoleRecipe.addIngredient(cilantro);

    Ingredient blackPepper = new Ingredient();
    blackPepper.setDescription("Black pepper");
    blackPepper.setAmount(new BigDecimal(2));
    blackPepper.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Dash").get());
    blackPepper.setRecipe(perfectGuacamoleRecipe);
    perfectGuacamoleRecipe.addIngredient(cilantro);

    Ingredient tomato = new Ingredient();
    tomato.setDescription("Ripe tomato");
    tomato.setAmount(new BigDecimal(0.5));
    tomato.setUnitOfMeasure(unitOfMeasureRepository.findByDescription("Count").get());
    tomato.setRecipe(perfectGuacamoleRecipe);
    perfectGuacamoleRecipe.addIngredient(tomato);

  }

  private Byte[] GetRecipeImage(String fileName) {
    try {
      BufferedImage inputImage = ImageIO.read(new File(System.getProperty("user.dir") + "/src/main/resources/static/images/"+ fileName));
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
