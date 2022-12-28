package common;

public class Employee {

	public enum EmployeeRole {
		AreaDeliveryOperator, Provider, AreaManager, NetworkEmployee, NetworkMarketingManager, CEO
	};

	private EmployeeRole employeeRole;
	private String userName;

	public Employee() {}
	public Employee(EmployeeRole employeeRole, String userName) {
		super();
		this.employeeRole = employeeRole;
		this.userName = userName;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
