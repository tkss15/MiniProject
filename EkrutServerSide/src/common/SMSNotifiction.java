package common;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SMSNotifiction implements INotifictionStrategy 
{
	String phone;
	public SMSNotifiction(String phone)
	{
		
	}
	@Override
	public void SendNotification(String to, String msg) 
	{
	        Twilio.init("AUTH", "KEY");
	        Message message = Message.creator(
	                new com.twilio.type.PhoneNumber(to),
	                new com.twilio.type.PhoneNumber("+12057821468"),
	                msg)
	            .create();
	}

}