package pl.techcorp.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import pl.techcorp.employee.domain.Person;
import pl.techcorp.employee.exception.EmployeeNotFoundException;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class EmployeeService {
    private List<Person> employees = new ArrayList<>();

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    public void init() {
        loadEmployeesFromCsv();
    }

    private void loadEmployeesFromCsv() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(resourceLoader.getResource("classpath:MOCK_DATA.csv").getInputStream()))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                int id = Integer.parseInt(fields[0]);
                String firstName = fields[1];
                String lastName = fields[2];
                String company = fields[3];
                String country = fields[4];
                double salary = Double.parseDouble(fields[5]);
                String currency = fields[6]; // Read the currency from the CSV

                employees.add(new Person(id, firstName, lastName, company, country, salary, currency));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Map<String, Double> getTotalSalariesByCurrency() {
    List<Person> employees = getAllEmployees(); // Pobierz wszystkich pracowników
    
    // Grupowanie pracowników po walucie i sumowanie ich wynagrodzeń
    return employees.stream()
            .collect(Collectors.groupingBy(
                    Person::getCurrency, // Grupa po walucie
                    Collectors.summingDouble(Person::getSalary) // Sumowanie wynagrodzeń
            ));
}



    public List<Person> getAllEmployees() {
        return employees;
    }

    public Person getEmployeeById(int id) {
        return employees.stream()
                .filter(emp -> emp.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EmployeeNotFoundException(id));  
    }

    public void addEmployee(Person person) {
        if (employees.stream().anyMatch(emp -> emp.getId() == person.getId())) {
            throw new IllegalArgumentException("Pracownik o ID " + person.getId() + " już istnieje");
        }
        employees.add(person);
    }

    public void updateEmployee(int id, Person person) {
        Optional<Person> existingEmployee = employees.stream()
                .filter(emp -> emp.getId() == id)
                .findFirst();

        if (existingEmployee.isEmpty()) {
            throw new EmployeeNotFoundException(id); 
        }


        person.setId(id);
        employees.set(employees.indexOf(existingEmployee.get()), person);
    }

    public void deleteEmployee(int id) {
        boolean removed = employees.removeIf(emp -> emp.getId() == id);
        if (!removed) {
            throw new EmployeeNotFoundException(id);
        }
    }
}
