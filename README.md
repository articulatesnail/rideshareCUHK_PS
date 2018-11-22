# rideshareCUHK_PS

Passenger menu
Driver Menu


******To logon to server ***********
ssh linux1.cse.cuhk.edu.hk -l db026
eke2kd50            //Sams pswd
mysql --host=projgw --port=2633 -u Group5 -p
ride

********Driver accept, fin, need the data object*************
      // create a sql date object so we can use it in our INSERT statement
      Calendar calendar = Calendar.getInstance();
      java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

*******Example of query for mysql SSH******
 Select * from Vehicles V
 Inner Join Drivers D 
 On D.vehicle_id=V.id 
 Where V.seats>= 7 AND model_year>=2018;

********Sample Trip info*********
1,7,71,2018-01-01 17:45:52,2018-01-01 18:40:12,54,4
2,71,114,2018-01-04 16:24:16,2018-01-04 16:55:13,30,4
3,47,57,2018-01-06 22:28:36,2018-01-06 23:02:00,33,4
4,93,2,2018-01-08 05:40:07,2018-01-08 06:27:10,47,0