//package com.drujba.autobackend.controllers;
//
//import com.drujba.autobackend.controllers.car.CarModelController;
//import com.drujba.autobackend.db.repostiories.car.CarModelRepository;
//import com.drujba.autobackend.models.dto.car.CarModelDto;
//import com.drujba.autobackend.services.car.ICarModelService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.util.List;
//import java.util.UUID;
//
//import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.patch;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(CarModelController.class)
//@AutoConfigureMockMvc(addFilters = false)
//class CarModelControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ICarModelService carModelService;
//
//    @MockBean
//    private CarModelRepository carModelRepository;
//
//    @Test
//    void testCreateModel() throws Exception {
//        CarModelDto carModelDto = new CarModelDto();
//        UUID modelId = UUID.randomUUID();
//
//        Mockito.when(carModelService.saveCarModel(Mockito.any(CarModelDto.class))).thenReturn(modelId);
//
//        mockMvc.perform(post("/car/model")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(carModelDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$").value(modelId.toString()));
//    }
//
//    @Test
//    void testDeleteModel() throws Exception {
//        UUID modelId = UUID.randomUUID();
//
//        Mockito.doNothing().when(carModelService).deleteCarModel(modelId);
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/car/model/{id}", modelId))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void testUpdateModel() throws Exception {
//        UUID modelId = UUID.randomUUID();
//        CarModelDto carModelDto = new CarModelDto();
//
//        Mockito.doNothing().when(carModelService).updateCarModel(Mockito.eq(modelId), Mockito.any(CarModelDto.class));
//
//        mockMvc.perform(patch("/car/model/{id}", modelId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(carModelDto)))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void testGetBrands() throws Exception {
//        List<String> brands = List.of("Toyota", "Honda");
//
//        Mockito.when(carModelRepository.findDistinctBrands()).thenReturn(brands);
//
//        mockMvc.perform(get("/car/model/brands"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(brands.size()))
//                .andExpect(jsonPath("$[0]").value("Toyota"))
//                .andExpect(jsonPath("$[1]").value("Honda"));
//    }
//
//    @Test
//    void testGetModelsByBrand() throws Exception {
//        String brand = "Toyota";
//        List<String> models = List.of("Camry", "Corolla");
//
//        Mockito.when(carModelRepository.findDistinctModelsByBrand(brand)).thenReturn(models);
//
//        mockMvc.perform(get("/car/model/models")
//                        .param("brand", brand))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(models.size()))
//                .andExpect(jsonPath("$[0]").value("Camry"))
//                .andExpect(jsonPath("$[1]").value("Corolla"));
//    }
//
//    @Test
//    void testGetGenerationsByModel() throws Exception {
//        String model = "Camry";
//        List<String> generations = List.of("2007-2011", "2012-2016");
//
//        Mockito.when(carModelRepository.findDistinctGenerationsByModel(model)).thenReturn(generations);
//
//        mockMvc.perform(get("/car/model/generations")
//                        .param("model", model))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(generations.size()))
//                .andExpect(jsonPath("$[0]").value("2007-2011"))
//                .andExpect(jsonPath("$[1]").value("2012-2016"));
//    }
//}
