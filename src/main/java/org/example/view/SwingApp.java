package org.example.view;

import org.example.model.Employee;
import org.example.repository.EmployeeRepository;
import org.example.repository.Repository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SwingApp extends JFrame {
    private final Repository<Employee> employeeRepository;

    private final JTable employeeTable;

    public SwingApp(){
        setTitle("Gestion de empleados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 230);

        // crear una tabla para mostrar los empleados
        employeeTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        // crear botones para las acciones
        JButton agregarButton = new JButton("Agregar");
        JButton ActualizarButton = new JButton("Actualizar");
        JButton EliminarButton = new JButton("Eliminar");

        // Configurar el panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(agregarButton);
        buttonPanel.add(ActualizarButton);
        buttonPanel.add(EliminarButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Establecer los estilos para los botones
        agregarButton.setBackground(new Color(46, 204, 113));
        agregarButton.setForeground(Color.WHITE);
        agregarButton.setFocusPainted(false);

        ActualizarButton.setBackground(new Color(52, 152, 219));
        ActualizarButton.setForeground(Color.WHITE);
        ActualizarButton.setFocusPainted(false);

        agregarButton.setBackground(new Color(231, 76, 60));
        agregarButton.setForeground(Color.WHITE);
        agregarButton.setFocusPainted(false);

        // crear el objeto repository para acceder a la base de datos
        employeeRepository = new EmployeeRepository();

        // cargar los empleados iniciales en la tabla
        refreshEmployeeTable();

        // agregar actionListener para los botones
        agregarButton.addActionListener(e -> {
            try {
                agregarEmpleado();
            } catch (Exception ex){
                throw new RuntimeException(ex);
            }
        });

        ActualizarButton.addActionListener(e -> actualizarEmpleado());

        EliminarButton.addActionListener(e -> eliminarEmpleado());

    }

    private void refreshEmployeeTable() {
        // obtener la lista actualizada de empleados desde la base de datos
        try {
            List<Employee> employees = employeeRepository.findAll();

            // crear un modelo de tabla y establecer los datos en los empleados
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Nombre");
            model.addColumn("Apellido Paterno");
            model.addColumn("Apellido Materno");
            model.addColumn("Email");
            model.addColumn("Salario");

            for(Employee employee : employees){
                Object[] rowData = {
                        employee.getId(),
                        employee.getFirst_name(),
                        employee.getPa_surname(),
                        employee.getMa_surname(),
                        employee.getEmail(),
                        employee.getSalary()
                };
                model.addRow(rowData);
            }

            // establecer el modelo de tabla actualizado
            employeeTable.setModel(model);

        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error al obtener los empleados");
        }

    }

    private void agregarEmpleado() throws Exception {
        JTextField nombreField = new JTextField();
        JTextField paternoField = new JTextField();
        JTextField maternoField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField salarioField = new JTextField();

        Object[] fields = {
          "Nombre:", nombreField,
          "Apellido Paterno:", paternoField,
          "Apellido Materno:", maternoField,
          "Email:", emailField,
          "Salario:", salarioField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Agregar Empleado", JOptionPane.OK_OPTION);

        if(result==JOptionPane.OK_OPTION){
            //Crear un nuevo objeto Employee con los datos ingresados
            Employee employee=new Employee();
            employee.setFirst_name(nombreField.getText());
            employee.setPa_surname(paternoField.getText());
            employee.setMa_surname(maternoField.getText());
            employee.setEmail(emailField.getText());
            employee.setSalary(Float.parseFloat(salarioField.getText()));

            //Gueardar el nuevo empleado en la bd
            employeeRepository.save(employee);

            //Actualizar la tabla con los empleados actualizados
            refreshEmployeeTable();

            JOptionPane.showMessageDialog(this,"Empleado agregado correctamente","éxito",JOptionPane.OK_OPTION);
        }
    }

    private void actualizarEmpleado() {
        //Obtener el Id del empleado a actualizar
        String empleadoIdStr = JOptionPane.showInputDialog(this, "Ingrese el id del empleado", "Actualizar", JOptionPane.OK_OPTION);
        if (empleadoIdStr != null) {
            try {
                int empleadoId = Integer.parseInt(empleadoIdStr);

                //Obtener el empleado desde la base de datos

                Employee empleado = employeeRepository.getById(empleadoId);

                if (empleado != null) {
                    //Crear un formulario con los datos del empleado
                    JTextField nombreField = new JTextField(empleado.getFirst_name());
                    JTextField apellidoPaternoField = new JTextField(empleado.getPa_surname());
                    JTextField apellidoMaternoField = new JTextField(empleado.getMa_surname());
                    JTextField emailField = new JTextField(empleado.getEmail());
                    JTextField salarioField = new JTextField(String.valueOf(empleado.getSalary()));

                    Object[] fields = {
                            "Nombre", nombreField,
                            "Apellido paterno", apellidoPaternoField,
                            "Apellido materno", apellidoMaternoField,
                            "Email", emailField,
                            "Salario:", salarioField
                    };

                    int confirmResult = JOptionPane.showConfirmDialog(this, fields, "Actualizar Empleado", JOptionPane.OK_OPTION);
                    if (confirmResult == JOptionPane.OK_OPTION) {
                        //Actualizar los datos del empleado con los valores ingresados en el formulario
                        empleado.setFirst_name(nombreField.getText());
                        empleado.setPa_surname(apellidoPaternoField.getText());
                        empleado.setMa_surname(apellidoMaternoField.getText());
                        empleado.setEmail(emailField.getText());
                        empleado.setSalary(Float.parseFloat(salarioField.getText()));

                        //Guardar los cambios en la base de datos
                        employeeRepository.save(empleado);

                        //Actualizar la tabla de empleados en la interfaz
                        refreshEmployeeTable();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ningún empleado con el ID estipulado", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ingrese un valor numérico válido", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al obtener los datos de la bd", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void eliminarEmpleado(){
        //Obtener el ID del empleado a eliminar
        String empleadoIdStr=JOptionPane.showInputDialog(this,"Ingrese el ID del empleado a eliminar","Eliminar empleado",JOptionPane.OK_OPTION);
        if(empleadoIdStr!=null){
            try{
                int empleadoId=Integer.parseInt(empleadoIdStr);

                //Confirmar la eliminación del empleado
                int confirmResult=JOptionPane.showConfirmDialog(this,"¿Está seguro de eliminar el usuario seleccionado?","confirmar eliminación",JOptionPane.OK_CANCEL_OPTION);
                if(confirmResult==JOptionPane.YES_OPTION){
                    //Eliminar el empleado de la base de datos
                    employeeRepository.delete(empleadoId);

                    //Actualizar la tabla de empleaods en la interfaz
                    refreshEmployeeTable();
                }



            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(this,"Ingrese un valor numérico válido para el ID","Error",JOptionPane.ERROR_MESSAGE);

            }catch(SQLException e){
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

}
