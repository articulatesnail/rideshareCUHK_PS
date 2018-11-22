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

Example of query for mysql SSH******
 Select * from Vehicles V
 inner Join Drivers D 
 on D.vehicle_id=V.id 
 where V.seats>= 7 AND model_year>=2018;
