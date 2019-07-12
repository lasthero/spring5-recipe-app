package guru.springframework.repositories;

import guru.springframework.domain.UnitOfMeasure;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, Long> {

  Optional<UnitOfMeasure> findByDescription(String description);

}
