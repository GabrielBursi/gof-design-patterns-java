import java.util.ArrayList;
import java.util.List;

public class UserRepositoryMemory implements UserRepository {

    private static volatile UserRepositoryMemory instance;
    private final List<User> users;

    private UserRepositoryMemory() {
        this.users = new ArrayList<User>();
    }

    static UserRepositoryMemory getInstance() {
        UserRepositoryMemory result = instance;
        if(result == null) {
            synchronized (UserRepositoryMemory.class) {
                result = instance;
                if (result == null) {
                    instance = result = new UserRepositoryMemory();
                }
            }
        }

        return result;
    }

    @Override
    public void save(User user) {
        this.users.add(user);
    }

    @Override
    public User getByEmail(String email) {
        return this.users.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
    }
}
