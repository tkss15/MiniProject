package common;


import com.twilio.rest.api.v2010.account.Message;

public class SMSNotifiction implements INotifictionStrategy 
{
	private String CelephoneReciver;
	public SMSNotifiction(String CelephoneReciver)
	{
		// if Phone was 054.... its now +97254....
		this.CelephoneReciver = "+972" + CelephoneReciver.substring(1);
	}
	public void SendNotification(String msg) 
	{
		SendNotification(CelephoneReciver,msg);
	}
	@Override
	public void SendNotification(String to, String msg) 
	{
		Thread SendSMS = new Thread() 
		{
		    public void run() 
		    {
		        Message message = Message.creator(
				        new com.twilio.type.PhoneNumber(to),
				        new com.twilio.type.PhoneNumber("+12057821468"),
				        msg)
				    .create();
		    }  
		};

		SendSMS.start();
		
	}

}
