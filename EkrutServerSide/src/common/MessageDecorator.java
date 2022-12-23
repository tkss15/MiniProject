package common;


public abstract class MessageDecorator implements MessageHandler 
{
	protected MessageHandler message;
	
	public MessageDecorator(MessageHandler message)
	{
		this.message = message;
	}
	
	public abstract void handle();
}
