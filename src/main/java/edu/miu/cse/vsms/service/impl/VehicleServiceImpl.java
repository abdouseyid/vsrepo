package edu.miu.cse.vsms.service.impl;

import edu.miu.cse.vsms.dto.request.ServiceRequestDto;
import edu.miu.cse.vsms.dto.response.VehicleServiceResponseDto;
import edu.miu.cse.vsms.exception.ResourceNotFoundException;
import edu.miu.cse.vsms.model.Employee;
import edu.miu.cse.vsms.model.VService;
import edu.miu.cse.vsms.repository.EmployeeRepository;
import edu.miu.cse.vsms.repository.VehicleServiceRepository;
import edu.miu.cse.vsms.service.VehicleService;
import lombok.RequiredArgsConstructor;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleServiceRepository vehicleServiceRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public VehicleServiceResponseDto assignService(ServiceRequestDto request) {
        // Write your code here
        VService vservice = new VService();
        vservice.setServiceName(request.serviceName());
        vservice.setCost(request.cost());
        vservice.setVehicleType(request.vehicleType());

        Employee employee = employeeRepository.findById(request.employeeId()).get();
        vservice.setEmployee(employee);

        VService savedService = vehicleServiceRepository.save(vservice);
        VehicleServiceResponseDto vehicleServiceResponseDto = new VehicleServiceResponseDto(
                savedService.getId(),
                savedService.getServiceName(),
                savedService.getCost(),
                savedService.getVehicleType()
        );
        return vehicleServiceResponseDto;
    }
}
