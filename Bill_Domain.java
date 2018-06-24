package com.Daybook.Domain;

public class Bill_Domain {
	
	int billid;
	int client_id;
	String bill_date;
	String bill_Duedate;
	String bill_detl;
	double bill_amount;
	String bill_sttus;
	
	public Bill_Domain(){}
	public Bill_Domain(int bill,int client,String bdate,String bddate,String billdetl,double billamnt,String bstatus)
	{
		this.billid=bill;
		this.client_id=client;
		this.bill_date=bdate;
		this.bill_Duedate=bddate;
		this.bill_detl=billdetl;
		this.bill_amount=billamnt;
		this.bill_sttus=bstatus;	
	}
	public int getBillid() {
		return billid;
	}
	public void setBillid(int billid) {
		this.billid = billid;
	}
	public int getClient_id() {
		return client_id;
	}
	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}
	public String getBill_date() {
		return bill_date;
	}
	public void setBill_date(String bill_date) {
		this.bill_date = bill_date;
	}
	public String getBill_Duedate() {
		return bill_Duedate;
	}
	public void setBill_Duedate(String bill_Duedate) {
		this.bill_Duedate = bill_Duedate;
	}
	public String getBill_detl() {
		return bill_detl;
	}
	public void setBill_detl(String bill_detl) {
		this.bill_detl = bill_detl;
	}
	public double getBill_amount() {
		return bill_amount;
	}
	public void setBill_amount(double bill_amount) {
		this.bill_amount = bill_amount;
	}
	public String getBill_sttus() {
		return bill_sttus;
	}
	public void setBill_sttus(String bill_sttus) {
		this.bill_sttus = bill_sttus;
	}
	

}
