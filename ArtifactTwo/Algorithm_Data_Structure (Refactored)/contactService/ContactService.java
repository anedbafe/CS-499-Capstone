package contactService;

import java.util.HashMap;
import java.util.Collection;
import java.util.Map;

public class ContactService {

    // CONTACT MEMORY STORAGE USING A HASHMAP
    private final Map<String, Contact> contacts;

    // CONSTRUCTOR TO INITIALIZE EMPTY CONTACT LIST
    public ContactService() {
        this.contacts = new HashMap<>();
    }

    // ADD A NEW CONTACT TO THE MAP, IF CONTACT ID DOES NOT ALREADY EXIST
    public void addContact(Contact contact) {
        if (contacts.containsKey(contact.getContactId())) {
            throw new IllegalArgumentException("\nCannot create contact ID " + contact.getContactId() + " already on file!\n");
        }
        contacts.put(contact.getContactId(), contact);
        System.out.println("\nContact ID: " + contact.getContactId() + " created!\n");
    }

    // READ CONTACT INFORMATION BY ID
    public Contact readContact(String contactId) {
        if (contacts.containsKey(contactId)) {
            return contacts.get(contactId);
        } else {
            throw new IllegalArgumentException("\nContact ID " + contactId + " not found!\n");
        }
    }

    // UPDATE CONTACT INFORMATION FOR A GIVEN CONTACT ID
    public Contact updateContact(String contactId, String firstName, String lastName, String phone, String address) {
        // CHECK IF CONTACT ID EXISTS
        if (!contacts.containsKey(contactId)) {
            throw new IllegalArgumentException("\nContact ID " + contactId + " not found!\n");
        }

        // RETRIEVE CONTACT OBJECT FROM MAP
        Contact contact = contacts.get(contactId);
        boolean meetCondition = false;

        // UPDATE FIRST NAME IF VALID
        if (firstName != null && !firstName.trim().isEmpty() && firstName.length() <= 10) {
            contact.setFirstName(firstName);
            meetCondition = true;
        }

        // UPDATE LAST NAME IF VALID
        if (lastName != null && !lastName.trim().isEmpty() && lastName.length() <= 10) {
            contact.setLastName(lastName);
            meetCondition = true;
        }

        // UPDATE PHONE NUMBER IF VALID
        if (phone != null && !phone.trim().isEmpty() && phone.matches("\\d{10}")) {
            contact.setPhone(phone);
            meetCondition = true;
        }

        // UPDATE ADDRESS IF VALID
        if (address != null && !address.trim().isEmpty() && address.length() <= 30) {
            contact.setAddress(address);
            meetCondition = true;
        }

        // THROW ERROR IF NO VALID FIELD WAS UPDATED
        if (!meetCondition) {
            throw new IllegalArgumentException("\nSome input information are not valid!\n");
        }

        return contact;
    }

    // DELETE CONTACT FROM MAP BY ID
    public void deleteContact(String contactId) {
        if (!contacts.containsKey(contactId)) {
            throw new IllegalArgumentException("\nContact ID not found!");
        }
        contacts.remove(contactId);
    }

    // GET A SINGLE CONTACT BY ID (RETURNS NULL IF NOT FOUND)
    public Contact getContact(String contactId) {
        return contacts.get(contactId);
    }

    // RETURN COLLECTION OF ALL CONTACTS
    public Collection<Contact> getAllContacts() {
        return contacts.values();
    }
}
