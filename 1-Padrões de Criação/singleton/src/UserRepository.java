public interface UserRepository {
    void save (User user);
	User getByEmail (String email);
}
