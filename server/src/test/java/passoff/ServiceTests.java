package java.passoff;

import org.junit.jupiter.api.*;
import service.AuthService;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTests {

    @Test
    public void AuthClearTest() {
        AuthService authService = new AuthService();
    }



}
