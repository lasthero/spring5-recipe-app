package guru.springframework.commands;

import guru.springframework.domain.Difficulty;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Setter
@Getter
@NoArgsConstructor
public class RecipeCommand {

  private Long id;

  @NotBlank
  @Size(min = 3, max = 255)
  private String description;

  @Min(1)
  @Max(999)
  private Integer prepTime;

  @Min(1)
  @Max(999)
  private Integer cookTime;

  @Min(1)
  @Max(100)
  private Integer servings;
  private Byte[] image;
  private String base64Image;
  private String source;

  @URL
  private String url;

  @NotBlank
  private String directions;
  private Set<IngredientCommand> ingredients = new HashSet<>();
  private Difficulty difficulty;
  private NotesCommand notes;
  private Set<CategoryCommand> categories = new HashSet<>();
}
