package common;

import client.ClientUI;
import javafx.concurrent.Task;
import javafx.scene.control.Label;


public class CountdownOrder 
{
	
	private Task<Void> countdownTask;
	private int countdownOrder;
	
	private final int TIME_FIVE_MINUTES = 15*5;
	
	public CountdownOrder()
	{
		countdownOrder = TIME_FIVE_MINUTES;
	}
	public CountdownOrder(int countdownOrder)
	{
		this.countdownOrder = countdownOrder;
	}
	private String timeFormat()
	{
		int Min = countdownOrder/60;
		int Seconds = (countdownOrder - Min*60);
		return String.format("%d:%d", Min,Seconds);
	}
	public void cancelTask()
	{
		countdownTask.cancel();
	}
	public void initialize(Label timer)
	{
	    countdownTask = new Task<Void>() 
	    {
	        @Override
	        protected Void call() throws Exception 
	        {
	          while (countdownOrder >= 0) 
	          {
	        	 updateMessage(timeFormat());
	            countdownOrder--;
	            Thread.sleep(1000);
	          }	  			
	          return null;
	        }
	   };
	   countdownTask.setOnSucceeded(event -> {
		   System.out.println("Time is up");
		   ClientUI.clientController.UserDissconnected();
		   System.exit(0);
	   });
       timer.textProperty().bind(countdownTask.messageProperty());
	   new Thread(countdownTask).start();
	}
	public int getCountdownOrder() {
		return countdownOrder;
	}
	public void setCountdownOrder(int countdownOrder) {
		this.countdownOrder = countdownOrder;
	}
}
