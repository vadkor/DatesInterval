package rms.dates.interval.validation;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import rms.dates.interval.service.DatesService;

public class DateOrderValidator implements ConstraintValidator<DateOrderCheck, Object> {

	@Autowired
	private DatesService datesService;

	private String startDate;
	private String endDate;

	@Override
	public void initialize(DateOrderCheck constraint) {
		this.startDate = constraint.startDateField();
		this.endDate = constraint.endDateField();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {

		Date startDate = (Date) new BeanWrapperImpl(value).getPropertyValue(this.startDate);
		Date endDate = (Date) new BeanWrapperImpl(value).getPropertyValue(this.endDate);
		if (startDate == null || endDate == null) {
			return true; // Let the other validator handle this
		}
		long interval = datesService.calculateDateInterval(startDate, endDate);
		return (interval >= 0);
	}
}
