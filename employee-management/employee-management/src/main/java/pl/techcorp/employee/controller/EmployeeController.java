package pl.techcorp.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.techcorp.employee.domain.Person;
import pl.techcorp.employee.exception.EmployeeNotFoundException;
import pl.techcorp.employee.service.EmployeeService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Wyświetlanie listy wszystkich pracowników
    @GetMapping("/employees")
    public String viewEmployees(Model model) {
        List<Person> employees = employeeService.getAllEmployees();
        
        // Przekazanie listy pracowników do modelu
        model.addAttribute("employees", employees);
        
        // Całkowita liczba pracowników
        model.addAttribute("totalEmployees", employees.size());
        
        // Lista krajów do rozwijanego menu (dropdown)
        List<String> countries = employees.stream()
            .map(Person::getCountry)
            .distinct()
            .collect(Collectors.toList());
        model.addAttribute("countries", countries);

        // Podział sumy wynagrodzeń na waluty (przykład)
        Map<String, Double> totalSalariesByCurrency = employeeService.getTotalSalariesByCurrency();
        model.addAttribute("totalSalariesByCurrency", totalSalariesByCurrency);

        return "employees"; // Widok strony głównej
    }

    // Wyświetlanie szczegółów konkretnego pracownika
    @GetMapping("/employees/{id}")
    public String viewEmployeeDetails(@PathVariable int id, Model model) {
        Person employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            throw new EmployeeNotFoundException(id);
        }
        model.addAttribute("employee", employee);
        return "employee-details"; // Widok szczegółów pracownika
    }

    // API - pobieranie wszystkich pracowników
    @GetMapping("/api")
    @ResponseBody
    public List<Person> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // API - pobieranie pracownika po ID
    @GetMapping("/api/{id}")
    @ResponseBody
    public Person getEmployeeById(@PathVariable int id) {
        return employeeService.getEmployeeById(id);
    }

    // API - dodawanie pracownika
    @PostMapping("/api")
    @ResponseBody
    public Person addEmployee(@RequestBody Person person) {
        employeeService.addEmployee(person);
        return person;
    }

    // API - aktualizacja danych pracownika
    @PutMapping("/api/{id}")
    @ResponseBody
    public Person updateEmployee(@PathVariable int id, @RequestBody Person person) {
        employeeService.updateEmployee(id, person);
        return person;
    }

    // API - usuwanie pracownika
    @DeleteMapping("/api/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
    }
}
