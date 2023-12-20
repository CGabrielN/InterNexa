package socialnetwork.service;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.RepositoryException;
import socialnetwork.repository.db.paging.UserDBPagingRepository;

import java.util.Optional;

public class UserService {

    private final UserDBPagingRepository userDBPagingRepository;
    private final Validator<User> validator;

    public UserService(UserDBPagingRepository userDBPagingRepository, Validator<User> validator) {
        this.userDBPagingRepository = userDBPagingRepository;
        this.validator = validator;
    }

    /**
     * Coordinates the addition of a user into the app
     *
     * @param firstName first name of the user
     * @param lastName  last name of the user
     * @return Optional<User> empty if the user was saved, the user otherwise
     */
    public Optional<User> addUser(String firstName, String lastName, String username, String email, String password) {
        User user = new User(firstName, lastName, username, email, password);

        validator.validate(user);
        return this.userDBPagingRepository.save(user);
    }

    /**
     * Coordinates the operation of deleting a user from the app
     *
     * @param id id of the user to be deleted
     * @return Optional of user, empty if the user was not deleted, the deleted user otherwise
     */
    public Optional<User> deleteUser(Long id) {
        return this.userDBPagingRepository.delete(id);
    }

    /**
     * Updates the attributes of the user with the given id to the new ones
     *
     * @param id           id of the user to be modified
     * @param newFirstName the new first name of the user
     * @param newLastName  the new last name of the user
     * @return Optional of user - empty if the user was updated, the user otherwise
     */
    public Optional<User> updateUser(Long id, String newFirstName, String newLastName, String newUsername, String newEmail, String newPassword)  {
        User user = new User(id, newFirstName, newLastName, newUsername, newEmail, newPassword);
        validator.validate(user);

        return this.userDBPagingRepository.update(user);
    }

    /**
     * Search for a user with a given id
     * @param id id of the user
     * @return Optional of user - empty if there's no user with the given id, the user otherwise
     */
    public Optional<User> findUser(Long id) {
        return this.userDBPagingRepository.searchById(id);
    }

    /**
     * @return all users registered in the app
     */
    public Iterable<User> getAll() {
        return this.userDBPagingRepository.getAll();
    }

    /**
     * Try to log in with a given email and password
     * @param email user's email
     * @param password user's password
     * @return Optional of user - empty if there's no account with the given credentials, the user otherwise
     */
    public Optional<User> tryLogin(String email, String password){
        return this.userDBPagingRepository.tryLogin(email, password);
    }
}
