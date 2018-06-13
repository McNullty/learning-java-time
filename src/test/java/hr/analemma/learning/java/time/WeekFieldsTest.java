package hr.analemma.learning.java.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

import org.junit.Test;

public class WeekFieldsTest {

  @Test
  public void testFirestWeekInAYear2017() {
    final LocalDate firstDayOfAYear = LocalDate.of(2017, 1, 1);

    final WeekFields weekOfTheYear = WeekFields.ISO;
    assertThat(firstDayOfAYear.getLong(weekOfTheYear.weekOfYear())).isEqualTo(0);
  }

  /**
   * week was checked on http://hr.weeknumber52.com/
   */
  @Test
  public void testWeek24() {
    final LocalDate firstDayOfAYear = LocalDate.of(2018, 6, 13);

    final WeekFields weekOfTheYear = WeekFields.ISO;
    assertThat(firstDayOfAYear.getLong(weekOfTheYear.weekOfYear())).isEqualTo(24);
  }
}
