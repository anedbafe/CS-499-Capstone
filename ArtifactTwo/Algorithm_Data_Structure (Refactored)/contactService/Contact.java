package contactService;

// LIBRARY IMPORTS
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Contact {
    // SET TO TRACK GENERATED CONTACT IDS TO ENSURE UNIQUENESS
    public static final Set<String> custIdCreated = new HashSet<>();

    // CONTACT ATTRIBUTES
    private final String contactId;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    // CONSTRUCTOR TO INITIALIZE A NEW CONTACT OBJECT WITH VALIDATION
    public Contact(String firstName, String lastName, String phone, String address) {
        this.contactId = custIdGen();
        validString(firstName, 10, "First Name");
        validString(lastName, 10, "Last Name");
        validPhone(phone);
        validString(address, 30, "Address");
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }

    // GENERATES A UNIQUE CONTACT ID WITH PREFIX "ID" AND 8 RANDOM DIGITS
    public static String custIdGen() {
        while (true) {
            String custId = "ID" + (10000000 + new Random().nextInt(90000000));
            if (!custIdCreated.contains(custId)) {
                custIdCreated.add(custId);
                return custId;
            }
        }
    }

    // GETTER FOR CONTACT ID
    public String getContactId() {
        return contactId;
    }

    // GETTER FOR FIRST NAME
    public String getFirstName() {
        return firstName;
    }

    // GETTER FOR LAST NAME
    public String getLastName() {
        return lastName;
    }

    // GETTER FOR PHONE NUMBER
    public String getPhone() {
        return phone;
    }

    // GETTER FOR ADDRESS
    public String getAddress() {
        return address;
    }

    // SETTER FOR FIRST NAME WITH VALIDATION
    public void setFirstName(String firstName) {
        validString(firstName, 10, "First Name");
        this.firstName = firstName;
    }

    // SETTER FOR LAST NAME WITH VALIDATION
    public void setLastName(String lastName) {
        validString(lastName, 10, "Last Name");
        this.lastName = lastName;
    }

    // SETTER FOR PHONE NUMBER WITH VALIDATION
    public void setPhone(String phone) {
        validPhone(phone);
        this.phone = phone;
    }

    // SETTER FOR ADDRESS WITH VALIDATION
    public void setAddress(String address) {
        validString(address, 30, "Address");
        this.address = address;
    }

    // VALIDATES STRINGS FOR NULL, EMPTY, AND MAXIMUM LENGTH CONSTRAINTS
    private void validString(String value, int maxChars, String objectName) {
        if (value == null || value.trim().isEmpty() || value.length() > maxChars) {
            throw new IllegalArgumentException(objectName + " is invalid! \n"
                    + "Cannot be null, empty, or longer than " + maxChars + " characters.");
        }
    }

    // VALIDATES PHONE NUMBER FORMAT: NOT NULL, NOT EMPTY, AND EXACTLY 10 DIGITS
    private void validPhone(String phone) {
        if (phone == null || phone.trim().isEmpty() || !phone.matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone is invalid!\nCannot be null, empty, and must be exactly 10 digits.");
        }
    }
}
