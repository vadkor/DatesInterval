package rms.dates.interval.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.ModelResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

public class GlobalErrorsMatcher extends ModelResultMatchers {

	public static ResultMatcher hasGlobalError(String model, String expectedCode) {
		return result -> {
			BindingResult bindingResult = getBindingResult(result.getModelAndView(), model);
			bindingResult.getGlobalErrors().stream().filter(oe -> model.equals(oe.getObjectName()))
					.forEach(oe -> assertEquals("Expected code", expectedCode, oe.getCode()));
		};
	}

	private static BindingResult getBindingResult(ModelAndView mav, String name) {
		BindingResult result = (BindingResult) mav.getModel().get(BindingResult.MODEL_KEY_PREFIX + name);
		assertTrue("No BindingResult for model: " + name, result != null);
		assertTrue("No global errors for model: " + name, result.getGlobalErrorCount() > 0);
		return result;
	}
}
