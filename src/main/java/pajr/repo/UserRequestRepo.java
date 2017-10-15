package pajr.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import pajr.model.Status;
import pajr.model.UserRequest;

public interface UserRequestRepo  extends CrudRepository<UserRequest, Integer> {
    
    List<UserRequest> findByStatus(Status status);
}
