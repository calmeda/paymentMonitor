package ph.almeda.payment.monitor.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DateFormatUtils;
import org.jsoup.Jsoup;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import ph.almeda.payment.monitor.model.Policy;

public class PaymentDueDateView {
	
	MongoClient mongoClient = null;
	MongoCollection<org.bson.Document> collection = null;
	
	private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:65.0) Gecko/20100101 Firefox/65.0";
	private final String urlPost = "https://api.semaphore.co/api/v4/messages";


	public static void main(String arg[]) throws Exception
	{
		System.out.println("****PaymentDueDateView Start****");
		PaymentDueDateView ddv = new PaymentDueDateView();
		Calendar cal = Calendar.getInstance();
		//cal.add(Calendar.DATE, 30);
		String dateStr = DateFormatUtils.format(cal.getTime(), "yyyy-MM-dd");
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		//String dateStr = "2017-12-01";
		ArrayList<Policy> psList = ddv.getPaymentDueDate(dateStr);
		
		ArrayList<Policy> policyReminderList = new ArrayList<Policy>();

		ConsoleView cv = new ConsoleView();
		for (Policy ps : psList)
		{
			ddv.processPaymentDueDate(ps);
			
			if (ps.getNumberOfDaysDue() == 30 || ps.getNumberOfDaysDue() == 11 || ps.getNumberOfDaysDue() == 0)
			{
				policyReminderList.add(ps);
				ddv.sendPost(ps);
				cv.consoleOutput(ps);
			}
		}
		
		System.out.println("****PaymentDueDateView End****");
	}
	
	public MongoDatabase getDatabase() {
		// To connect to mongodb server
		mongoClient = new MongoClient();
		// Now connect to your databases
		System.out.println("Connect to database successfully");
		return mongoClient.getDatabase("SunlifePoliciesSample");
	}
	
	private void sendPost(Policy ps) throws Exception {

		Map<String, String> postData = new HashMap<String, String>();
		postData.put("apikey", "7ec6d99734d54fecd26d441cf6738481");
		postData.put("number", "09177242690");
		//postData.put("number", "09209382483");
		
		StringBuffer reminderMessageSb = new StringBuffer();
		reminderMessageSb.append("*Sunlife Payment Reminder*");
		reminderMessageSb.append("\npolicyNumber:" + ps.getPolicyNumber());
		reminderMessageSb.append("\nowner:" + ps.getLastName() + ", " + ps.getFirstName());
		reminderMessageSb.append("\namountDue:" + ps.getPremiumAmount());
		reminderMessageSb.append("\ndueDate:" + ps.getPaymentDueDate());
		
		System.out.println("JSoupHttpPost Message length" + reminderMessageSb.toString().length() + "|" + reminderMessageSb.toString());
		postData.put("message", reminderMessageSb.toString());

		org.jsoup.nodes.Document doc = Jsoup.connect(urlPost).ignoreContentType(true).userAgent(USER_AGENT).data(postData).post();
		System.out.println("JSoupHttpPost " + doc.getAllElements());
	}

	
	public ArrayList<Policy> getPaymentDueDate(String dateStr)
	{
		ArrayList<Policy> psList = new ArrayList<Policy>();
		MongoDatabase db = getDatabase();
	    //BasicDBObject whereQuery = new BasicDBObject();
	    //System.out.println("dateStr " + dateStr);
	    //whereQuery.put("policyIssueDate", new BasicDBObject("$gte", dateStr));
	    //MongoCursor<Document> cursor1 = db.getCollection("PaymentDueDate").find(whereQuery).iterator();
	    MongoCursor<org.bson.Document> cursor1 = db.getCollection("PaymentDueDate").find().iterator();
    	Gson gson = new Gson(); 

	    while(cursor1.hasNext()) {
	    	psList.add(gson.fromJson(cursor1.next().toJson(), Policy.class));
	    }
	    
	    return psList;
	}
	
	/* TODO
	 * clean up this code to remove duplicate copy/paste lines
	 */
	public void processPaymentDueDate(Policy policy) throws Exception
	{
		String paymentInterval = policy.getPaymentIntervals();
		String issueDate = policy.getPolicyIssueDate();
        // use comma as separator
        String[] issueDateSplit = issueDate.split("-");
		
		if (paymentInterval.equals("annual"))
		{
			String paymentDueDate = Calendar.getInstance().get(Calendar.YEAR) + "-" + issueDateSplit[1] + "-" + issueDateSplit[2];
			System.out.println("paymentDueDate " + paymentDueDate);

			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);
			
			String monthStr = "";
			if (month < 10)
			{
				monthStr = "0" + month;
			} else
			{
				monthStr = "" + month;
			}
			String today = year + "-" + monthStr + "-" + day;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		    Date todaysDate = sdf.parse(today);
		    Date q1Date = sdf.parse(paymentDueDate);
		    
		    //q1
		    long diffInMillies1 = Math.abs(q1Date.getTime() - todaysDate.getTime());
		    long diff1 = TimeUnit.DAYS.convert(diffInMillies1, TimeUnit.MILLISECONDS);

		    policy.setNumberOfDaysDue(diff1);
		    policy.setPaymentDueDate(paymentDueDate);

		} else if (paymentInterval.equals("semi-annual"))
		{
			int q3 = Integer.parseInt(issueDateSplit[1]) + 6;
			
			if (q3 > 12)
			{
				q3 = q3 - 12;
			}
			
			String q3Str = "";
			if (q3 < 10)
			{
				q3Str = "0" + q3;
			} else
			{
				q3Str = "" + q3;
			}

			//q1
			String paymentDueDate1 = Calendar.getInstance().get(Calendar.YEAR) + "-" + issueDateSplit[1] + "-" + issueDateSplit[2];
			System.out.println("paymentDueDate " + paymentDueDate1);
			
			//q3
			String paymentDueDate3 = Calendar.getInstance().get(Calendar.YEAR) + "-" + q3Str + "-" + issueDateSplit[2];
			System.out.println("paymentDueDate " + paymentDueDate3);
			
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);
			
			String monthStr = "";
			if (month < 10)
			{
				monthStr = "0" + month;
			} else
			{
				monthStr = "" + month;
			}
			String today = year + "-" + monthStr + "-" + day;
			
			//get nearest date from current date
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		    Date todaysDate = sdf.parse(today);
		    Date q1Date = sdf.parse(paymentDueDate1);
		    Date q3Date = sdf.parse(paymentDueDate3);
		    
		    long[] qs = new long[2];
		    Hashtable ht = new Hashtable();
		 
		    //q1
		    long diffInMillies1 = Math.abs(q1Date.getTime() - todaysDate.getTime());
		    long diff1 = TimeUnit.DAYS.convert(diffInMillies1, TimeUnit.MILLISECONDS);
		    System.out.println("q1 diffInDays " + diff1);
		    qs[0] = diff1;
		    ht.put("" + diff1, paymentDueDate1);
			
		    //q3
		    long diffInMillies3 = Math.abs(q3Date.getTime() - todaysDate.getTime());
		    long diff3 = TimeUnit.DAYS.convert(diffInMillies3, TimeUnit.MILLISECONDS);
		    System.out.println("q3 diffInDays " + diff3);
		    qs[2] = diff3;
		    ht.put("" + diff3, paymentDueDate3);
		    
		    Arrays.sort(qs);
		    
		    System.out.println("lowest " + qs[0]);
		    policy.setNumberOfDaysDue(qs[0]);
		    policy.setPaymentDueDate((String) ht.get("" + qs[0]));
		    
		    System.out.println("ht.get " + ht.get("" + qs[0]));
			
		} else if (paymentInterval.equals("quarterly"))
		{
			
			int q2 = Integer.parseInt(issueDateSplit[1]) + 3;
			int q3 = Integer.parseInt(issueDateSplit[1]) + 6;
			int q4 = Integer.parseInt(issueDateSplit[1]) + 9;
			
			if (q2 > 12)
			{
				q2 = q2 - 12;
			} 
			
			if (q3 > 12)
			{
				q3 = q3 - 12;
			}
			
			if (q4 > 12)
			{
				q4 = q4 - 12;
			}
			
			String q2Str = "";
			if (q2 < 10)
			{
				q2Str = "0" + q2;
			} else
			{
				q2Str = "" + q2;
			}

			String q3Str = "";
			if (q3 < 10)
			{
				q3Str = "0" + q3;
			} else
			{
				q3Str = "" + q3;
			}

			String q4Str = "";
			if (q4 < 10)
			{
				q4Str = "0" + q4;
			} else
			{
				q4Str = "" + q4;
			}

			
			//q1
			String paymentDueDate1 = Calendar.getInstance().get(Calendar.YEAR) + "-" + issueDateSplit[1] + "-" + issueDateSplit[2];
			//System.out.println("paymentDueDate " + paymentDueDate1);
			
			//q2
			String paymentDueDate2 = Calendar.getInstance().get(Calendar.YEAR) + "-" + q2Str + "-" + issueDateSplit[2];
			//System.out.println("paymentDueDate " + paymentDueDate2);
			
			//q3
			String paymentDueDate3 = Calendar.getInstance().get(Calendar.YEAR) + "-" + q3Str + "-" + issueDateSplit[2];
			//System.out.println("paymentDueDate " + paymentDueDate3);
			
			//q4
			String paymentDueDate4 = Calendar.getInstance().get(Calendar.YEAR) + "-" + q4Str + "-" + issueDateSplit[2];
			//System.out.println("paymentDueDate " + paymentDueDate4);

			//Calendar.getInstance().get(Calendar.MONTH)
			//System.out.println("Calendar.getInstance().get(Calendar.MONTH) " + Calendar.getInstance().get(Calendar.MONTH) + 1);
			
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);
			
			String monthStr = "";
			if (month < 10)
			{
				monthStr = "0" + month;
			} else
			{
				monthStr = "" + month;
			}
			String today = year + "-" + monthStr + "-" + day;
			
			//get nearest date from current date
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		    Date todaysDate = sdf.parse(today);
		    Date q1Date = sdf.parse(paymentDueDate1);
		    Date q2Date = sdf.parse(paymentDueDate2);
		    Date q3Date = sdf.parse(paymentDueDate3);
		    Date q4Date = sdf.parse(paymentDueDate4);
		    
		    long[] qs = new long[4];
		    Hashtable ht = new Hashtable();
		 
		    //q1
		    long diffInMillies1 = Math.abs(q1Date.getTime() - todaysDate.getTime());
		    long diff1 = TimeUnit.DAYS.convert(diffInMillies1, TimeUnit.MILLISECONDS);
		    //System.out.println("q1 diffInDays " + diff1);
		    qs[0] = diff1;
		    ht.put("" + diff1, paymentDueDate1);
			
		    //q2
		    long diffInMillies2 = Math.abs(q2Date.getTime() - todaysDate.getTime());
		    long diff2 = TimeUnit.DAYS.convert(diffInMillies2, TimeUnit.MILLISECONDS);
		    //System.out.println("q2 diffInDays " + diff2);
		    qs[1] = diff2;
		    ht.put("" + diff2, paymentDueDate2);
		    
		    //q3
		    long diffInMillies3 = Math.abs(q3Date.getTime() - todaysDate.getTime());
		    long diff3 = TimeUnit.DAYS.convert(diffInMillies3, TimeUnit.MILLISECONDS);
		    //System.out.println("q3 diffInDays " + diff3);
		    qs[2] = diff3;
		    ht.put("" + diff3, paymentDueDate3);
		    
		    //q4
		    long diffInMillies4 = Math.abs(q4Date.getTime() - todaysDate.getTime());
		    long diff4 = TimeUnit.DAYS.convert(diffInMillies4, TimeUnit.MILLISECONDS);
		    //System.out.println("q4 diffInDays " + diff4);
		    qs[3] = diff4;
		    ht.put("" + diff4, paymentDueDate4);
		    
		    Arrays.sort(qs);
		    
		    //System.out.println("lowest " + qs[0]);
		    policy.setNumberOfDaysDue(qs[0]);
		    policy.setPaymentDueDate((String) ht.get("" + qs[0]));
		    
		    //System.out.println("ht.get " + ht.get("" + qs[0]));
		}
	}




}
