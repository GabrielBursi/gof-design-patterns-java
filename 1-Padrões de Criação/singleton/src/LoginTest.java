import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LoginTest {

    @Test()
    public void loginTest() {
        SignUp signuUp = new SignUp();
        Login login = new Login();
        SignUpDTO signUpDTO = new SignUpDTO("gabriel", "java@email.com", "java123");
        signuUp.execute(signUpDTO);
        LoginDTO loginDTO = new LoginDTO("java@email.com", "java123");
        LoginOutputDTO output = login.execute(loginDTO);
        assertTrue(output.isSucess());
    }
}
