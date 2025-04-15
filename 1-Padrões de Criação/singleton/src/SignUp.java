public class SignUp {
    private final UserRepository userRepository;

    public SignUp() {
        this.userRepository = UserRepositoryMemory.getInstance();
    }

    void execute(SignUpDTO input) {
        User user = User.create(input.name(), input.email(), input.password());
        userRepository.save(user);
    }
}
