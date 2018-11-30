package fi.hh.SWD4TN020.RecipeStorage.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface CategoryRepository extends CrudRepository <Category, Long> {
	
	List<Category>findByName(@Param("name") String name);
	
	// Fetch recipes by category
		//List<Category> findByCategory (@Param("name") String name);

}
