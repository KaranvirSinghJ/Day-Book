package com.Daybook.Domain;

public class Reminder_Domain {
	
	int rem_id;
	String rem_time;
	String rem_vib;
	String rem_sound;
	String rem_days;
	public boolean box;
	public int getRem_id() {
		return rem_id;
	}
	public void setRem_id(int rem_id) {
		this.rem_id = rem_id;
	}
	public String getRem_time() {
		return rem_time;
	}
	public void setRem_time(String rem_time) {
		this.rem_time = rem_time;
	}

    
	public String getRem_vib() {
		return rem_vib;
	}
	public void setRem_vib(String rem_vib) {
		this.rem_vib = rem_vib;
	}
	public String getRem_sound() {
		return rem_sound;
	}
	public void setRem_sound(String rem_sound) {
		this.rem_sound = rem_sound;
	}
	public String getRem_days() {
		return rem_days;
	}
	public void setRem_days(String rem_days) {
		this.rem_days = rem_days;
	}
	public Reminder_Domain(){}
	public Reminder_Domain(int rem_ids,String rem_times,String rem_vibs,String rem_sounds,String days)
	{
		this.rem_id=rem_ids;
		this.rem_time=rem_times;
		this.rem_vib=rem_vibs;
		this.rem_sound=rem_sounds;
		this.rem_days=days;
	}
	public boolean isBox() {
		return box;
	}
	public void setBox(boolean box) {
		this.box = box;
	}
	
}
