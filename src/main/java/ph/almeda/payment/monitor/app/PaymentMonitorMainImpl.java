package ph.almeda.payment.monitor.app;

import java.util.ArrayList;
import java.util.Date;

import ph.almeda.payment.monitor.load.MongoDBLoader;
import ph.almeda.payment.monitor.model.Policy;

public class PaymentMonitorMainImpl {
	
	public static void main(String args[]) throws Exception
	{
		
		ArrayList<Policy> pList = null;

		Policy policy = new Policy();
		policy.setFirstName("Juan");
		policy.setLastName("Dela Cruz");
		policy.setMiddleName("Yu");
		policy.setPolicyIssueDate("12/15/2016");
		policy.setPolicyNumber("0813299586");
		policy.setPolicyName("Sun Maxilink Prime - Basic");
		policy.setPremiumAmount("15000");
		
		MongoDBLoader mdb = new MongoDBLoader();
		mdb.insertOneGson(policy);
		
		System.out.println("done");

	}

}
