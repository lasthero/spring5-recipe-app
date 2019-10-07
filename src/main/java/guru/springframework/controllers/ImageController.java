package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.RecipeService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ImageController {

  private final RecipeService recipeService;

  public ImageController(RecipeService recipeService) {
    this.recipeService = recipeService;
  }

  @GetMapping("recipe/{id}/recipeimage")
  public void renderImageFromDB(@PathVariable String id, HttpServletResponse response) throws IOException {
    RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(id));

    byte[] byteArray = new byte[recipeCommand.getImage().length];
    int i=0;
    for (Byte wrappedByte: recipeCommand.getImage()) {
      byteArray[i++] = wrappedByte; //auto boxing
    }

    response.setContentType("image/jpeg");
    InputStream stream = new ByteArrayInputStream(byteArray);
    IOUtils.copy(stream, response.getOutputStream());
  }

}
