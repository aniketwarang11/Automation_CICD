package Utility;

import java.util.Map;

import context.ScenarioContext;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestStepFinished;

public class ITestStepFinished implements ConcurrentEventListener {

	public static class StepCounters {
		public int executedStep = 0;
		public int total = 0;
		public int passed = 0;
		public int failedHard = 0;
		public int failedSoft = 0;
		public boolean hasHardFailed = false;

		public int getTotalFailed() {
			return failedHard + failedSoft;
		}
	}

	public enum FailureType {
		NONE, HARD, SOFT
	}

	private static final Map<String, StepCounters> summaryMap = new java.util.concurrent.ConcurrentHashMap<>();

	public EventHandler<TestCaseStarted> handlerTestCaseStarted = this::handleTestCaseStarted;

	public static void updateStep(String scenarioName, FailureType failureType) {
		StepCounters counters = summaryMap.computeIfAbsent(scenarioName, k -> new StepCounters());

		synchronized (counters) {
			// Don't count skipped steps after a hard failure
			if (counters.hasHardFailed) {
				return;
			}

			counters.executedStep++;

			switch (failureType) {
			case NONE -> counters.passed++;
			case SOFT -> counters.failedSoft++;
			case HARD -> {
				counters.failedHard++;
				counters.hasHardFailed = true; // Prevent further executedStep count
			}
			}
		}
	}

	public static Map<String, StepCounters> getAllSummaries() {
		return summaryMap;
	}

	private void handleTestCaseStarted(TestCaseStarted event) {
		String scenarioName = event.getTestCase().getName();
		int total = (int) event.getTestCase().getTestSteps().stream().filter(s -> s instanceof PickleStepTestStep)
				.count();

		StepCounters counters = summaryMap.computeIfAbsent(scenarioName, k -> new StepCounters());
		counters.total = total; // Total steps set upfront
	}

	// Thread-local variable to store step names per thread
	private static final ThreadLocal<String> currentStep = new ThreadLocal<>();

	// Event handler for TestStepFinished event
	public EventHandler<TestStepFinished> handler = this::logTestStepName;

	public EventHandler<TestStepFinished> handlerForSummery = this::handleTestStepFinished;

	@Override
	public void setEventPublisher(EventPublisher eventPublisher) {
		eventPublisher.registerHandlerFor(TestCaseStarted.class, handlerTestCaseStarted);
		eventPublisher.registerHandlerFor(TestStepFinished.class, handler);
		eventPublisher.registerHandlerFor(TestStepFinished.class, handlerForSummery);
		eventPublisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinished);

	}

	private void logTestStepName(TestStepFinished testStepFinished) {
		if (testStepFinished.getTestStep() instanceof PickleStepTestStep) {
			PickleStepTestStep testStep = (PickleStepTestStep) testStepFinished.getTestStep();
			currentStep.set(testStep.getStep().getText()); // Store step name in thread-local
		}
	}

	// Retrieves the current step name for the calling thread
	public static String getStepName() {
		return currentStep.get();
	}

	// Cleanup method to avoid memory leaks
	public static void clear() {
		currentStep.remove();
	}

	private void handleTestStepFinished(TestStepFinished event) {
		if (event.getTestStep() instanceof PickleStepTestStep) {
			PickleStepTestStep step = (PickleStepTestStep) event.getTestStep();
			String scenarioName = Setup.getContext().currentScenarioName;
			String stepName = step.getStep().getText();

			int lineNumber = step.getStep().getLocation().getLine();
			System.out.println("Step: \"" + stepName + "\" at line " + lineNumber);

			FailureType failureType = FailureType.NONE;

			// Check for hard failure first
			if (event.getResult().getStatus() == Status.FAILED) {
				failureType = FailureType.HARD;
			} else {
				try {
					Setup.getContext().getSoftAssertions().assertAll();
				} catch (Throwable e) {

					ScenarioContext.addStepError(scenarioName, Integer.toString(lineNumber), e.toString());

					failureType = FailureType.SOFT;
				} finally {
					Setup.getContext().resetSoftAssertions();

				}
			}

			updateStep(scenarioName, failureType);
		}
	}

	private void handleTestRunFinished(TestRunFinished event) {
		getAllSummaries().forEach((scenario, counters) -> {
			int skipped = counters.total - counters.executedStep;
			System.out.printf("Scenario: %s\nTotal : %d\n(Passed: %d, Failed: %d, Skipped: %d)%n%n", scenario,
					counters.total, counters.passed, (counters.failedHard + counters.failedSoft), skipped);
		});
	}

}
