package contactServiceTest;

import static org.junit.Assert.*;
import org.junit.Test;
import contactService.Contact;
import contactService.ContactService;

// JUNIT TEST FOR CONTACT CLASS
public class ContactServiceTest {

    // METHOD THAT CREATE AND OBJECT AND WILL CHECK IF OBJECT IS CREATE WITH RIGHT PARAMS
    @Test
    public void createContact() {
        ContactService service = new ContactService();
        Contact contact = new Contact("John", "Doe", "0123456789", "Southern NH University");
        
        service.addContact(contact);

        assertEquals("John", contact.getFirstName());
        assertEquals("Doe", contact.getLastName());
        assertEquals("0123456789", contact.getPhone());
        assertEquals("Southern NH University", contact.getAddress());

        System.out.println("ContactServiceTest: Creating contact test PASSED!");
    }

    // METHOD TO CREATE AN OBJECT AND CHECK OBJECT BY ID AFTER
    @Test
    public void addContact() {
        ContactService service = new ContactService();
        Contact contact = new Contact("John", "Doe", "0123456789", "Southern NH University");
        
        service.addContact(contact);
        String id = contact.getContactId();

        assertNotNull(service.getContact(id));
        System.out.println("ContactServiceTest: Adding contact test PASSED!");
    }

    // METHOD TO CREATE TWO OBJECTS AND CHECK OBJECT BY ID AFTER
    @Test
    public void addMultiplesContact() {
        ContactService service = new ContactService();
        Contact contact1 = new Contact("John", "Doe", "0123456789", "Southern NH University");
        Contact contact2 = new Contact("Jane", "Smith", "9876543210", "SNHUniversity");

        service.addContact(contact1);
        service.addContact(contact2);

        String id1 = contact1.getContactId();
        String id2 = contact2.getContactId();

        // Verify both contacts were added
        assertNotNull(service.getContact(id1));
        assertNotNull(service.getContact(id2));
        System.out.println("ContactServiceTest: Adding multiple contact test PASSED!");
    }

    // METHOD THAT WILL MAKE A DUPLICATE CONTACT AND EXCEPTION WILL BE THROWN NO DUPLICATE ID ACCEPTED/RANDOM ID OF 8 CHARS IMPLEMENTED TO MINIMIZE THIS ISSUE
    @Test
    public void addDuplicateContact() {
        ContactService service = new ContactService();
        Contact contact = new Contact("John", "Doe", "0123456789", "Southern NH University");
        
        service.addContact(contact);

        // MESSAGE DISPLAY WHEN EXCEPTION IS THROWN
        try {
            // Adding contact second time
            service.addContact(contact);
            fail("Expected IllegalArgumentException thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("ContactServiceTest: Adding duplicate contact test PASSED!");
        }
    }

    // METHOD TO READ CONTACT USING CONTACT ID
    @Test
    public void readContact() {
        ContactService service = new ContactService();
        Contact contact = new Contact("John", "Doe", "0123456789", "Southern NH University");
        
        service.addContact(contact);
        String id = contact.getContactId();

        service.readContact(id);

        assertNotNull(service.getContact(id));
        assertEquals("John", service.getContact(id).getFirstName());
        System.out.println("ContactServiceTest: Reading a contact test PASSED!");
    }

    // METHOD TO UPDATE A RECORD USING CONTACT ID
    @Test
    public void UpdateContact() {
        ContactService service = new ContactService();
        Contact contact = new Contact("John", "Doe", "0123456789", "Southern NH University");
        
        service.addContact(contact);
        String id = contact.getContactId();

        service.updateContact(id, "Jane", "Smith", "9876543210", "SNH-University");

        // VERIFY UPDATED INFO
        Contact updatedContact = service.getContact(id);
        assertEquals("Jane", updatedContact.getFirstName());
        assertEquals("Smith", updatedContact.getLastName());
        assertEquals("9876543210", updatedContact.getPhone());
        assertEquals("SNH-University", updatedContact.getAddress());

        System.out.println("ContactServiceTest: update test PASSED!");
    }

    // METHOD TO DELETE A RECORD
    @Test
    public void deleteContact() {
        ContactService service = new ContactService();
        Contact contact = new Contact("John", "Doe", "0123456789", "Southern NH University");
        service.addContact(contact);
        String id = contact.getContactId();

        // DELETE CONTACT PREVIOUSLY ADDED
        service.deleteContact(id);
        // VERIFY CONTACT IS REMOVED
        assertNull(service.getContact(id));
    }

    // METHOD WILL DELETE A CONTACT THAT DOES NOT EXISTS
    @Test
    public void deleteContactInvalid() {
        ContactService service = new ContactService();
        Contact contact = new Contact("John", "Doe", "0123456789", "Southern NH University");
        service.addContact(contact);
        String fakeId = "99999";

        try {
            service.deleteContact(fakeId);
            fail("Expected IllegalArgumentException thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("Contact ID " + fakeId + " not found! Can't be deleted.");
            System.out.println("ContactServiceTest: non-contact test PASSED!");
        }
    }
} 