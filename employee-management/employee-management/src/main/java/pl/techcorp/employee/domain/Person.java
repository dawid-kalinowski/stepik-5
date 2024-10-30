package pl.techcorp.employee.domain;

public class Person {
    private int id;
    private String firstName;
    private String lastName;
    private String company;
    private String country;

    // Gettery i settery
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    // Konstruktor
    public Person(int id, String firstName, String lastName, String company, String country) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.country = country;
    }
}
