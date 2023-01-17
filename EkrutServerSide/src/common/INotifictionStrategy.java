package common;
/**
 * interface that uses the strategy design
 * each notification is specific for the requirements needed
 * */
public interface INotifictionStrategy {
	void SendNotification(String to,String msg);
}
