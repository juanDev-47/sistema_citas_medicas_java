package org.example.repository;

import org.example.model.Employee;
import org.example.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository implements Repository<Employee> {

    List<Employee> employees = new ArrayList<>();
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }


    @Override
    public List<Employee> findAll() throws SQLException {
        try(Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Employee");){
            while (rs.next()){
                createEmployee(rs);
            }
        }
        return employees;
    }



    @Override
    public Employee getById(Integer id) throws SQLException {
        try(Statement stmt = getConnection().prepareStatement("SELECT * FROM Employee WHERE id = ?");){
            ((PreparedStatement) stmt).setInt(1, id);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Employee employee) {

    }

    @Override
    public void update(Employee employee) {

    }

    @Override
    public void delete(Integer id) {

    }

    private Employee createEmployee(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setId(rs.getInt("id"));
        e.setFirst_name(rs.getString("first_name"));
        e.setPa_surname(rs.getString("pa_surname"));
        e.setMa_surname(rs.getString("ma_surname"));
        e.setEmail(rs.getString("email"));
        e.setSalary(rs.getFloat("salary"));

        return e;
    }
}
