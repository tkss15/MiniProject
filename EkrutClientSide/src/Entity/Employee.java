package Entity;


/**
 * 
 * @author galmu
 * The Employee class is a subclass of the User class that represents an employee within the Ekrut system.
 * It contains an enumeration of possible employee roles, as well as a field to store the employee's current role.
 * The class has a default constructor, as well as a constructor that takes in a User object to copy its properties.
 * It also has a constructor that takes in an EmployeeRole and a userName.
 * The class overrides the toString() method inherited from the User class to provide its own implementation.
 * It also has getters and setters for the employeeRole field.
 */

public class Employee extends User {

	public enum EmployeeRole {
		AreaDeliveryOperator, Provider, AreaManager, NetworkEmployee, NetworkMarketingManager, CEO
	};

	private EmployeeRole employeeRole;
	

	public Employee() {super();}
	
	public Employee(User user) {
		super(user.getFirstName(),user.getLastName(),user.getPhone(),user.getEmail(),user.getID(),user.getUserName(),
				user.getPassword(),user.getArea());
		this.setOnlineStatus(user.getOnlineStatus());
	}
	public Employee(EmployeeRole employeeRole, String userName) {
		super();
		this.employeeRole = employeeRole; 
		super.setUserName(userName);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public EmployeeRole getEmployeeRole() {
		return employeeRole;
	}

	public void setEmployeeRole(EmployeeRole employeeRole) {
		this.employeeRole = employeeRole;
	}
	
}
