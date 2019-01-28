package ph.almeda.payment.monitor.transform;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ph.almeda.payment.monitor.model.Policy;

public class CSVToPolicyHandler {
	
	public Policy processPolicy(List<String> line) throws Exception {
		Policy policyObj = new Policy();
        policyObj.setPolicyNumber(line.get(0));
        
        String name = line.get(1);
        name = name.replaceAll("\"", "");
        
        // use comma as separator
        String[] nameStr = name.split(",");

        policyObj.setLastName(nameStr[0]);
        policyObj.setFirstName(nameStr[1].trim());
        
        policyObj.setPolicyName(line.get(3));
        
        String sDate1=line.get(5);
        sDate1 = sDate1.replaceAll("\"", "");

        //convert String to Date
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        Date date = format.parse(sDate1);
        //System.out.println(date);
        
        //convert Date to String, use format method of SimpleDateFormat class.
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        //System.out.println("converted Date to String: " + strDate);
        
        policyObj.setPolicyIssueDate(strDate);

        String amount=line.get(6);
        amount = amount.replaceAll("\"", "");

        policyObj.setPremiumAmount(amount);
        
        policyObj.setPaymentIntervals("quarterly");
        
        return policyObj;

	}

}
