package pajr.dao;

import pajr.model.Disaster;
import org.springframework.data.repository.CrudRepository;

public interface DisasterRepository extends CrudRepository<Disaster, Integer> {

}

