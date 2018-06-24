package com.Daybook.BO;

public class BASEBO {
	
	public String short12_String(String str)
	{
		String str2="";
		if(str.length()==0)
		{
			
		}
		else if(str.length()>=12)
		{
		str2=str.substring(0, 11)+"..";
		}
		else
		{
			str2=str;
		}
		return str2;
	}
	public String short20_String(String str)
	{
		String str2="";
		if(str.length()==0)
		{
			
		}
		else if(str.length()>=20)
		{
		str2=str.substring(0, 19)+"..";
		}
		else
		{
			str2=str;
		}
		return str2;
	}

}
