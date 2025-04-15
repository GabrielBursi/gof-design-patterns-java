import java.util.UUID;

public class User {

    private final String userId;
    private final String name;
    private final String email;
    private final String password;

    public User(String userId,
            String name,
            String email,
            String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    static User create (String name, String email, String password) {
		String userId = UUID.randomUUID().toString();
		return new User(userId, name, email, password);
	}

    boolean passwordMatches (String password) {
		return this.password.equals(password);
	}

}
