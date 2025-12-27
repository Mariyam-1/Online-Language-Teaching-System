package test;

import main.Users;
import main.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsersTest {
    private Users testUser;
    private final String TEST_NAME = "Test User";
    private final String TEST_EMAIL = "test@email.com";
    private final String TEST_PASSWORD = "testpass123";
    private final Role TEST_ROLE = Role.student;

    @BeforeEach
    void setUp() {
        testUser = new Users(TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_ROLE);
    }

    @Test
    void testUserCreation() {
        assertNotNull(testUser);
        assertEquals(TEST_NAME, testUser.getName());
        assertEquals(TEST_EMAIL, testUser.getEmail());
        assertEquals(TEST_ROLE, testUser.getRole());
        assertTrue(testUser.isAccountActive());
    }

    @Test
    void testLogin() {
        assertTrue(testUser.login(TEST_EMAIL, TEST_PASSWORD));
        assertFalse(testUser.login("wrong@email.com", TEST_PASSWORD));
        assertFalse(testUser.login(TEST_EMAIL, "wrongpassword"));
    }

    @Test
    void testAccountStatusManagement() {
        assertTrue(testUser.isAccountActive());
        
        testUser.deactivateAccount();
        assertFalse(testUser.isAccountActive());
        
        testUser.activateAccount();
        assertTrue(testUser.isAccountActive());
    }

    @Test
    void testToString() {
        String expectedString = TEST_NAME + " (" + TEST_ROLE + ", " +(testUser.isAccountActive() ? "Active" : "Inactive") + ")";
        assertEquals(expectedString, testUser.toString());
    }

   
    @Test
    void testGetRoleMessage() {
        String expectedMessage = "Welcome " + TEST_NAME+ "! You are logged in as " + TEST_ROLE;
        assertEquals(expectedMessage, testUser.getRoleMessage());
    }
} 