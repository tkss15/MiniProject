package Entity;

public class RegisterClient extends User {
	private User clientUser;
	private ClientStatus ClientStatus;

	private Integer clientSubscriberNumber;
	private Double clientMonthlyFee;
	private String clientCardNumber, clientCardDate;
	private Boolean clientFirstPurchase;
	
	public enum ClientStatus 
	{
		CLIENT_APRROVED,
		CLIENT_SUBSCRIBER,
		CLIENT_PENDING
	}
	
	public RegisterClient(User clientUser,
			Integer clientSubscriberNumber, Double clientMonthlyFee, String clientCardNumber, String clientCardDate,
			Boolean clientFirstPurchase) 
	{
		super(clientUser.getFirstName(),clientUser.getLastName(),clientUser.getPhone(),clientUser.getEmail(),clientUser.getID(),clientUser.getUserName(),
				clientUser.getPassword(),clientUser.getArea());
		this.setOnlineStatus(clientUser.getOnlineStatus());
		
		this.clientSubscriberNumber = clientSubscriberNumber;
		this.clientMonthlyFee = clientMonthlyFee;
		this.clientCardNumber = clientCardNumber;
		this.clientCardDate = clientCardDate;
		this.clientFirstPurchase = clientFirstPurchase;
	}
	
	@Override
	public String toString() {
		return "RegisterClient [clientUser=" + clientUser + ", ClientStatus=" + ClientStatus
				+ ", clientSubscriberNumber=" + clientSubscriberNumber + ", clientMonthlyFee=" + clientMonthlyFee
				+ ", clientCardNumber=" + clientCardNumber + ", clientCardDate=" + clientCardDate
				+ ", clientFirstPurchase=" + clientFirstPurchase + "]";
	}
	public User getClientUser() {
		return clientUser;
	}
	public void setClientUser(User clientUser) {
		this.clientUser = clientUser;
	}
	public ClientStatus getClientStatus() {
		return ClientStatus;
	}
	public void setClientStatus(String clientStatus) {
		//enum('SUBSCRIBER','APPROVED','NOT_APPROVED','PENDING')
		switch(clientStatus)
		{
			case"SUBSCRIBER": ClientStatus = ClientStatus.CLIENT_SUBSCRIBER;
				break;
			case"APPROVED": ClientStatus = ClientStatus.CLIENT_APRROVED;
				break;
			case"PENDING":ClientStatus = ClientStatus.CLIENT_PENDING;
				break;
		}
	}
	public Integer getClientSubscriberNumber() {
		return clientSubscriberNumber;
	}
	public void setClientSubscriberNumber(Integer clientSubscriberNumber) {
		this.clientSubscriberNumber = clientSubscriberNumber;
	}
	public Double getClientMonthlyFee() {
		return clientMonthlyFee;
	}
	public void setClientMonthlyFee(Double clientMonthlyFee) {
		this.clientMonthlyFee = clientMonthlyFee;
	}
	public String getClientCardNumber() {
		return clientCardNumber;
	}
	public void setClientCardNumber(String clientCardNumber) {
		this.clientCardNumber = clientCardNumber;
	}
	public String getClientCardDate() {
		return clientCardDate;
	}
	public void setClientCardDate(String clientCardDate) {
		this.clientCardDate = clientCardDate;
	}
	public Boolean getClientFirstPurchase() {
		return clientFirstPurchase;
	}
	public void setClientFirstPurchase(Boolean clientFirstPurchase) {
		this.clientFirstPurchase = clientFirstPurchase;
	}
}
