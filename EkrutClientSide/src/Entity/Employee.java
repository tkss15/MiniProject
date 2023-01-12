package Entity;

public class Employee extends User {

	public enum EmployeeRole {
		AreaDeliveryOperator, Provider, AreaManager, NetworkEmployee, NetworkMarketingManager, CEO
	};

	private EmployeeRole employeeRole;
	

	public Employee() {super();}
	
	public Employee(User user) {
		//String firstName, String lastName, String phone, String email, String ID, String UserName,
		//String Password)
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
		// TODO Auto-generated method stub
		return super.toString();
	}

	public EmployeeRole getEmployeeRole() {
		return employeeRole;
	}

	public void setEmployeeRole(EmployeeRole employeeRole) {
		this.employeeRole = employeeRole;
	}
	
}
