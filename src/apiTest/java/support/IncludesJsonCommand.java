package support;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.concordion.api.*;
import org.concordion.api.listener.AssertEqualsListener;
import org.concordion.api.listener.AssertFailureEvent;
import org.concordion.api.listener.AssertSuccessEvent;
import org.concordion.internal.util.Announcer;

import java.util.Map.Entry;

public class IncludesJsonCommand extends AbstractCommand {

    private Announcer<AssertEqualsListener> listeners = Announcer.to(AssertEqualsListener.class);

    public void addAssertEqualsListener(AssertEqualsListener listener) {
        listeners.addListener(listener);
    }

    public void removeAssertEqualsListener(AssertEqualsListener listener) {
        listeners.removeListener(listener);
    }

    public void verify(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {

        Element element = commandCall.getElement();

        String actual = (String) evaluator.evaluate(commandCall.getExpression());
        String expected = element.getText();

        if (assertIncludesJson(actual, expected)) {
            resultRecorder.record(Result.SUCCESS);
            announceSuccess(element);
        } else {
            resultRecorder.record(Result.FAILURE);
            announceFailure(element, expected.toString(), actual);
        }

    }

    private boolean assertIncludesJson(String actual, String expected) {

        JsonElement actualJson = new JsonParser().parse(actual);
        JsonElement expectedJson = new JsonParser().parse(expected);

        return includesJson(actualJson, expectedJson);
    }

    private boolean includesJson(JsonElement actualJson, JsonElement expectedJson) {

        if (expectedJson.isJsonObject()) {

            if (!actualJson.isJsonObject()) {
                return false;
            }

            for (Entry<String, JsonElement> entry : expectedJson.getAsJsonObject().entrySet()) {

                String property = entry.getKey();
                JsonElement expectedValue = entry.getValue();

                if (!actualJson.getAsJsonObject().has(property)) {
                    return false;
                }

                JsonElement actualValue = actualJson.getAsJsonObject().get(property);
                if (!includesJson(actualValue, expectedValue)) {
                    return false;
                }
            }

            return true;
        }

        if (expectedJson.isJsonArray()) {

            if (!actualJson.isJsonArray()) {
                return false;
            }

            outer: for (JsonElement expectedItem : expectedJson.getAsJsonArray()) {
                for (JsonElement candidateItem : actualJson.getAsJsonArray()) {
                    if (includesJson(candidateItem, expectedItem)) {
                        continue outer;
                    }
                }
                return false;
            }
            return true;
        }

        return actualJson.equals(expectedJson);

    }

    private void announceSuccess(Element element) {
        listeners.announce().successReported(new AssertSuccessEvent(element));
    }

    private void announceFailure(Element element, String expected, Object actual) {
        listeners.announce().failureReported(new AssertFailureEvent(element, expected, actual));
    }

}
