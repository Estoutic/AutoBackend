package com.drujba.autobackend.services.calculate;

import com.drujba.autobackend.models.dto.calculate.CustomsCalculationRequestDto;
import com.drujba.autobackend.models.dto.calculate.CustomsCalculationResponseDto;

public interface ICalculateService {

    CustomsCalculationResponseDto calculateCustoms(CustomsCalculationRequestDto request);
}
