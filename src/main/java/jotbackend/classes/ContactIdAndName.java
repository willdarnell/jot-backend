package jotbackend.classes;
import jotbackend.classes.Contact;

public interface ContactIdAndName{
    String getContactId();
    String getFirstName();
    String getLastName();

    default String getFullName() {
        return getFirstName().concat(" ").concat(getLastName());
    }
}