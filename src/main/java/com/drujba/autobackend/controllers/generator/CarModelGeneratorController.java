package com.drujba.autobackend.controllers.generator;

import com.drujba.autobackend.models.dto.car.CarCreationDto;
import com.drujba.autobackend.models.dto.car.CarModelDto;
import com.drujba.autobackend.models.enums.car.*;
import com.drujba.autobackend.services.car.ICarModelService;
import com.drujba.autobackend.services.car.ICarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

/**
 * REST Controller for generating car model data
 */
@RestController
@RequestMapping("/api/cars/generate")
@RequiredArgsConstructor
@Slf4j
public class CarModelGeneratorController {

    private final ICarModelService carModelService;
    private final ICarService carService;

    /**
     * Generates a specified number of random car models
     * @param count Number of car models to generate (default 30)
     * @return List of generated car model IDs
     */
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PostMapping("/models")
    public ResponseEntity<Map<String, Object>> generateCarModels(
            @RequestParam(defaultValue = "30") int count) {

        log.info("Starting car model generation for {} models...", count);

        List<UUID> generatedIds = new ArrayList<>();
        Set<String> uniqueModelKeys = new HashSet<>();

        // Define car brands and their models with generations
        Map<String, Map<String, List<String>>> carData = new HashMap<>();

        // BMW
        Map<String, List<String>> bmwModels = new HashMap<>();
        bmwModels.put("3 Series", Arrays.asList("E30", "E36", "E46", "E90", "F30", "G20"));
        bmwModels.put("5 Series", Arrays.asList("E28", "E34", "E39", "E60", "F10", "G30"));
        bmwModels.put("X5", Arrays.asList("E53", "E70", "F15", "G05"));
        carData.put("BMW", bmwModels);

        // Mercedes
        Map<String, List<String>> mercedesModels = new HashMap<>();
        mercedesModels.put("C-Class", Arrays.asList("W202", "W203", "W204", "W205", "W206"));
        mercedesModels.put("E-Class", Arrays.asList("W124", "W210", "W211", "W212", "W213"));
        mercedesModels.put("S-Class", Arrays.asList("W140", "W220", "W221", "W222", "W223"));
        carData.put("Mercedes", mercedesModels);

        // Audi
        Map<String, List<String>> audiModels = new HashMap<>();
        audiModels.put("A4", Arrays.asList("B5", "B6", "B7", "B8", "B9"));
        audiModels.put("A6", Arrays.asList("C4", "C5", "C6", "C7", "C8"));
        audiModels.put("Q5", Arrays.asList("8R", "FY"));
        carData.put("Audi", audiModels);

        // Toyota
        Map<String, List<String>> toyotaModels = new HashMap<>();
        toyotaModels.put("Corolla", Arrays.asList("E90", "E100", "E110", "E120", "E140", "E170", "E210"));
        toyotaModels.put("Camry", Arrays.asList("XV10", "XV20", "XV30", "XV40", "XV50", "XV70"));
        toyotaModels.put("RAV4", Arrays.asList("XA10", "XA20", "XA30", "XA40", "XA50"));
        carData.put("Toyota", toyotaModels);

        // Honda
        Map<String, List<String>> hondaModels = new HashMap<>();
        hondaModels.put("Civic", Arrays.asList("EG", "EK", "EP", "FD", "FK", "FL"));
        hondaModels.put("Accord", Arrays.asList("CB", "CD", "CE", "CF", "CL", "CM", "CR"));
        hondaModels.put("CR-V", Arrays.asList("RD1", "RD4", "RE", "RM", "RW"));
        carData.put("Honda", hondaModels);

        // Subaru
        Map<String, List<String>> subaruModels = new HashMap<>();
        subaruModels.put("Forester", Arrays.asList("SF", "SG", "SH", "SJ", "SK"));
        subaruModels.put("Impreza", Arrays.asList("GC", "GD", "GE", "GJ", "GT"));
        subaruModels.put("Outback", Arrays.asList("BG", "BH", "BP", "BR", "BS"));
        carData.put("Subaru", subaruModels);

        // Nissan
        Map<String, List<String>> nissanModels = new HashMap<>();
        nissanModels.put("Altima", Arrays.asList("L30", "L31", "L32", "L33", "L34"));
        nissanModels.put("X-Trail", Arrays.asList("T30", "T31", "T32"));
        nissanModels.put("Qashqai", Arrays.asList("J10", "J11", "J12"));
        carData.put("Nissan", nissanModels);

        // Mazda
        Map<String, List<String>> mazdaModels = new HashMap<>();
        mazdaModels.put("3", Arrays.asList("BK", "BL", "BM", "BP"));
        mazdaModels.put("6", Arrays.asList("GG", "GH", "GJ", "GL"));
        mazdaModels.put("CX-5", Arrays.asList("KE", "KF", "KG"));
        carData.put("Mazda", mazdaModels);

        // Get all brands
        List<String> brands = new ArrayList<>(carData.keySet());
        Random random = new Random();

        // Track generated models details for response
        List<Map<String, String>> generatedModels = new ArrayList<>();

        // Generate and save unique car models
        int attempts = 0;
        int maxAttempts = count * 2; // To prevent infinite loop if we run out of unique combinations

        while (generatedIds.size() < count && attempts < maxAttempts) {
            attempts++;

            // Select random brand
            String brand = brands.get(random.nextInt(brands.size()));
            Map<String, List<String>> models = carData.get(brand);

            // Select random model
            List<String> modelNames = new ArrayList<>(models.keySet());
            String modelName = modelNames.get(random.nextInt(modelNames.size()));

            // Select random generation
            List<String> generations = models.get(modelName);
            String generation = generations.get(random.nextInt(generations.size()));

            // Create unique key to avoid duplicates
            String modelKey = brand + "-" + modelName + "-" + generation;

            if (uniqueModelKeys.add(modelKey)) {
                CarModelDto carModelDto = new CarModelDto();
                carModelDto.setBrand(brand);
                carModelDto.setModel(modelName);
                carModelDto.setGeneration(generation);

                try {
                    UUID id = carModelService.saveCarModel(carModelDto);
                    generatedIds.add(id);

                    Map<String, String> modelDetails = new HashMap<>();
                    modelDetails.put("id", id.toString());
                    modelDetails.put("brand", brand);
                    modelDetails.put("model", modelName);
                    modelDetails.put("generation", generation);
                    generatedModels.add(modelDetails);

                    log.info("Created car model: {} {} {}, ID: {}", brand, modelName, generation, id);
                } catch (Exception e) {
                    log.error("Failed to create car model: {} {} {}", brand, modelName, generation, e);
                }
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", String.format("Generated %d car models", generatedIds.size()));
        response.put("generatedCount", generatedIds.size());
        response.put("models", generatedModels);

        log.info("Car model generation completed. Created {} models.", generatedIds.size());

        return ResponseEntity.ok(response);
    }

    /**
     * Generates a specified number of random cars
     * @param count Number of cars to generate (default 50)
     * @return Map with generation results
     */
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PostMapping("/cars")
    public ResponseEntity<Map<String, Object>> generateCars(
            @RequestParam(defaultValue = "50") int count) {

        log.info("Starting car generation for {} cars...", count);

        List<UUID> generatedIds = new ArrayList<>();
        List<Map<String, Object>> generatedCars = new ArrayList<>();

        // Random data for car generation
        Random random = new Random();

        // Define car brands and their models with generations
        Map<String, Map<String, List<String>>> carData = new HashMap<>();

        // BMW
        Map<String, List<String>> bmwModels = new HashMap<>();
        bmwModels.put("3 Series", Arrays.asList("E30", "E36", "E46", "E90", "F30", "G20"));
        bmwModels.put("5 Series", Arrays.asList("E28", "E34", "E39", "E60", "F10", "G30"));
        bmwModels.put("X5", Arrays.asList("E53", "E70", "F15", "G05"));
        carData.put("BMW", bmwModels);

        // Mercedes
        Map<String, List<String>> mercedesModels = new HashMap<>();
        mercedesModels.put("C-Class", Arrays.asList("W202", "W203", "W204", "W205", "W206"));
        mercedesModels.put("E-Class", Arrays.asList("W124", "W210", "W211", "W212", "W213"));
        mercedesModels.put("S-Class", Arrays.asList("W140", "W220", "W221", "W222", "W223"));
        carData.put("Mercedes", mercedesModels);

        // Audi
        Map<String, List<String>> audiModels = new HashMap<>();
        audiModels.put("A4", Arrays.asList("B5", "B6", "B7", "B8", "B9"));
        audiModels.put("A6", Arrays.asList("C4", "C5", "C6", "C7", "C8"));
        audiModels.put("Q5", Arrays.asList("8R", "FY"));
        carData.put("Audi", audiModels);

        // Toyota
        Map<String, List<String>> toyotaModels = new HashMap<>();
        toyotaModels.put("Corolla", Arrays.asList("E90", "E100", "E110", "E120", "E140", "E170", "E210"));
        toyotaModels.put("Camry", Arrays.asList("XV10", "XV20", "XV30", "XV40", "XV50", "XV70"));
        toyotaModels.put("RAV4", Arrays.asList("XA10", "XA20", "XA30", "XA40", "XA50"));
        carData.put("Toyota", toyotaModels);

        // Honda
        Map<String, List<String>> hondaModels = new HashMap<>();
        hondaModels.put("Civic", Arrays.asList("EG", "EK", "EP", "FD", "FK", "FL"));
        hondaModels.put("Accord", Arrays.asList("CB", "CD", "CE", "CF", "CL", "CM", "CR"));
        hondaModels.put("CR-V", Arrays.asList("RD1", "RD4", "RE", "RM", "RW"));
        carData.put("Honda", hondaModels);

        // Subaru
        Map<String, List<String>> subaruModels = new HashMap<>();
        subaruModels.put("Forester", Arrays.asList("SF", "SG", "SH", "SJ", "SK"));
        subaruModels.put("Impreza", Arrays.asList("GC", "GD", "GE", "GJ", "GT"));
        subaruModels.put("Outback", Arrays.asList("BG", "BH", "BP", "BR", "BS"));
        carData.put("Subaru", subaruModels);

        // Nissan
        Map<String, List<String>> nissanModels = new HashMap<>();
        nissanModels.put("Altima", Arrays.asList("L30", "L31", "L32", "L33", "L34"));
        nissanModels.put("X-Trail", Arrays.asList("T30", "T31", "T32"));
        nissanModels.put("Qashqai", Arrays.asList("J10", "J11", "J12"));
        carData.put("Nissan", nissanModels);

        // Mazda
        Map<String, List<String>> mazdaModels = new HashMap<>();
        mazdaModels.put("3", Arrays.asList("BK", "BL", "BM", "BP"));
        mazdaModels.put("6", Arrays.asList("GG", "GH", "GJ", "GL"));
        mazdaModels.put("CX-5", Arrays.asList("KE", "KF", "KG"));
        carData.put("Mazda", mazdaModels);

        // Colors
        String[] colors = {"Black", "White", "Silver", "Gray", "Red", "Blue", "Green", "Yellow",
                "Brown", "Orange", "Purple", "Beige", "Gold", "Navy", "Burgundy"};

        // VIN characters (excluding I, O, Q to avoid confusion)
        String vinChars = "ABCDEFGHJKLMNPRSTUVWXYZ0123456789";

        // Current year for realistic year ranges
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        // Engine capacities
        String[] engineCapacities = {"1.0", "1.2", "1.4", "1.6", "1.8", "2.0", "2.2", "2.5",
                "3.0", "3.5", "4.0", "4.5", "5.0", "5.5", "6.0"};

        // Engine powers
        String[] enginePowers = {"90", "110", "130", "150", "170", "190", "210", "230",
                "250", "280", "310", "340", "370", "400", "450", "500"};

        // Get all brands
        List<String> brands = new ArrayList<>(carData.keySet());

        // First, create all car models and save them to the database
        Map<String, UUID> modelIdMap = new HashMap<>();
        log.info("First ensuring all car models exist in the database...");

        for (String brand : brands) {
            Map<String, List<String>> models = carData.get(brand);
            for (String modelName : models.keySet()) {
                for (String generation : models.get(modelName)) {
                    String modelKey = brand + ":" + modelName + ":" + generation;

                    CarModelDto carModelDto = new CarModelDto();
                    carModelDto.setBrand(brand);
                    carModelDto.setModel(modelName);
                    carModelDto.setGeneration(generation);

                    try {
                        // Try to save the car model (might already exist)
                        UUID modelId = carModelService.saveCarModel(carModelDto);
                        modelIdMap.put(modelKey, modelId);
                        log.info("Created or found car model: {} {} {}, ID: {}", brand, modelName, generation, modelId);
                    } catch (Exception e) {
                        // If model already exists, we can ignore this exception
                        log.info("Car model already exists: {} {} {}", brand, modelName, generation);
                    }
                }
            }
        }

        // Now generate the cars using the saved models
        log.info("Generating {} cars using the existing models...", count);

        // Generate the specified number of cars
        for (int i = 0; i < count; i++) {
            try {
                CarCreationDto carCreationDto = new CarCreationDto();

                // Select random brand, model, generation
                String brand = brands.get(random.nextInt(brands.size()));
                Map<String, List<String>> models = carData.get(brand);

                List<String> modelNames = new ArrayList<>(models.keySet());
                String modelName = modelNames.get(random.nextInt(modelNames.size()));

                List<String> generations = models.get(modelName);
                String generation = generations.get(random.nextInt(generations.size()));

                // Create car model DTO with ID from the ones we saved
                String modelKey = brand + ":" + modelName + ":" + generation;
                CarModelDto carModelDto = new CarModelDto();
                carModelDto.setBrand(brand);
                carModelDto.setModel(modelName);
                carModelDto.setGeneration(generation);

                if (modelIdMap.containsKey(modelKey)) {
                    carModelDto.setCarModelId(modelIdMap.get(modelKey));
                } else {
                    // If we somehow don't have the model ID, try to save it now
                    try {
                        UUID modelId = carModelService.saveCarModel(carModelDto);
                        modelIdMap.put(modelKey, modelId);
                        carModelDto.setCarModelId(modelId);
                    } catch (Exception e) {
                        // Model might already exist, but we don't have the ID
                        log.error("Could not get ID for car model: {} {} {}", brand, modelName, generation);
                        continue; // Skip this car and move to the next one
                    }
                }

                // Set the car model in the car creation DTO
                carCreationDto.setCarModelDto(carModelDto);

                // Set year (between 1990 and current year)
                int year = 1990 + random.nextInt(currentYear - 1989);
                carCreationDto.setYear(year);

                // Set color
                String color = colors[random.nextInt(colors.length)];
                carCreationDto.setColor(color);

                // Set mileage (based on car age)
                int age = currentYear - year;
                int avgAnnualMileage = 10000 + random.nextInt(5000); // Between 10k and 15k miles per year
                BigDecimal mileage = new BigDecimal(age * avgAnnualMileage * (0.8 + random.nextDouble() * 0.4)); // +/- 20%
                carCreationDto.setMileage(mileage.setScale(0, BigDecimal.ROUND_DOWN));

                // Set owners count (1-4, weighted toward fewer owners)
                int[] ownerWeights = {60, 25, 10, 5}; // 60% chance of 1 owner, 25% chance of 2, etc.
                int ownersCount = weightedRandom(random, ownerWeights) + 1;
                carCreationDto.setOwnersCount(ownersCount);

                // Set transmission type
                TransmissionType[] transmissionTypes = TransmissionType.values();
                carCreationDto.setTransmissionType(transmissionTypes[random.nextInt(transmissionTypes.length)]);

                // Set body type
                BodyType[] bodyTypes = BodyType.values();
                carCreationDto.setBodyType(bodyTypes[random.nextInt(bodyTypes.length)]);

                // Set engine power
                String enginePower = enginePowers[random.nextInt(enginePowers.length)];
                carCreationDto.setEnginePower(enginePower);

                // Set engine type
                EngineType[] engineTypes = EngineType.values();
                carCreationDto.setEngineType(engineTypes[random.nextInt(engineTypes.length)]);

                // Set drive type
                DriveType[] driveTypes = DriveType.values();
                carCreationDto.setDriveType(driveTypes[random.nextInt(driveTypes.length)]);

                // Set engine capacity
                String engineCapacity = engineCapacities[random.nextInt(engineCapacities.length)];
                carCreationDto.setEngineCapacity(engineCapacity);

                // Set steering position
                SteeringPosition[] steeringPositions = SteeringPosition.values();
                carCreationDto.setSteeringPosition(steeringPositions[random.nextInt(steeringPositions.length)]);

                // Set seats count (2-9)
                int seatsCount = 2 + random.nextInt(8);
                carCreationDto.setSeatsCount(seatsCount);

                // Set price (based on multiple factors)
                BigDecimal basePrice = new BigDecimal(5000 + random.nextInt(50000));
                // Adjust price based on year
                double yearFactor = 0.6 + ((double)(year - 1990) / (currentYear - 1990)) * 0.8;
                // Adjust price based on mileage
                double mileageFactor = 1.0 - (mileage.doubleValue() / 300000.0);
                if (mileageFactor < 0.3) mileageFactor = 0.3;

                BigDecimal price = basePrice
                        .multiply(BigDecimal.valueOf(yearFactor))
                        .multiply(BigDecimal.valueOf(mileageFactor))
                        .setScale(0, BigDecimal.ROUND_DOWN);
                carCreationDto.setPrice(price);

                // Generate VIN (17 characters)
                StringBuilder vin = new StringBuilder();
                for (int j = 0; j < 17; j++) {
                    vin.append(vinChars.charAt(random.nextInt(vinChars.length())));
                }
                carCreationDto.setVin(vin.toString());

                // Generate description
                String[] conditionWords = {"Excellent", "Good", "Fair", "Like new", "Well-maintained"};
                String[] featureWords = {"leather seats", "sunroof", "navigation system", "backup camera",
                        "heated seats", "Bluetooth", "cruise control", "alloy wheels",
                        "keyless entry", "premium sound system"};

                StringBuilder description = new StringBuilder();
                description.append(conditionWords[random.nextInt(conditionWords.length)]);
                description.append(" condition ");
                description.append(year).append(" ");
                description.append(brand).append(" ");
                description.append(modelName).append(". ");

                // Add 2-4 random features
                int featureCount = 2 + random.nextInt(3);
                List<Integer> selectedFeatures = new ArrayList<>();
                for (int j = 0; j < featureCount; j++) {
                    int featureIndex;
                    do {
                        featureIndex = random.nextInt(featureWords.length);
                    } while (selectedFeatures.contains(featureIndex));

                    selectedFeatures.add(featureIndex);

                    if (j > 0) {
                        if (j == featureCount - 1) {
                            description.append(" and ");
                        } else {
                            description.append(", ");
                        }
                    }
                    description.append(featureWords[featureIndex]);
                }
                description.append(".");
                carCreationDto.setDescription(description.toString());

                // Save the car
                UUID id = carService.saveCar(carCreationDto);
                generatedIds.add(id);

                // Add to the result list
                Map<String, Object> carDetails = new HashMap<>();
                carDetails.put("id", id.toString());
                carDetails.put("brand", brand);
                carDetails.put("model", modelName);
                carDetails.put("year", year);
                carDetails.put("color", color);
                carDetails.put("price", price);
                generatedCars.add(carDetails);

                log.info("Created car: {} {} {}, ID: {}", brand, modelName, year, id);
            } catch (Exception e) {
                log.error("Failed to create car: {}", e.getMessage(), e);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", String.format("Generated %d cars", generatedIds.size()));
        response.put("generatedCount", generatedIds.size());
        response.put("cars", generatedCars);

        log.info("Car generation completed. Created {} cars.", generatedIds.size());

        return ResponseEntity.ok(response);
    }

    /**
     * Helper method to get weighted random index
     */
    private int weightedRandom(Random random, int[] weights) {
        int totalWeight = Arrays.stream(weights).sum();
        int randomValue = random.nextInt(totalWeight);

        int cumulativeWeight = 0;
        for (int i = 0; i < weights.length; i++) {
            cumulativeWeight += weights[i];
            if (randomValue < cumulativeWeight) {
                return i;
            }
        }

        return weights.length - 1; // Fallback
    }
}