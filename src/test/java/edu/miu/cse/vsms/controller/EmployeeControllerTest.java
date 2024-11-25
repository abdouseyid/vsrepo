package edu.miu.cse.vsms.controller;

import com.google.gson.Gson;
import edu.miu.cse.vsms.dto.request.EmployeeRequestDto;
import edu.miu.cse.vsms.dto.response.EmployeeResponseDto;
import edu.miu.cse.vsms.dto.response.VehicleServiceResponseDto;
import edu.miu.cse.vsms.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    void createEmployee() throws Exception {
        List<VehicleServiceResponseDto> services = new ArrayList<>();
        services.add(new VehicleServiceResponseDto(1L, "Oil change", 120.0, "Motorcycle"));
        services.add(new VehicleServiceResponseDto(2L, "Tire replacement", 180.50, "Truck"));

        EmployeeRequestDto request =new EmployeeRequestDto(
                "John Doe",
                "john.doe@example.com",
                "1234567890",
                LocalDate.parse("2024-01-10")
        );

        EmployeeResponseDto response = new EmployeeResponseDto(
                1L,
                "John Doe",
                "john.doe@example.com",
                "1234567890",
                LocalDate.parse("2024-01-10"),
                services
        );

        Mockito.when(employeeService.addEmployee(request)).thenReturn(response);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/employess")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.content().json(new Gson().toJson(response))
                );
    }
}
