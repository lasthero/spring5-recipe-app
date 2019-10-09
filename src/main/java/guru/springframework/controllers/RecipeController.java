package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class RecipeController {

  private RecipeService recipeService;

  @Autowired
  public RecipeController(RecipeService recipeService) {
    this.recipeService = recipeService;
  }

  @GetMapping("/recipe/{id}/show")
  public String getRecipePage(@PathVariable String id, Model model){

    model.addAttribute("recipe", recipeService.findById(new Long(id)));

    return "recipe/show";
  }

  @GetMapping("recipe/new")
  public String newRecipe(Model model) {
    model.addAttribute("recipe", new RecipeCommand());
    return "recipe/recipeform";
  }

  @GetMapping("recipe/{id}/update")
  public String updateRecipe(@PathVariable String id, Model model) {
    model.addAttribute("recipe", recipeService.findCommandById(new Long(id)));
    return "recipe/recipeform";
  }

  @GetMapping("recipe/{id}/delete")
  public String deleteRecipe(@PathVariable String id) {

    recipeService.deleteById(new Long(id));

    return "redirect:/index";
  }


  @PostMapping("recipe")
  public String saveOrUpdate(@ModelAttribute RecipeCommand command) {
    RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);

    return "redirect:/recipe/" + savedCommand.getId() + "/show";
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public ModelAndView handleNotFound(Exception exception) {
    log.error("Handling not found exception");
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("404error");
    modelAndView.addObject("exception", exception);
    return modelAndView;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(NumberFormatException.class)
  public ModelAndView handleBadRequest(Exception exception) {
    log.error("Handling number format exception");
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("400badRequest");
    modelAndView.addObject("exception", exception);
    return modelAndView;
  }

}
