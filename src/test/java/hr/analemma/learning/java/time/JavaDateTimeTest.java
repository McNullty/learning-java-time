package hr.analemma.learning.java.time;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalQuery;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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
    assertThat(zdtStart.plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).isEqualTo("2017-03-26T12:00:00");

    ZonedDateTime zdtEnd = ZonedDateTime.of(2017, 10, 28, 12, 0, 0, 0, ZoneId.of("Europe/Paris"));
    assertThat(zdtEnd.plusHours(24).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).isEqualTo("2017-10-29T11:00:00");
  }

  @Test
  public void testWithTemporalAdjusters() {
    LocalDate startDate = LocalDate.of(2017, 07, 25);

    LocalDate endDate = startDate.plusWeeks(2);
    assertThat(endDate).isEqualTo("2017-08-08");

    // Two weeks from given date and then get date of next Friday
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

  @Test
  public void testDayOfTheWeek() {
    DayOfWeek utorak = DayOfWeek.TUESDAY;
    Locale zagreb = Locale.getDefault();

    assertThat(utorak.getDisplayName(TextStyle.FULL, zagreb)).isEqualTo("utorak");

    assertThat(Month.JANUARY.getDisplayName(TextStyle.FULL, zagreb)).isEqualTo("siječnja");
    assertThat(Month.JANUARY.getDisplayName(TextStyle.FULL_STANDALONE, zagreb)).isEqualTo("siječanj");
  }

  @Test
  public void testMonthDay() {
    assertThat(MonthDay.of(Month.FEBRUARY, 29).isValidYear(2017)).isEqualTo(false);
    assertThat(MonthDay.of(Month.FEBRUARY, 29).isValidYear(2016)).isEqualTo(true);
  }

  @Test
  public void testOffset() {
    LocalDateTime now = LocalDateTime.now();

    ZonedDateTime la = now.atZone(ZoneId.of("America/Los_Angeles"));

    System.out.println("now: " + now);
    System.out.println("la: " + la);
    System.out.println("la offsetTime: " + la.toOffsetDateTime());
  }

  @Test
  public void testInstant() {
    Instant timestamp = Instant.now();
    System.out.println(timestamp);

    Date timestampDate = new Date(System.currentTimeMillis());
    System.out.println(timestampDate);
  }

  @Test
  public void testUnixTimestamp() {
    final long currentTimeMillis = System.currentTimeMillis();
    System.out.println(currentTimeMillis);

    final LocalDate now = LocalDate.now();
    System.out.println(now.atStartOfDay(ZoneId.systemDefault()).toEpochSecond());
  }

  @Test
  public void testCreateLocalDateFromUnixEpoch() {
    final LocalDate todayFromUnixEpoch = Instant.ofEpochMilli(System.currentTimeMillis())
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

    final LocalDate now = LocalDate.now();

    assertThat(now).isEqualTo(todayFromUnixEpoch);
  }

  class NextPaydayAdjuster implements TemporalAdjuster {

    @Override
    public Temporal adjustInto(final Temporal temporal) {
      LocalDate date = LocalDate.from(temporal);

      LocalDate thisMonthPayday = LocalDate.of(date.getYear(), date.getMonth(), 11);
      thisMonthPayday = adjustForWeekends(thisMonthPayday);

      if (date.isBefore(thisMonthPayday) || date.isEqual(thisMonthPayday)) {
        return thisMonthPayday;
      }

      LocalDate nextMonthPayday = LocalDate.of(date.getYear(), date.getMonth().plus(1), 11);
      nextMonthPayday = adjustForWeekends(nextMonthPayday);

      return nextMonthPayday;
    }

    private LocalDate adjustForWeekends(final LocalDate payday) {
      if (payday.getDayOfWeek() == DayOfWeek.SUNDAY || payday.getDayOfWeek() == DayOfWeek.SATURDAY) {
        return payday.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
      }

      return payday;
    }

  }

  @Test
  public void testCustomTemporalAdjuster() {
    LocalDate date = LocalDate.of(2017, 6, 9);
    assertThat(date.with(new NextPaydayAdjuster())).isEqualTo(date);
    assertThat(LocalDate.of(2017, 6, 3).with(new NextPaydayAdjuster())).isEqualTo(date);

    assertThat(LocalDate.of(2017, 6, 10).with(new NextPaydayAdjuster())).isEqualTo(LocalDate.of(2017, 7, 11));
  }

  class WorkDays implements TemporalQuery<Boolean> {

    @Override
    public Boolean queryFrom(final TemporalAccessor temporal) {
      LocalDate date = LocalDate.from(temporal);

      if (date.getDayOfWeek() == DayOfWeek.SUNDAY || date.getDayOfWeek() == DayOfWeek.SATURDAY) {
        return false;
      }

      return true;
    }
  }

  @Test
  public void testCustomTemporalQuery() {
    assertThat(LocalDate.of(2017, 6, 9).getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);
    assertThat(LocalDate.of(2017, 6, 9).query(new WorkDays())).isEqualTo(true);
    assertThat(LocalDate.of(2017, 6, 10).query(new WorkDays())).isEqualTo(false);
  }

  @Test
  public void testDuration() throws InterruptedException {
    Instant t1 = Instant.now();

    Thread.sleep(1500);

    Instant t2 = Instant.now();

    Duration duration = Duration.between(t1, t2);
    assertThat(duration.getSeconds()).isEqualTo(1);
    assertThat(duration).isGreaterThanOrEqualTo(Duration.ofMillis(1500));

    LocalTime start = LocalTime.of(9, 5);
    LocalTime end = LocalTime.of(10, 17);
    Duration gap = Duration.between(start, end);

    assertThat(gap.toHours()).isEqualTo(1);
    assertThat(gap.toMinutes()).isEqualTo(72);

    LocalTime start2 = LocalTime.of(11, 5);
    LocalTime end2 = LocalTime.of(13, 5);
    Duration gap2 = Duration.between(start2, end2);
    assertThat(gap2.toHours()).isEqualTo(2);

    Duration dur = gap.plus(gap2);
    assertThat(dur.toHours()).isEqualTo(3);
    assertThat(dur.toMinutes()).isEqualTo(192);
  }

  @Test
  public void testPeriods() {
    LocalDate start = LocalDate.of(2017, Month.AUGUST, 11);
    LocalDate end = LocalDate.of(2017, Month.SEPTEMBER, 21);

    Period gap = Period.between(start, end);
    assertThat(gap.getDays()).isEqualTo(10);
    assertThat(gap.getMonths()).isEqualTo(1);
  }

  @Test
  public void testParsing() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");

    LocalDate date = LocalDate.parse("13 06 2017", formatter);
    assertThat(date).isEqualTo("2017-06-13");
  }

  @Test
  public void testZone() {
    System.out.println(ZoneId.systemDefault());
    System.out.println(ZoneId.getAvailableZoneIds());
    System.out.println(ZoneId.SHORT_IDS);
  }
}
