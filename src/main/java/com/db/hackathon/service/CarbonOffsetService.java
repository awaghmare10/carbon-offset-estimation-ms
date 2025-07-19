package com.db.hackathon.service;


import com.db.hackathon.configuration.CarbonFactorConfig;
import com.db.hackathon.model.CarbonOffsetRequest;
import com.db.hackathon.model.CarbonOffsetResponse;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.*;
import org.jpmml.model.PMMLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.*;

import static org.jpmml.evaluator.ModelEvaluatorFactory.*;

@Service
public class CarbonOffsetService {

    @Autowired
    private CarbonFactorConfig config;

    private static final DecimalFormat fourDecimal = new DecimalFormat("#.####");

    public CarbonOffsetResponse estimateOffset(CarbonOffsetRequest req) {
        double fertilizerEmission = req.fertilizerNkgPerHa * req.areaHa * config.getFertilizer() * config.getGwpN2o() / 1000;

        if (req.manureApplied) {
            fertilizerEmission += config.getManureEmissionAddition();
        }

        double fuelEmission = req.dieselLiters * config.getFuel() / 1000;

        Set<String> floodTypes = Set.of("flood", "furrow", "basin");
        boolean isFlood = req.irrigationType != null && floodTypes.contains(req.irrigationType.toLowerCase());
        double irrigationEmission = (isFlood ? req.areaHa * config.getIrrigationFlood() : req.areaHa * config.getIrrigationOther()) / 1000;

        double totalEmissions = fertilizerEmission + fuelEmission + irrigationEmission;

        if (req.noTill) {
            totalEmissions *= config.getNoTillReductionFactor();
        }

        double treeSequestration = req.treesPlanted * config.getTrees();
        double biocharSequestration = req.biocharTons * config.getBiochar();

        double leakage = 0.1 * totalEmissions;
        double uncertainty = 0.2 * totalEmissions;

        double mlPredictionOffset = predictWithPMML(req);

        double sequestrationTotal = treeSequestration + biocharSequestration + mlPredictionOffset;
        double totalOffset = sequestrationTotal - (totalEmissions + leakage + uncertainty);
        double creditValue = totalOffset * config.getPricePerTonne();

        CarbonOffsetResponse res = new CarbonOffsetResponse();
        res.estimatedOffsetTCO2e = Math.round(totalOffset * 100.0) / 100.0;
        res.estimatedCreditValue = Math.round(creditValue * 100.0) / 100.0;
        res.currency = config.getCurrency();

        Map<String, Double> emissions = new HashMap<>();
        emissions.put("fertilizer", Math.round(fertilizerEmission * 10000.0) / 10000.0);
        emissions.put("fuel", Math.round(fuelEmission * 10000.0) / 10000.0);
        emissions.put("irrigation", Math.round(irrigationEmission * 10000.0) / 10000.0);

        Map<String, Double> sequestration = new HashMap<>();
        sequestration.put("trees", Math.round(treeSequestration * 10000.0) / 10000.0);
        sequestration.put("biochar", Math.round(biocharSequestration * 10000.0) / 10000.0);
        sequestration.put("mlPrediction", Math.round(mlPredictionOffset * 10000.0) / 10000.0);

        res.emissions = emissions;
        res.sequestration = sequestration;
        res.leakage = Math.round(leakage * 10000.0) / 10000.0;
        res.uncertaintyBuffer = Math.round(uncertainty * 10000.0) / 10000.0;

        return res;
    }



    private double predictWithPMML(CarbonOffsetRequest req) {
        try (FileInputStream fis = new FileInputStream("src/main/resources/carbon_model.pmml")) {
            PMML pmml = PMMLUtil.unmarshal(fis);
            ModelEvaluator<?> evaluator = new ModelEvaluatorBuilder(pmml).build();
            evaluator.verify();

            Map<FieldName, FieldValue> features = new LinkedHashMap<>();
            for (InputField inputField : evaluator.getInputFields()) {
                String fieldName = inputField.getName().getValue();
                Object rawValue = switch (fieldName) {
                    case "area_ha" -> req.areaHa;
                    case "fertilizer_n_kg_per_ha" -> req.fertilizerNkgPerHa;
                    case "diesel_liters" -> req.dieselLiters;
                    case "latitude" -> req.location != null ? req.location.lat : null;
                    case "longitude" -> req.location != null ? req.location.lon : null;
                    default -> null;
                };
                features.put(inputField.getName(), inputField.prepare(rawValue));
            }

            Map<FieldName, ?> result = evaluator.evaluate(features);
            Object predicted = result.get(evaluator.getTargetFields().get(0).getName());

            if (predicted instanceof Computable computable) {
                Object resultVal = computable.getResult();
                return Double.parseDouble(resultVal.toString());
            } else {
                return Double.parseDouble(predicted.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}
