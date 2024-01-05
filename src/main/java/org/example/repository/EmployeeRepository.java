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
                Employee e = createEmployee(rs);
                employees.add(e);
            }
        }
        return employees;
    }



    @Override
    public Employee getById(Integer id) throws SQLException {
        Employee employee = null;
        try(Statement stmt = getConnection().prepareStatement("SELECT * FROM Employee WHERE id = ?");){
            ((PreparedStatement) stmt).setInt(1, id);

            try(ResultSet rs = ((PreparedStatement) stmt).executeQuery();){
                if(rs.next()){
                    employee = createEmployee(rs);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return employee;
    }

    @Override
    public void save(Employee employee) throws SQLException {
        String sql = "INSERT INTO Employee (first_name, pa_surname, ma_surname, email, salary) VALUES (?, ?, ?, ?, ?)";
        try(Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);){
            stmt.setString(1, employee.getFirst_name());
            stmt.setString(2, employee.getPa_surname());
            stmt.setString(3, employee.getMa_surname());
            stmt.setString(4, employee.getEmail());
            stmt.setFloat(5, employee.getSalary());

            stmt.executeUpdate();

        }
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
