
CREATE SCHEMA `ekrutdatabase`;

CREATE TABLE `ekrutdatabase`.Users (
firstName varchar(30),
lastName varchar(30),
telephone varchar(10),
Email varchar(50),
ID varchar(10),
userName varchar(20) PRIMARY KEY NOT NULL,
userPassword varchar(20) NOT NULL,
userOnline ENUM("Online","Offline")
);


CREATE TABLE `ekrutdatabase`.Employees (
	Employeerole enum('AreaDeliveryOperator','Provider','AreaManager','NetworkEmployee','NetworkMarketingManager','CEO') NOT null,
	userName varchar(20),
	branch varchar(50),
	PRIMARY KEY (userName),
    FOREIGN KEY (userName) REFERENCES Users(userName)
);

CREATE TABLE `ekrutdatabase`.RegisterClients (
	userName varchar(20),
    userStatus ENUM("APPROVED","NOT_APPROVED","PENDING"),
	PRIMARY KEY (userName),
    FOREIGN KEY (userName) REFERENCES Users(userName)
);

CREATE TABLE `ekrutdatabase`.Subscriber (
	userName varchar(20),
	CardNumber varchar(20),
    CardDate varchar(5),
    SubscriberNumber int NOT NULL,
    MonthlyFeeCharge int,
    firstPurchase boolean,
	PRIMARY KEY (userName),
    FOREIGN KEY (userName) REFERENCES Users(userName)
);

CREATE TABLE `ekrutdatabase`.Facilities(
	FacilityID int not null auto_increment primary key,
    FacilityArea ENUM("North","South","UAE"),
    FacilityLocation varchar(30),
    FacilityName varchar(20),
    FacilityThreshholdLevel int,
    FacilityEK boolean
);

CREATE TABLE `ekrutdatabase`.Orders (
	orderCode int NOT NULL auto_increment PRIMARY KEY,
    finalPrice double,
    isInvoiceConfirmed boolean,
    FacilityID int,
    userName varchar(20),
    orderdate varchar(40),
    FOREIGN KEY (FacilityID) REFERENCES Facilities(FacilityID),
    FOREIGN KEY (userName) REFERENCES Users(userName)
);


CREATE TABLE `ekrutdatabase`.VirtualOrders (
	orderCode int,
	HasDelivery boolean,
    DeliveryLocation varchar(30),
    DeliveryStatus ENUM("Received","Dispatched","Done"),
    customerApproval int,
    estimatedDateAndTime varchar(30),
    PRIMARY KEY (orderCode),
	FOREIGN KEY (orderCode) REFERENCES Orders(orderCode)
);
CREATE TABLE `ekrutdatabase`.DelayedPayments(
	orderDate varchar(30),
    orderCode int,
    FOREIGN KEY (orderCode) REFERENCES Orders(orderCode)
);

CREATE TABLE `ekrutdatabase`.Products(
	ProductCode int not null auto_increment primary key,
    ProductName varchar(20),
    ProductPrice double,
    ProductDescription varchar(30),
    ProductSrc varchar(20),
    ProductPicture LONGBLOB
);


CREATE TABLE `ekrutdatabase`.ProductsInFacility(
	ProductCode int,
    ProductAmount int,
    FacilityID int,
    HasReachedThreshholdLevel boolean,
	PRIMARY KEY (FacilityID,ProductCode),
	FOREIGN KEY (FacilityID) REFERENCES Facilities(FacilityID),
    FOREIGN KEY (ProductCode) REFERENCES Products(ProductCode)
);

CREATE TABLE `ekrutdatabase`.ProductsInOrder(
	orderCode int,
	ProductCode int,
    FacilityID int,
    ProductAmount int,
    hasReachedThreshholdLevel boolean,
	PRIMARY KEY (orderCode,ProductCode, FacilityID),
	FOREIGN KEY (orderCode) REFERENCES Orders(orderCode),
    FOREIGN KEY (ProductCode) REFERENCES products(ProductCode),
    FOREIGN KEY (facilities) REFERENCES products(FacilityID)
);
