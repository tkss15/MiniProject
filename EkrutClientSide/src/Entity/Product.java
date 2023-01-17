package Entity;

import java.io.File;
import java.util.Objects;

import common.IProductPrice;

public class Product 
{
	/**
	 *each product is being saved in our database, pictures of the product are saved in a directory and in a
	 *the DB as blobs
	 *each product has a unique product code.
	 * */
	private int ProductAmount = 1;
	private int MaxAmount;
	private Integer ProductCode;

	private String ProductName, ProductDescription, ProductSrc;
	private Double ProductPrice;
	public File PicturePhoto;
	
	private IProductPrice PriceStategy;
	
	private final String dirPictures = "EkrutApplication";
	/**
	 * @param - productCode - the code of the product
	 * @param - productName - the name of the product
	 * @param - productDescription - the description of each product
	 * @param - productSrc - the name of the file that is saved in the client
	 * @param - productPrice - the price of the product
	 * @param - MaxAmount - the maximum amount of the product
	 * @param - the strategy of the product 
	 * 
	 * */
	public Product(Integer ProductCode,String ProductName,String ProductDescription,String ProductSrc,Double ProductPrice, int MaxAmount)
	{
		this.ProductCode = ProductCode;
		this.ProductName = ProductName;
		this.ProductDescription = ProductDescription;
		this.ProductSrc = ProductSrc;
		this.ProductPrice = ProductPrice;
		this.MaxAmount = MaxAmount;
		this.PriceStategy = new PriceStartegyRegular();
		//the directory to put the product pictures
		File newDir = new File(dirPictures);
		newDir.mkdirs();
		this.PicturePhoto = new File(dirPictures + "/" + this.ProductSrc);
	}
	/**another constructor for the product, but with picture photo*/
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
	/**constructor for the pold product */
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
	/**getters and setters for the product */
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
	/**hashCode of the product*/
	@Override
	public int hashCode() {
		return Objects.hash(ProductCode);
	}
	/**override of the equals method to do a manuel equals*/
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
