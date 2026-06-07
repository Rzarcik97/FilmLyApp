package filmly.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityName, Long id) {
        super(entityName + " not found with id: " + id);
    }

    public EntityNotFoundException(String entityName, String name) {
        super(entityName + " " + name + " not found.");
    }
}
