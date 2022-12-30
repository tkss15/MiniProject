package Entity;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Product 
{
	@Override
	public String toString() {
		return "Product [ProductAmount=" + ProductAmount + ", MaxAmount=" + MaxAmount + ", ProductCode=" + ProductCode
				+ ", ProductName=" + ProductName + ", ProductDescription=" + ProductDescription + ", ProductSrc="
				+ ProductSrc + ", ProductPrice=" + ProductPrice + ", PicturePhoto=" + PicturePhoto + ", dirPictures="
				+ dirPictures + "]";
	}
	private int ProductAmount = 1;
	private int MaxAmount;
	private Integer ProductCode;
	private String ProductName, ProductDescription, ProductSrc;
	private Double ProductPrice;
	public File PicturePhoto;
	
	public int getMaxAmount() {
		return MaxAmount;
	}
	public void setMaxAmount(int maxAmount) {
		MaxAmount = maxAmount;
	}
	private final String dirPictures = "EkrutApplication";
	
	public Product(Integer ProductCode,String ProductName,String ProductDescription,String ProductSrc,Double ProductPrice, int MaxAmount)
	{
		this.ProductCode = ProductCode;
		this.ProductName = ProductName;
		this.ProductDescription = ProductDescription;
		this.ProductSrc = ProductSrc;
		this.ProductPrice = ProductPrice;
		this.MaxAmount = MaxAmount;
		File newDir = new File(dirPictures);
		newDir.mkdirs();
		this.PicturePhoto = new File(dirPictures + "/" + this.ProductSrc);
	}
//	public String getPictureURL()
//	{
//		return 
//	}
	public String getPathPicture()
	{
		System.out.println(PicturePhoto.getAbsolutePath());
		return String.format("file:///%s", PicturePhoto.getAbsolutePath());
	}
	public Product(Product Oldproduct)
	{
		this(Oldproduct.ProductCode,
			Oldproduct.getProductName(),
			Oldproduct.getProductDescription(),
			Oldproduct.getProductSrc(),
			Oldproduct.getProductPrice(), 
			Oldproduct.MaxAmount);
		this.setAmount(Oldproduct.getProductAmount());
	}
	public int getProductAmount() {
		return ProductAmount;
	}
	public String getProductName() {
		return ProductName;
	}
	public String getProductDescription() {
		return ProductDescription;
	}
	public String getProductSrc() {
		return ProductSrc;
	}
	public Double getProductPrice() {
		return ProductPrice;
	}
	public void setAmount(int Amount)
	{
		ProductAmount = Amount;
	}
	@Override
	public int hashCode() {
		return Objects.hash(ProductCode);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Product))
			return false;
		Product other = (Product) obj;
		return Objects.equals(ProductCode, other.ProductCode);
	}
}
