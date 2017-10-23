package mcce.sapp.models;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

/**
 * A DAO for the entity {{capitalizedId}} is simply created by extending the CrudRepository
 * interface provided by spring. The following methods are some of the ones
 * available from such interface: save, delete, deleteAll, findOne and findAll.
 * The magic is that such methods must not be implemented, and moreover it is
 * possible create new query methods working only by defining their signature!
 * 
 * @author Pentcloud
 */
@Transactional
public interface {{capitalizedId}}Dao extends CrudRepository<{{capitalizedId}}, Long> {


} // class {{capitalizedId}}Dao