package contactServiceTest;

import static org.junit.Assert.*;
import org.junit.Test;
import contactService.Contact;

// JUNIT TEST CLASS FOR THE CONTACT CLASS
public class ContactTest {

    // TEST THAT CREATES A CONTACT WITH VALID INPUT DATA
    @Test
    public void testContactValildInfo() {
        Contact contact = new Contact("John", "Doe", "0123456789", "SNHU CS-320");
        assertEquals("John", contact.getFirstName());
        assertEquals("Doe", contact.getLastName());
        assertEquals("0123456789", contact.getPhone());
        assertEquals("SNHU CS-320", contact.getAddress());

        System.out.println("Testing creating a contact with valid information: PASSED!");
    }

    // TEST THAT THROWS EXCEPTION WHEN FIRST NAME IS NULL
    @Test(expected = IllegalArgumentException.class)
    public void testContactNullName() {
        new Contact(null, "Doe", "0123456789", "SNHU CS-320");
    }

    // TEST THAT THROWS EXCEPTION WHEN FIRST NAME EXCEEDS CHARACTER LIMIT
    @Test
    public void testContactLongName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Contact("JonathanDavidSmith", "Doe", "0123456789", "SNHU CS-320");
        });
        assertTrue(exception.getMessage().contains("First Name"));
    }

    // TEST THAT THROWS EXCEPTION WHEN PHONE NUMBER IS TOO SHORT
    @Test(expected = IllegalArgumentException.class)
    public void testContactShortNumber() {
        new Contact("John", "Doe", "01234567", "SNHU CS-320");
    }

    // TEST THAT THROWS EXCEPTION WHEN PHONE NUMBER IS TOO LONG
    @Test(expected = IllegalArgumentException.class)
    public void testContactLongNumber() {
        new Contact("John", "Doe", "01234567891", "SNHU CS-320");
    }

    // TEST THAT THROWS EXCEPTION WHEN ADDRESS IS NULL
    @Test(expected = IllegalArgumentException.class)
    public void testContactNullAddress() {
        new Contact("John", "Doe", "0123456789", null);
    }

    // TEST THAT THROWS EXCEPTION WHEN ADDRESS EXCEEDS MAXIMUM LENGTH
    @Test(expected = IllegalArgumentException.class)
    public void testContactLongAddress() {
        new Contact("John", "Doe", "0123456789", "This address is way too long to be valid for input");
    }
}
