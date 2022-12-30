
INSERT INTO `ekrutdatabase`.Users (firstName,lastName,telephone,Email,ID,userName,userPassword,userOnline) VALUES("Eldad","Shneor","0547443546","Eldadshneor@gmail.com","206696833","tkss15","123456","Offline");
INSERT INTO `ekrutdatabase`.Users (firstName,lastName,telephone,Email,ID,userName,userPassword,userOnline) VALUES("Jake","Sully","0549328652","JakeSully@gmail.com","205893781","jakesu192","123456","Offline");
INSERT INTO `ekrutdatabase`.Users (firstName,lastName,telephone,Email,ID,userName,userPassword,userOnline) VALUES("Amit","Malca","0538301984","amitmalca10@gmail.com","324130483","amitmalca10","123456","Offline");

INSERT INTO `ekrutdatabase`.registerclients (userName,userStatus) VALUES("tkss15","APPROVED");
INSERT INTO `ekrutdatabase`.registerclients (userName,userStatus) VALUES("jakesu192","APPROVED");
INSERT INTO `ekrutdatabase`.employees (Employeerole,userName,branch) VALUES("CEO","jakesu192",null);

INSERT INTO `ekrutdatabase`.products(ProductName,ProductPrice,ProductDescription,ProductSrc) VALUES("Orignal taste Chips", 12.90, "Orignal asf","chips.png");
INSERT INTO `ekrutdatabase`.products(ProductName,ProductPrice,ProductDescription,ProductSrc) VALUES("Cola zero", 7.90, "350mill ","cola.png");
INSERT INTO `ekrutdatabase`.products(ProductName,ProductPrice,ProductDescription,ProductSrc) VALUES("Fanta can", 5.40, "Fanta can","fanta.png");
INSERT INTO `ekrutdatabase`.products(ProductName,ProductPrice,ProductDescription,ProductSrc) VALUES("Bisli", 9.90, "Laptop of LG every 2 hours","bsili.png");

INSERT INTO `ekrutdatabase`.facilities(FacilityArea, FacilityLocation,FacilityName,FacilityThreshholdLevel, FacilityEK) VALUES ("North", "haifa","Univercity of Haifa",45, 0);
INSERT INTO `ekrutdatabase`.facilities(FacilityArea, FacilityLocation,FacilityName,FacilityThreshholdLevel, FacilityEK) VALUES ("South", "karmiel","Ort Braude College",45, 0);
INSERT INTO `ekrutdatabase`.facilities(FacilityArea, FacilityLocation,FacilityName,FacilityThreshholdLevel, FacilityEK) VALUES ("UAE", "Kirat-ata","Rogozin School",45, 0);

INSERT INTO `ekrutdatabase`.`subscriber` (`userName`, `CardNumber`, `CardDate`, `SubscriberNumber`, `MonthlyFeeCharge`, `firstPurchase`) VALUES ('tkss15', '123467', '01/20', '14', '0', '0');

INSERT INTO `ekrutdatabase`.Orders(finalPrice,isInvoiceConfirmed,FacilityID,userName) VALUES (15,1,2,"tkss15");
