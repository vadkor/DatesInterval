package rms.dates.interval.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static rms.dates.interval.matchers.GlobalErrorsMatcher.hasGlobalError;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = DatesIntervalController.class, includeFilters = @Filter(classes = Service.class, type = FilterType.ANNOTATION))
public class DatesIntervalControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testRedirFromRootToDatesPage() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/dates"));
	}

	@Test
	public void testDatesPageUriReturnsDatesPage() throws Exception {
		mockMvc.perform(get("/dates")).andExpect(view().name("dates"));
	}

	@Test
	public void testIntervalSuccess() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/interval").accept(MediaType.TEXT_HTML)
				.param("startDate", "1/01/2019")
				.param("endDate", "31/01/2019")
		)
		.andExpect(status().isOk())
		.andExpect(view().name("interval"))
		.andExpect(model().hasNoErrors())
		.andExpect(model().attribute("interval", equalTo(30L)))
		.andExpect(content().string(containsString("Interval in days: 30")));
	}

	@Test
	public void testErrorWhenNoStartDate() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/interval").accept(MediaType.TEXT_HTML)
				.param("endDate", "1/01/2019")
		)
		.andExpect(model().attributeHasFieldErrorCode("dates", "startDate", "NotNull"))
		.andExpect(model().errorCount(1))
		.andExpect(view().name("dates"))
		.andExpect(status().isOk());
	}

	@Test
	public void testErrorWhenNoEndDate() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/interval").accept(MediaType.TEXT_HTML)
				.param("startDate", "1/01/2019")
		)
		.andExpect(model().attributeHasFieldErrorCode("dates", "endDate", "NotNull"))
		.andExpect(model().errorCount(1))
		.andExpect(view().name("dates"))
		.andExpect(status().isOk());
	}

	@Test
	public void testErrorWhenNoDates() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/interval").accept(MediaType.TEXT_HTML))
		.andExpect(model().attributeHasFieldErrorCode("dates", "startDate", "NotNull"))
		.andExpect(model().attributeHasFieldErrorCode("dates", "endDate", "NotNull"))
		.andExpect(model().errorCount(2))
		.andExpect(view().name("dates"))
		.andExpect(status().isOk());
	}

	@Test
	public void testErrorWhenEndDateBeforeStartDate() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/interval").accept(MediaType.TEXT_HTML)
				.param("startDate", "1/02/2019")
				.param("endDate", "1/01/2019")
		)
		.andExpect(model().errorCount(1))
		.andExpect(hasGlobalError("dates", "DateOrderCheck"))
		.andExpect(view().name("dates"))
		.andExpect(status().isOk());
	}
}
