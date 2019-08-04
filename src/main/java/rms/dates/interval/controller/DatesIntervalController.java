package rms.dates.interval.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import rms.dates.interval.model.Dates;
import rms.dates.interval.service.DatesService;

@Controller
public class DatesIntervalController {
	private static final String VIEW_DATES = "dates";
	private static final String VIEW_INTERVAL = "interval";

	@Autowired
	private DatesService datesService;

	@GetMapping("/")
	public String redirectToDates(Dates dates) {
		return "redirect:/" + VIEW_DATES;
	}

	@GetMapping("/dates")
	public String showDatesForm(Dates dates) {
		return VIEW_DATES;
	}

	@PostMapping("/interval")
	public String calculateInterval(@Valid Dates dates, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return VIEW_DATES;
		}
		long interval = datesService.calculateDateInterval(dates.getStartDate(), dates.getEndDate());
		model.addAttribute("interval", interval);
		return VIEW_INTERVAL;
	}
}
