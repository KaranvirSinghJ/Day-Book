package com.Daybook.Domain;

import com.Daybook.BO.BASEBO;

public class trans_DOMAIN  extends BASEBO{

	String Itemname;
	String Item_detail;
	String type;
	int bid;
	double totamnt;
	int trans_id;
	int client_id;
    public boolean box;
	
	public trans_DOMAIN() {}
	public trans_DOMAIN(int tid,String item,String typ,String itemdet,Double tot,int billid)
	{
		
		this.trans_id=tid;
		this.Itemname=item;
		this.Item_detail=itemdet;
		this.type=typ;
		this.totamnt=tot;	
		this.bid=billid;
	}
	
	public int getBid() {
		return bid;
	}
	public void setBid(int bid) {
		this.bid = bid;
	}
	public String getItemname() {
		return Itemname;
	}
	public void setItemname(String itemname) {
		Itemname = itemname;
	}
	public String getItem_detail() {
		return Item_detail;
	}
	public void setItem_detail(String item_detail) {
		Item_detail = item_detail;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public boolean isBox() {
		return box;
	}
	public void setBox(boolean box) {
		this.box = box;
	}	
}
