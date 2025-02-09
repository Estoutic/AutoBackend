package com.drujba.autobackend.controllers.calculate;

import com.drujba.autobackend.models.dto.calculate.CustomsCalculationRequestDto;
import com.drujba.autobackend.models.dto.calculate.CustomsCalculationResponseDto;
import com.drujba.autobackend.services.calculate.ICalculateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculate")
@RequiredArgsConstructor
public class CalculateController {


    private final ICalculateService calculationService;

    @PostMapping("")
    public ResponseEntity<CustomsCalculationResponseDto> calculate(@RequestBody CustomsCalculationRequestDto request) {
        return ResponseEntity.ok(calculationService.calculate(request));
    }
}
