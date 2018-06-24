package com.Daybook.Domain;

public class today_DOMAIN {
	String mem_name;
	String mem_addr;

	String item_name;
	String status;
	double totamnt;
	
	int trans_id;
	int client_id;

	public today_DOMAIN(){}
	
	public today_DOMAIN(int c_id,int tr_id,String clname,String claddr,String nm,String stat,double tot)
	{
		this.trans_id=tr_id;
		this.client_id=c_id;
		this.mem_name=clname;
		this.mem_addr=claddr;
		this.item_name=nm;
		this.status=stat;
		this.totamnt=tot;
	}
	
	public String getMem_name() {
		return mem_name;
	}
	public void setMem_name(String mem_name) {
		this.mem_name = mem_name;
	}
	public String getMem_addr() {
		return mem_addr;
	}
	public void setMem_addr(String mem_addr) {
		this.mem_addr = mem_addr;
	}
	
	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getTotamnt() {
		return totamnt;
	}
	public void setTotamnt(double totamnt) {
		this.totamnt = totamnt;
	}
	public int getTrans_id() {
		return trans_id;
	}
	public void setTrans_id(int trans_id) {
		this.trans_id = trans_id;
	}
	public int getClient_id() {
		return client_id;
	}
	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}
	
}
