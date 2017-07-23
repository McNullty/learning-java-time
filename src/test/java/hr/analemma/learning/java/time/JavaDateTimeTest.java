package hr.analemma.learning.java.time;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

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
	}
}
