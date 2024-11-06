package pl.techcorp.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import pl.techcorp.employee.domain.Person;
import pl.techcorp.employee.exception.EmployeeNotFoundException;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                int id = Integer.parseInt(fields[0]);
                String firstName = fields[1];
                String lastName = fields[2];
                String company = fields[3];
                String country = fields[4];
                employees.add(new Person(id, firstName, lastName, company, country));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
