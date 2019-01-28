package ph.almeda.payment.monitor.model;

import java.util.Date;

public class Policy {
	
	private String lastName;
	private String firstName;
	private String middleName;
	private String policyNumber;
	private String policyIssueDate;
	private String policyName;
	private String paymentIntervals;
	private String premiumAmount;
	private String paymentDueDate;
	private long numberOfDaysDue;
	
	public String getPaymentDueDate() {
		return paymentDueDate;
	}
	public void setPaymentDueDate(String paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}
	public long getNumberOfDaysDue() {
		return numberOfDaysDue;
	}
	public void setNumberOfDaysDue(long qs) {
		this.numberOfDaysDue = qs;
	}
	public String getPaymentIntervals() {
		return paymentIntervals;
	}
	public void setPaymentIntervals(String paymentIntervals) {
		this.paymentIntervals = paymentIntervals;
	}
	public String getPolicyName() {
		return policyName;
	}
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getPolicyIssueDate() {
		return policyIssueDate;
	}
	public void setPolicyIssueDate(String policyIssueDate) {
		this.policyIssueDate = policyIssueDate;
	}
	public String getPremiumAmount() {
		return premiumAmount;
	}
	public void setPremiumAmount(String premiumAmount) {
		this.premiumAmount = premiumAmount;
	}

}
