package rms.dates.interval.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;

public class DatesServiceTest {

	@Test
	public void testCalculateDateInterval_SameDays() {
		DatesService service = new DatesService();
		Calendar startDate = Calendar.getInstance();
		startDate.set(2019, 01, 01);
		Calendar endDate = Calendar.getInstance();
		endDate.set(2019, 01, 01);
		assertEquals(0, service.calculateDateInterval(startDate.getTime(), endDate.getTime()));
	}

	@Test
	public void testCalculateDateInterval_SequentialDays() {
		DatesService service = new DatesService();
		Calendar startDate = Calendar.getInstance();
		startDate.set(2019, 01, 01);
		Calendar endDate = Calendar.getInstance();
		endDate.set(2019, 01, 02);
		assertEquals(1, service.calculateDateInterval(startDate.getTime(), endDate.getTime()));
	}

	@Test
	public void testCalculateDateInterval_NonLeapYear() {
		DatesService service = new DatesService();
		Calendar startDate = Calendar.getInstance();
		startDate.set(2019, 01, 01);
		Calendar endDate = Calendar.getInstance();
		endDate.set(2020, 01, 01);
		assertEquals(365, service.calculateDateInterval(startDate.getTime(), endDate.getTime()));
	}

	@Test
	public void testCalculateDateInterval_LeapYear() {
		DatesService service = new DatesService();
		Calendar startDate = Calendar.getInstance();
		startDate.set(2020, 01, 01);
		Calendar endDate = Calendar.getInstance();
		endDate.set(2021, 01, 01);
		assertEquals(366, service.calculateDateInterval(startDate.getTime(), endDate.getTime()));
	}

	@Test
	public void testCalculateDateInterval_EndDayBeforeStart() {
		DatesService service = new DatesService();
		Calendar startDate = Calendar.getInstance();
		startDate.set(2025, 01, 01);
		Calendar endDate = Calendar.getInstance();
		endDate.set(2021, 01, 01);
		assertTrue("End before start", service.calculateDateInterval(startDate.getTime(), endDate.getTime()) < 0);
	}
}
