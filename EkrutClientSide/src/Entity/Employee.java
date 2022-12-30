package Entity;

public class Employee extends User {

	public enum EmployeeRole {
		AreaDeliveryOperator, Provider, AreaManager, NetworkEmployee, NetworkMarketingManager, CEO
	};

	private EmployeeRole employeeRole;
	private String branch;
	
	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public Employee() {super();}
	
	public Employee(User user,String branch) {
		//String firstName, String lastName, String phone, String email, String ID, String UserName,
		//String Password)
		super(user.getFirstName(),user.getLastName(),user.getPhone(),user.getEmail(),user.getID(),user.getUserName(),
				user.getPassword());
		this.branch = branch;
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
