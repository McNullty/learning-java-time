package hr.analemma.learning.java.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.junit.Test;

public class DateTimestampTest {

  @Test
  public void testTimestamp() {
    TimeZone zone = TimeZone.getTimeZone("America/New_York");
    final Calendar calendar = Calendar.getInstance(zone);

    final Date date = calendar.getTime();

    System.out.println(date);
  }

  @Test
  public void secondTest() {

    final long timestamp = System.currentTimeMillis();
    System.out.println(timestamp);
    Date currentDate = new Date(timestamp);
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    TimeZone zoneNewYork = TimeZone.getTimeZone("America/New_York");
    df.setTimeZone(zoneNewYork);
    String finale = df.format(currentDate);
    System.out.println(finale);

    TimeZone zoneEst = TimeZone.getTimeZone("EST");
    df.setTimeZone(zoneEst);
    finale = df.format(currentDate);
    System.out.println(finale);

    final long timestamp2 = (long) Math.floor(timestamp / 1000l);
    System.out.println(timestamp2);
  }
}
