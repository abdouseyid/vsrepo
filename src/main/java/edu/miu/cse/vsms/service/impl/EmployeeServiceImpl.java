package edu.miu.cse.vsms.service.impl;

import edu.miu.cse.vsms.dto.request.EmployeeRequestDto;
import edu.miu.cse.vsms.dto.response.EmployeeResponseDto;
import edu.miu.cse.vsms.dto.response.VehicleServiceResponseDto;
import edu.miu.cse.vsms.model.Employee;
import edu.miu.cse.vsms.model.VService;
import edu.miu.cse.vsms.repository.EmployeeRepository;
import edu.miu.cse.vsms.repository.VehicleServiceRepository;
import edu.miu.cse.vsms.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final VehicleServiceRepository vehicleServiceRepository;

    @Override
    public EmployeeResponseDto addEmployee(EmployeeRequestDto request) {
        // Write your code here
        Employee employee = new Employee();
        employee.setName(request.name());
        employee.setEmail(request.email());
        employee.setPhone(request.phone());
        employee.setHireDate(request.hireDate());

        List<VService> vServices = vehicleServiceRepository.findAllVServiceByEmployeeId(employee.getId());
//        employee.setVServices(vServices);

        List<VehicleServiceResponseDto> vehicleServiceResponseDtos = new ArrayList<>();
        for (VService vService : vServices) {
            vehicleServiceResponseDtos.add(new VehicleServiceResponseDto(
                    vService.getId(),
                    vService.getServiceName(),
                    vService.getCost(),
                    vService.getVehicleType()
            ));
        }
        Employee savedEmployee = employeeRepository.save(employee);
        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto(
                savedEmployee.getId(),
                savedEmployee.getName(),
                savedEmployee.getEmail(),
                savedEmployee.getPhone(),
                savedEmployee.getHireDate(),
                vehicleServiceResponseDtos
        );
        return employeeResponseDto;
    }

    @Override
    public List<EmployeeResponseDto> getAllEmployees() {
        // Write your code here
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeResponseDto> employeeResponseDtos = new ArrayList<>();


        for (Employee employee : employees) {

            List<VehicleServiceResponseDto> vehicleServiceResponseDtos = new ArrayList<>();
            for (VService vService : employee.getVServices()) {
                vehicleServiceResponseDtos.add(new VehicleServiceResponseDto(
                        vService.getId(),
                        vService.getServiceName(),
                        vService.getCost(),
                        vService.getVehicleType()
                ));
            }

            EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto(
                    employee.getId(),
                    employee.getName(),
                    employee.getEmail(),
                    employee.getPhone(),
                    employee.getHireDate(),
                    vehicleServiceResponseDtos
            );
            employeeResponseDtos.add(employeeResponseDto);
        }
        return employeeResponseDtos;
    }

    @Override
    public EmployeeResponseDto getEmployeeById(Long id) {
        // Write your code here
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found with id" + id));
        return mapToResponseDto(employee);
    }

    @Override
    public EmployeeResponseDto partiallyUpdateEmployee(Long id, Map<String, Object> updates) {
        // Fetch the employee by ID or throw an exception if not found
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id " + id));

        // Apply each update based on the key
        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    // Write your code here
                    employee.setName((String) value);
                    break;
                case "email":
                    // Write your code here
                    employee.setEmail((String) value);
                    break;
                case "phone":
                    // Write your code here
                    employee.setPhone((String) value);
                    break;
                case "hireDate":
                    // Write your code here
                    employee.setHireDate(LocalDate.parse((String) value));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + key);
            }
        });
        // Write your code here
        Employee savedEmployee = employeeRepository.save(employee);

        return mapToResponseDto(savedEmployee);
    }

    private EmployeeResponseDto mapToResponseDto(Employee employee) {
        List<VehicleServiceResponseDto> serviceDtos = employee.getVServices().stream()
                .map(service -> new VehicleServiceResponseDto(
                        service.getId(),
                        service.getServiceName(),
                        service.getCost(),
                        service.getVehicleType()
                )).toList();

        return new EmployeeResponseDto(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getHireDate(),
                serviceDtos
        );
    }
}
