package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IngredientController {

  private RecipeService recipeService;
  private IngredientService ingredientService;
  private UnitOfMeasureService unitOfMeasureService;

  @Autowired
  IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
    this.recipeService = recipeService;
    this.ingredientService = ingredientService;
    this.unitOfMeasureService = unitOfMeasureService;
  }

  @RequestMapping("recipe/{id}/ingredients")
  public String listIngredients(@PathVariable String id, Model model) {

    RecipeCommand recipeCommand = recipeService.findCommandById(new Long(id));
    model.addAttribute("recipe", recipeCommand);
    return "recipe/ingredient/list";
  }

  @GetMapping
  @RequestMapping("recipe/{recipeId}/ingredient/{ingredientId}/show")
  public String showIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model)
  {
    IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(new Long(recipeId), new Long(ingredientId));
    model.addAttribute("ingredient", ingredientCommand);
    return "recipe/ingredient/show";
  }

  @GetMapping
  @RequestMapping("recipe/{recipeId}/ingredient/{ingredientId}/update")
  public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
    model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId)));
    model.addAttribute("uomList", unitOfMeasureService.listAllUoms());

    return "recipe/ingredient/ingredientform";
  }

  @PostMapping
  @RequestMapping("recipe/{recipeId}/ingredient")
  public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
    IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

    return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
  }

}
