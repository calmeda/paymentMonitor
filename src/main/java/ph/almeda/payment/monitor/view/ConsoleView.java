package ph.almeda.payment.monitor.view;

import ph.almeda.payment.monitor.model.Policy;

public class ConsoleView {

	public void consoleOutput(Policy ps) {
		System.out.print("Due in:" + ps.getNumberOfDaysDue() + " days |");
		System.out.print("Due Date is:" + ps.getPaymentDueDate()+ "|");
		System.out.print("Policy Number:" + ps.getPolicyNumber() + "|");
		System.out.print("Policy LastName:" + ps.getLastName() + "|");
		System.out.print("Policy FirstName:" + ps.getFirstName() + "|");
		System.out.print("Payment Intervals:" + ps.getPaymentIntervals() + "|");
		System.out.print("PolicyName:" + ps.getPolicyName() + "|");
		System.out.print("PolicyIssueDate:" + ps.getPolicyIssueDate() + "|");
		System.out.println("PremiumAmount:" +ps.getPremiumAmount() + "|");
	}

}
