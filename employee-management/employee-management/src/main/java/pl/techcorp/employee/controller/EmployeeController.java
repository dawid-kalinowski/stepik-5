package pl.techcorp.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.techcorp.employee.domain.Person;
import pl.techcorp.employee.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Person> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getEmployeeById(@PathVariable int id) {
        Person person = employeeService.getEmployeeById(id);
        return person != null ? new ResponseEntity<>(person, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Person> addEmployee(@RequestBody Person person) {
        employeeService.addEmployee(person);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Person> updateEmployee(@PathVariable int id, @RequestBody Person person) {
        Person existingEmployee = employeeService.getEmployeeById(id);
        if (existingEmployee == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Ustaw ID w obiekcie person przed aktualizacją
        person.setId(id);
        
        employeeService.updateEmployee(id, person);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
