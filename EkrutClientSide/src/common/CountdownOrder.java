package common;

import client.ClientUI;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

/***
 * @author eldad
 * CountdownOrder is the responsible class of the auto-termination to an order after x minutes.
 * 
 * @Task explanation:
 * Task is a background thread which can be used to perform time consuming operations without freezing the user interface.
 * We using task in this Class because the Timer will run down to 0 and if we use the same Thread as the Javafx Application thread
 * the UI will be stuck.
 */
public class CountdownOrder 
{
	
	private Task<Void> countdownTask;// we want to run the countdown in the background so we use task. watch @Task
	private int countdownOrder;// a variable that saves the countdown timer. 
	
	private final int TIME_FIVE_MINUTES = 60*5;// 300 Seconds equals 5 minutes
	
	// Empty Constructor means we just started the order and we will get full 5 minutes
	public CountdownOrder()
	{
		countdownOrder = TIME_FIVE_MINUTES;
	}
	// In case we moving between pages the countdown will continue from a certain point which is the first argument in this function.
	public CountdownOrder(int countdownOrder)
	{
		this.countdownOrder = countdownOrder;
	}
	/***
	 * timeFormat returns a String which contains the amount of time left for the countdown
	 * @return a String with the format of Minutes:Seconds.
	 */
	private String timeFormat()
	{
		int Min = countdownOrder/60;
		int Seconds = (countdownOrder - Min*60);
		return String.format("%d:%d", Min,Seconds);
	}
	// This method is Important since we need to be able to cancel a task once we leave the page.
	public void cancelTask()
	{
		countdownTask.cancel();
	}
	/***
	 * initialize will start the Countdown task which will run at the background this function will sleep for 1 second. each time 
	 * the thread will sleep it will decrease the countdown by one until it stops.
	 * @updateMessage - is a method that Task can operate this method updates a given message property 
	 * 
	 * @param timer
	 */
	public void initialize(Label timer)
	{
	    countdownTask = new Task<Void>() 
	    {
	        @Override
	        protected Void call() throws Exception 
	        {
	          while (countdownOrder >= 0) // While countdown is still on
	          {
	        	 updateMessage(timeFormat());// Updates the Label text field to be the the current Minutes:Seconds.
	            countdownOrder--;
	            Thread.sleep(1000);
	          }	  			
	          return null;// We always return null even if we dont have to.
	        }
	   };
	   // setOnSucceeded means that if task didnt throw an unexpected Error ( or task just completed ) it will end up doing the wirten inside 
	   // this function. this method uses lambda function and not anonymous class since is easier.
	   countdownTask.setOnSucceeded(event -> {
		   ClientUI.clientController.UserDisconnected(true);
		   System.exit(0);
	   });
	   // In order to use updateMessage in countdownTask we need to bind a property to the contdownTask.
       timer.textProperty().bind(countdownTask.messageProperty());
	   new Thread(countdownTask).start();// Task only starts here until now we just initialized it.
	}
	/***
	 * Getters and Setters for the Timer.
	 * @return
	 */
	public int getCountdownOrder() 
	{
		return countdownOrder;
	}
	public void setCountdownOrder(int countdownOrder) {
		this.countdownOrder = countdownOrder;
	}
}
