package common;

/**
 * all controllers implement this interface.
 * through this interface we can send and update data directly in the controller.
 *
 */

/**
 * updates and saves the data which is received from the server in the current displaying controller.
 *
 */
public interface IController {
	void updatedata(Object data);
}
