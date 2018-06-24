package com.Daybook.Domain;

public class Register_DOMAIN {
	
	String mem_name;
	String mem_addr;
	String mem_contact;
	int mem_id;
	
	public int getMem_id() {
		return mem_id;
	}
	public void setMem_id(int mem_id) {
		this.mem_id = mem_id;
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
	public String getMem_contact() {
		return mem_contact;
	}
	public void setMem_contact(String mem_contact) {
		this.mem_contact = mem_contact;
	}
	
	
	public Register_DOMAIN(int id,String name,String addrss,String contact)
	{
		this.mem_name=name;
		this.mem_addr=addrss;
		this.mem_contact=contact;
		this.mem_id=id;	
		
	}
	public Register_DOMAIN(){}
	
}
