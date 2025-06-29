package main;

import contactService.Contact;
import contactService.ContactService;
import java.util.Collection;
import java.util.Scanner;

public class Main {

    // SCANNER FOR USER INPUT
    private static final Scanner scanner = new Scanner(System.in);

    // INSTANCE OF CONTACTSERVICE TO HANDLE CONTACT OPERATIONS
    private static final ContactService contactService = new ContactService();

    // METHOD TO DISPLAY THE MENU OPTIONS TO THE USER
    private static void showMenu() {
        System.out.println("***** CONTACT SERVICE MENU *****");
        System.out.println("A. ADD CONTACT");
        System.out.println("S. SEARCH CONTACT");
        System.out.println("E. EDIT CONTACT");
        System.out.println("L. LIST ALL CONTACT");
        System.out.println("D. DELETE CONTACT");
        System.out.println("C. CLEAR SCREEN");
        System.out.println("Q. QUIT");
        System.out.println("\nSelect a menu option: ");
    }

    // METHOD TO HANDLE USER INPUT TO CREATE AND ADD A NEW CONTACT
    private static void addContact() {
        try {
            System.out.print("ENTER FIRST NAME: ");
            String firstName = scanner.nextLine();
            System.out.print("ENTER LAST NAME: ");
            String lastName = scanner.nextLine();
            System.out.print("ENTER PHONE (10 DIGITS): ");
            String phone = scanner.nextLine();
            System.out.print("ENTER ADDRESS: ");
            String address = scanner.nextLine();

            // CREATE CONTACT OBJECT WITH USER INPUT
            Contact newContact = new Contact(firstName, lastName, phone, address);

            // ADD CONTACT TO SERVICE
            contactService.addContact(newContact);
        } catch (IllegalArgumentException e) {
            System.out.println("\nCreating contact ERROR!: " + e.getMessage() + "\n");
        }
    }

    // METHOD TO SEARCH FOR AND DISPLAY A CONTACT BY ID
    private static void readContact() {
        System.out.print("SEARCHING: \nENTER CONTACT ID: ");
        String id = scanner.nextLine();

        try {
            Contact contact = contactService.readContact(id);

            System.out.println("\n***** CONTACT DETAIL *****");
            System.out.printf("%-10s %-12s %-12s %-15s %-30s%n", 
                "ID", "FIRST NAME", "LAST NAME", "PHONE", "ADDRESS");
            System.out.println("--------------------------------------------------------------------------------");
            System.out.printf("%-10s %-12s %-12s %-15s %-30s%n",
                contact.getContactId(),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getPhone(),
                contact.getAddress());
            System.out.println("--------------------------------------------------------------------------------\n");
        } catch (IllegalArgumentException e) {
            System.out.println("\nReading Contact ERROR!: " + e.getMessage() + "\n");
        }
    }

