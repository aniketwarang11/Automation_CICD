package Utility;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class PostProcessor {

    public static void main(String[] args) throws Exception {
    	
    	 System.out.println("=== Running PostProcessor ===");
        String jsonPath = "target/cucumber-reports/Cucumber.json";
        String stepErrorsPath = "target/stepErrors.json";

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Load step errors
        Type type = new TypeToken<Map<String, Map<String, String>>>() {}.getType();
        Map<String, Map<String, String>> stepErrors =
                gson.fromJson(new FileReader(stepErrorsPath), type);

        // Load Cucumber JSON
//        JsonArray jsonArray = JsonParser.parseReader(new FileReader(jsonPath)).getAsJsonArray();
        
        
        
        
        JsonElement rootElement = JsonParser.parseReader(new FileReader(jsonPath));

        if (rootElement == null || !rootElement.isJsonArray()) {
            throw new IllegalStateException("Cucumber JSON is empty or not in expected array format.");
        }

        JsonArray jsonArray = rootElement.getAsJsonArray();

        
        
        

        boolean anyStepFailed = false;

        for (JsonElement featureElement : jsonArray) {
            JsonObject feature = featureElement.getAsJsonObject();
            JsonArray elements = feature.getAsJsonArray("elements");

            for (JsonElement scenarioElement : elements) {
                JsonObject scenario = scenarioElement.getAsJsonObject();
                String scenarioName = scenario.get("name").getAsString();

                JsonArray steps = scenario.getAsJsonArray("steps");
                JsonArray filteredSteps = new JsonArray();
                boolean scenarioFailed = false;

                for (JsonElement stepElement : steps) {
                    JsonObject step = stepElement.getAsJsonObject();
                    String stepName = step.get("name").getAsString();
                    int lineNumber = step.get("line").getAsInt();

                    step.remove("before");
                    step.remove("after");

                    // Match based on scenario and line number
                    if (stepErrors.containsKey(scenarioName)) {
                        Map<String, String> lineErrors = stepErrors.get(scenarioName);
                        String lineKey = String.valueOf(lineNumber);
                        if (lineErrors.containsKey(lineKey)) {
                            String errorMsg = lineErrors.get(lineKey);
                            JsonObject result = step.getAsJsonObject("result");
                            result.addProperty("status", "failed");
                            result.addProperty("error_message", errorMsg);

                            scenarioFailed = true;
                            anyStepFailed = true;
                        }
                    }

                    filteredSteps.add(step);
                }

                // Replace filtered steps
                scenario.add("steps", filteredSteps);

                if (scenarioFailed) {
                    scenario.addProperty("status", "failed");
                }
            }
        }

        // Save updated JSON
        try (FileWriter writer = new FileWriter(jsonPath)) {
            gson.toJson(jsonArray, writer);
        }

        System.out.println("Cucumber JSON patched: removed hooks + applied soft failures.");

        if (anyStepFailed) {
            throw new RuntimeException("Failing build due to failed Cucumber steps.");
        }
    }
}
