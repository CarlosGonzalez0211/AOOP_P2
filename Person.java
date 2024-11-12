/**
 * This method represents a person with personal details such as ID number, name,
 * date of birth, address, and phone number.
 *
 * @author Daniela Castro Enriquez
 * @author Carlos Gonzalez
 * @author Aylin Rodriguez
 *
 */
public class Person {

    /** The unique identification number for the person. */
    private String idNumber;

    /** The first name of the person. */
    private String firstName;

    /** The last name of the person. */
    private String lastName;

    /** The date of birth of the person in the format YYYY-MM-DD. */
    private String dateOfBirth;

    /** The personal address of the person. */
    private String address;

    /** The phone number of the person. */
    private String phoneNumber;

    private String city;
    private String state;
    private String zip;
    
    private static String maxID = "-1";
    
        /**
         * This method constructs a new Person with the specified personal details.
         *
         * @param idNumber   the identification number of the person
         * @param firstName  the first name of the person
         * @param lastName   the last name of the person
         * @param dateOfBirth the date of birth of the person
         * @param address    the address of the person
         * @param phoneNumber the phone number of the person
         */
        public Person(String idNumber, String firstName, String lastName, String dateOfBirth, String address, String phoneNumber) {
            this.idNumber = idNumber;
            this.firstName = firstName;
            this.lastName = lastName;
            this.dateOfBirth = dateOfBirth;
            this.address = address;
            this.phoneNumber = phoneNumber;
            setMaxID(idNumber);
        }

        public Person(String idNumber, String firstName, String lastName, String dateOfBirth, String address, String city, String state, String zip, String phoneNumber) {
            this.idNumber = idNumber;
            this.firstName = firstName;
            this.lastName = lastName;
            this.dateOfBirth = dateOfBirth;
            this.address = address;
            this.city = city;
            this.state = state;
            this.zip = zip;
            this.phoneNumber = phoneNumber;
            //setMaxID(idNumber);
        }
    
        /**
         * This method sets the identification number of the person.
         *
         * @param idNumber the identification number
         */
        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }
    
        /**
         * This method assigns the first name of the person.
         *
         * @param firstName the first name of the person
         */
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
    
        /**
         * This method assigns the last name of the person.
         *
         * @param lastName the last name of the person
         */
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    
        /**
         * This emthod assigns the date of birth of the person.
         *
         * @param dateOfBirth the date of birth
         */
        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }
        
        /**
         * This method sets the address of the person.
         *
         * @param address the address
         */
        public void setAddress(String address) {
            this.address = address;
        } 

        public void setCity(String city) {
            this.city = city;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }
    
        /**
         * This method sets the phone number of the person.
         *
         * @param phoneNumber the phone number
         */
        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    
    
        public void setMaxID(String incomingMaxID){
            int parsedMaxID = Integer.parseInt(this.maxID);
            int currentMaxID = Integer.parseInt(incomingMaxID);
            if (parsedMaxID < currentMaxID){
                maxID = incomingMaxID;
            }
        }
    
        public static int getMaxId(){
            return Integer.parseInt(maxID);
    }
    /**
     * Retrieves the identification number of the person.
     *
     * @return the identification number
     */
    public String getIdNumber() {
        return this.idNumber;
    }

    /**
     * Retrieves the first name of the person.
     *
     * @return the first name
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Retrieves the last name of the person.
     *
     * @return the last name
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Retrieves the date of birth of the person.
     *
     * @return the date of birth
     */
    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    /**
     * Retrieves the address of the person.
     *
     * @return the address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Retrieves the phone number of the person.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
}