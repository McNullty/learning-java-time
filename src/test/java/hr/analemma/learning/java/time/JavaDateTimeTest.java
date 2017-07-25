package hr.analemma.learning.java.time;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

public class JavaDateTimeTest {

	@Test
	public void testDate() {
		LocalDate now = LocalDate.now();
		assertThat(now).isNotNull();

		LocalDate customDate = LocalDate.of(2017, Month.FEBRUARY, 17);
		assertThat(customDate.toString()).isEqualTo("2017-02-17");

		assertThat(customDate.getMonth()).isEqualTo(Month.FEBRUARY);
		assertThat(customDate.getMonthValue()).isEqualTo(2);

		assertThatExceptionOfType(DateTimeException.class).isThrownBy(() -> {
			LocalDate.of(2017, Month.FEBRUARY, 31);
		});
	}

	@Test
	public void testDateMath() {
		LocalDate customDate = LocalDate.of(2016, Month.FEBRUARY, 29);
		assertThat(customDate).isNotNull();

		// IMPORTANT: it treats 29.2. in leap year as end of month so when
		// adding a year it will put end of February next year
		assertThat(customDate.plus(1, ChronoUnit.YEARS).toString()).isEqualTo("2017-02-28");
		assertThat(customDate.plus(366, ChronoUnit.DAYS).toString()).isEqualTo("2017-03-01");

		LocalDate lastDateInJanuary = LocalDate.of(2016, Month.JANUARY, 31);
		assertThat(lastDateInJanuary.plusMonths(1)).isEqualTo("2016-02-29");
		assertThat(lastDateInJanuary.plusYears(1).plusMonths(1)).isEqualTo("2017-02-28");
	}

	@Test
	public void testTimeMath() {
		LocalDateTime customDateTime = LocalDateTime.of(2017, 07, 25, 21, 39);

		assertThat(customDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).isEqualTo("2017-07-25T21:39:00");

		assertThat(customDateTime.plusHours(24).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
				.isEqualTo("2017-07-26T21:39:00");

		LocalDateTime dstStart = LocalDateTime.of(2017, Month.MARCH, 25, 12, 00);
		assertThat(dstStart.plusHours(24).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
				.isEqualTo("2017-03-26T12:00:00");

		ZonedDateTime zdtStart = ZonedDateTime.of(dstStart, ZoneId.of("Europe/Paris"));
		assertThat(zdtStart.plusHours(24).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
				.isEqualTo("2017-03-26T13:00:00");

		ZonedDateTime zdtEnd = ZonedDateTime.of(2017, 10, 28, 12, 0, 0, 0, ZoneId.of("Europe/Paris"));
		assertThat(zdtEnd.plusHours(24).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).isEqualTo("2017-10-29T11:00:00");
	}

	@Test
	public void testWithTemporalAdjusters() {
		LocalDate startDate = LocalDate.of(2017, 07, 25);

		LocalDate endDate = startDate.plusWeeks(2);
		assertThat(endDate).isEqualTo("2017-08-08");

		// Two weeks grom given date and then get date of next Friday
		LocalDate endDateWithNextFriday = startDate.plusWeeks(2).with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
		assertThat(endDateWithNextFriday).isEqualTo("2017-08-11");
	}

	@Test
	public void testConvertingToDate() {
		LocalDate customDate = LocalDate.of(2017, Month.JUNE, 20);

		Date date = Date.from(customDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		Date gregorianDate = new GregorianCalendar(2017, 5, 20).getTime();

		assertThat(date).isEqualTo(gregorianDate);
	}
}