    // METHOD TO UPDATE CONTACT INFORMATION AFTER CONFIRMING FIELDS
    private static void updateContact() {
        System.out.print("EDITING: \nENTER CONTACT ID: ");
        String id = scanner.nextLine();

        try {
            Contact contact = contactService.getContact(id);

            System.out.println("\n***** CURRENT CONTACT ID DETAILS *****");
            System.out.printf("%-10s %-12s %-12s %-15s %-30s%n", 
                "ID", "FIRST NAME", "LAST NAME", "PHONE", "ADDRESS");
            System.out.println("--------------------------------------------------------------------------------");
            System.out.printf("%-10s %-12s %-12s %-15s %-30s%n",
                contact.getContactId(),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getPhone(),
                contact.getAddress());
            System.out.println("--------------------------------------------------------------------------------\n");

            String firstName = null, lastName = null, phone = null, address = null;

            System.out.print("DO YOU WANT TO CHANGE FIRST NAME? (Y/N): ");
            if (Character.toUpperCase(scanner.nextLine().charAt(0)) == 'Y') {
                System.out.print("ENTER NEW FIRST NAME: ");
                firstName = scanner.nextLine();
            }

            System.out.print("DO YOU WANT TO CHANGE LAST NAME? (Y/N): ");
            if (Character.toUpperCase(scanner.nextLine().charAt(0)) == 'Y') {
                System.out.print("ENTER NEW LAST NAME: ");
                lastName = scanner.nextLine();
            }

            System.out.print("DO YOU WANT TO CHANGE PHONE? (Y/N): ");
            if (Character.toUpperCase(scanner.nextLine().charAt(0)) == 'Y') {
                System.out.print("ENTER NEW PHONE (10 DIGITS): ");
                phone = scanner.nextLine();
            }

            System.out.print("DO YOU WANT TO CHANGE ADDRESS? (Y/N): ");
            if (Character.toUpperCase(scanner.nextLine().charAt(0)) == 'Y') {
                System.out.print("ENTER NEW ADDRESS: ");
                address = scanner.nextLine();
            }

            Contact updated = contactService.updateContact(id, firstName, lastName, phone, address);

            System.out.println("\n***** NEW CONTACT INFORMATION *****");
            System.out.printf("%-10s %-12s %-12s %-15s %-30s%n", 
                "ID", "FIRST NAME", "LAST NAME", "PHONE", "ADDRESS");
            System.out.println("--------------------------------------------------------------------------------");
            System.out.printf("%-10s %-12s %-12s %-15s %-30s%n",
                updated.getContactId(),
                updated.getFirstName(),
                updated.getLastName(),
                updated.getPhone(),
                updated.getAddress());
            System.out.println("--------------------------------------------------------------------------------\n");

            System.out.println("Contact updated successfully!\n");

        } catch (IllegalArgumentException e) {
            System.out.println("\nUpdating contact ERROR: " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("\nUnexpected ERROR: " + e.getMessage() + "\n");
        }
    }

    // METHOD TO DELETE A CONTACT BY ID
    private static void deleteContact() {
        System.out.print("DELETING: \nENTER CONTACT ID: ");
        String id = scanner.nextLine();

        try {
            contactService.deleteContact(id);
            System.out.println("\nContact " + id + " deleted!\n");
        } catch (IllegalArgumentException e) {
            System.out.println("\nDeleting contact ERROR!: " + e.getMessage() + "\n");
        }
    }

    // METHOD TO DISPLAY ALL CONTACTS IN LIST FORMAT
    private static void listContacts() {
        System.out.println("\n***** LISTING ALL CONTACTS ON FILE *****");
        System.out.printf("%-10s %-12s %-12s %-15s %-30s%n", 
            "ID", "FIRST NAME", "LAST NAME", "PHONE", "ADDRESS");
        System.out.println("--------------------------------------------------------------------------------");

        Collection<Contact> contacts = contactService.getAllContacts();

        if (contacts.isEmpty()) {
            System.out.println("NO CONTACTS FOUND.\n");
            return;
        }

        for (Contact contact : contacts) {
            System.out.printf("%-10s %-12s %-12s %-15s %-30s%n",
                contact.getContactId(),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getPhone(),
                contact.getAddress()
            );
        }

        System.out.println("--------------------------------------------------------------------------------\n");
    }

    // SIMULATES A CLEAR SCREEN BY PRINTING MULTIPLE LINES
    private static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    // MAIN METHOD TO RUN THE CONTACT SERVICE APPLICATION
    public static void main(String[] args) {
        char userInput = 'C'; // INITIAL VALUE TO ENTER LOOP

        do {
            showMenu(); // DISPLAY MENU
            String input = scanner.nextLine();

            if (!input.isEmpty()) {
                userInput = Character.toUpperCase(input.charAt(0));

                switch (userInput) {
                    case 'A':
                        addContact();
                        break;
                    case 'S':
                        readContact();
                        break;
                    case 'E':
                        updateContact();
                        break;
                    case 'D':
                        deleteContact();
                        break;
                    case 'L':
                        listContacts();
                        break;
                    case 'Q':
                        System.out.println("EXITING APPLICATION. GOODBYE!");
                        break;
                    case 'C':
                        clearScreen();
                        break;
                    default:
                        System.out.println("OPTION SELECTED DOES NOT EXIST.\nMAKE A SELECTION FROM THE MENU OPTION.\n");
                }
            }

        } while (userInput != 'Q');
    }
}
