package com.example.backend.model.dao;

import com.example.backend.model.LocalUser;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

// The purpose of a DAO is to provide a layer of abstraction between the application code and the database,
// allowing the application to work with objects that represent data rather than directly interacting with the database.
// By using DAOs, developers can separate the data access code from the business logic of their applications,
// which makes the code more maintainable and easier to test.
// -----------------------------------------------------------------------------------------------------------------
// CrudRepository is an interface provided by Spring Data JPA that defines a set of generic CRUD (Create, Read, Update, Delete)
// operations for working with entities in a database.
// ListCrudRepository is an extension to CrudRepository returning List instead of Iterable where applicable.
// -----------------------------------------------------------------------------------------------------------------
// In this example, the UserRepository interface extends CrudRepository<User, Long>.
// This means that it inherits all the methods defined in CrudRepository, but is specific to the User
// entity type and the Long primary key type.
public interface LocalUserDAO extends ListCrudRepository<LocalUser, Long> {

    // To use this method, you would inject an instance of the LocalUserRepository interface into a Spring component,
    // and then call the findByEmailIgnoreCase method on the injected repository object, passing in the email address as
    // an argument. The method will return an Optional object that can be checked for the presence of a LocalUser object
    // with the specified email address.
    Optional<LocalUser> findByEmailIgnoreCase(String email);
    Optional<LocalUser> findByUsernameIgnoreCase(String username);


}
