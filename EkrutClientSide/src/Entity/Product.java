package Entity;

import java.io.File;
import java.util.Objects;

import common.IProductPrice;

public class Product 
{
	private int ProductAmount = 1;
	private int MaxAmount;
	private Integer ProductCode;

	private String ProductName, ProductDescription, ProductSrc;
	private Double ProductPrice;
	public File PicturePhoto;
	
	private IProductPrice PriceStategy;
	
	private final String dirPictures = "EkrutApplication";
	
	public Product(Integer ProductCode,String ProductName,String ProductDescription,String ProductSrc,Double ProductPrice, int MaxAmount)
	{
		this.ProductCode = ProductCode;
		this.ProductName = ProductName;
		this.ProductDescription = ProductDescription;
		this.ProductSrc = ProductSrc;
		this.ProductPrice = ProductPrice;
		this.MaxAmount = MaxAmount;
		this.PriceStategy = new PriceStartegyRegular();
		File newDir = new File(dirPictures);
		newDir.mkdirs();
		this.PicturePhoto = new File(dirPictures + "/" + this.ProductSrc);
	}
	public Product(Integer ProductCode,String ProductName,String ProductDescription,String ProductSrc,Double ProductPrice)
	{
		this.ProductCode = ProductCode;
		this.ProductName = ProductName;
		this.ProductDescription = ProductDescription;
		this.ProductSrc = ProductSrc;
		this.ProductPrice = ProductPrice;
		this.PriceStategy = new PriceStartegyRegular();
		File newDir = new File(dirPictures);
		newDir.mkdirs();
		this.PicturePhoto = new File(dirPictures + "/" + this.ProductSrc);
	}
	public Product(Product Oldproduct)
	{
		this(Oldproduct.ProductCode,
			Oldproduct.getProductName(),
			Oldproduct.getProductDescription(),
			Oldproduct.getProductSrc(),
			Oldproduct.getProductPrice(), 
			Oldproduct.MaxAmount);
		this.setPriceStategy(Oldproduct.PriceStategy);
		this.setAmount(Oldproduct.getProductAmount());
	}
	public String getPathPicture()
	{
		return String.format("file:///%s", PicturePhoto.getAbsolutePath());
	}
	public IProductPrice getPriceStategy() {
		return PriceStategy;
	}
	public void setPriceStategy(IProductPrice priceStategy) {
		PriceStategy = priceStategy;
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
	public Integer getProductCode() {
		return ProductCode;
	}
	public void setProductCode(Integer productCode) {
		ProductCode = productCode;
	}
	public Double getProductPrice() {
		return ProductPrice;
	}
	public void setAmount(int Amount)
	{
		ProductAmount = Amount;
	}
	public int getMaxAmount() {
		return MaxAmount;
	}
	public void setMaxAmount(int maxAmount) {
		MaxAmount = maxAmount;
	}
	@Override
	public String toString() {
		return ProductName;
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
