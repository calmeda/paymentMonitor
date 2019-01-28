package ph.almeda.payment.monitor.poc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateFormatPOC {
/*	   public static void main(String[] args) throws Exception {

		    String strDate = "Sep 30, 2017";
		      DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		      Date da = (Date)formatter.parse(strDate);
		      System.out.println("==Date is ==" + da);
		      String strDateTime = formatter.format(da);

		      System.out.println("==String date is : " + strDateTime);
		}
*/	   
	   public static void main(String[] args)
	   {
	    LocalDateTime now = LocalDateTime.now();
	    printDate(now, "YYYY-MM-dd");
	    printDate(now, "MM-dd-YYYY");
	    printDate(now, "dd MMM YYYY");
	    printDate(now, "MMMM d, YYYY");
	    printDate(now, "MMM d, YYYY");

	   }
	   public static void printDate(LocalDateTime date, String pattern)
	   {
	    DateTimeFormatter f = DateTimeFormatter.ofPattern(pattern);
	    pattern = (pattern + "    ").substring(0, 14);
	    System.out.println(pattern + " " + date.format(f));
	   }
}
