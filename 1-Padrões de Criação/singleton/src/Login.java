public class Login {
    private final UserRepository userRepository;

    public Login() {
        this.userRepository = UserRepositoryMemory.getInstance();
    }


    LoginOutputDTO execute(LoginDTO input) {
        User user = userRepository.getByEmail(input.email());
        return new LoginOutputDTO(user != null && user.passwordMatches(input.password()));
    }
}
