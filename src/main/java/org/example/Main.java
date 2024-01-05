package org.example;


import org.example.model.Employee;
import org.example.repository.EmployeeRepository;
import org.example.repository.Repository;
import org.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException {

        try (Connection conn = DatabaseConnection.getConnection();) {
            Repository<Employee> repository = new EmployeeRepository();

//            Employee employee = new Employee("Juan", "Perez", "Gomez", "juan@gmail.com", 1000.0f);
//
//            repository.save(employee);


//        repository.findAll().forEach(System.out::println);
//            System.out.println(repository.getById(2));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}