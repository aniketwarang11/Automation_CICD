package context;

import org.assertj.core.api.SoftAssertions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ScenarioContext {

	public String currentScenarioName;

	private static Map<String, Map<String, String>> stepError = new ConcurrentHashMap<>();

	private static final ThreadLocal<SoftAssertions> softAssertions = ThreadLocal.withInitial(SoftAssertions::new);

	public SoftAssertions getSoftAssertions() {
		return softAssertions.get();
	}

	public static void resetSoftAssertions() {
		softAssertions.set(new SoftAssertions()); // Replace the current thread's instance
	}

	public void reset() {
		softAssertions.remove(); // Clean up after scenario
	}

	public static Map<String, Map<String, String>> getStepErrors() {
		return stepError;
	}

	// Serialize the stepError map to a file
	public static void saveStepErrorsToFile() throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (FileWriter writer = new FileWriter("target/stepErrors.json")) {
			gson.toJson(stepError, writer); // Write the stepError map as a formatted JSON
		}
		System.out.println("Step errors saved to target/stepErrors.json");
	}

	public static void addStepError(String scenarioName, String lineNo, String errorMessage) {
		stepError.computeIfAbsent(scenarioName, k -> new HashMap<>()).put(lineNo, errorMessage);
//        System.out.println(stepError);
	}

	public static String getStepError(String scenarioName, String stepName) {
		return stepError.getOrDefault(scenarioName, new HashMap<>()).get(stepName);
	}

	public static Map<String, Map<String, String>> getAllStepErrors() {
		return stepError;
	}

	public static void clear() {
		stepError.clear();
	}

}
