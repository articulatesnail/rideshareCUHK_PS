//comment
import java.util.Scanner;
import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.Date;
import java.util.ArrayList;

//import au.com.bytecode.opencsv.CSVscan;

public class proj{

	public static String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db5";
	public static String dbUsername = "Group5";
	public static String dbPassword = "ride";
    static int requestCount = 0;
    //////////////////////////////////////////////////////////////////////////////////////// Start of Passenger methods

    public static void passRequestRide(Scanner scan, Connection mySQLDB)throws SQLException{
        int rID = requestCount++;
        int earliestYear = -1;
        String modelName="";
        
        //while condition
        System.out.println("Please enter your ID.");
        int passID =  Integer.parseInt(scan.nextLine()) ;    
    
        try{         
            Statement stmt = mySQLDB.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Passengers WHERE id = " + passID);
            if(rs.next()) {
                ResultSet matchVehicleRs = stmt.executeQuery("SELECT * FROM Requests WHERE passenger_id = " + passID);
                if(matchVehicleRs.next()){
                    System.out.println("You already have a pending request");
                    stmt.close();
                    passRequestRide(scan, mySQLDB);
                }
                        }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        // while condition
        System.out.println("Please enter the number of passengers.");
            int noPassengers =  Integer.parseInt(scan.nextLine());        
        
            // while condition        
        System.out.println("Please enter the earliest model year. (Press enter to skip)");
            String tempEY= scan.nextLine();
            if(tempEY.isEmpty()){ 
                earliestYear = 0;
            }
            else{
                earliestYear = Integer.parseInt(tempEY);
            }
            
        System.out.println("Please enter the model. (Press enter to skip)");   
            modelName = scan.nextLine();
         //Check validity of inputs   
        
        try{
        //Update requests table
        String insertQuery = "INSERT INTO Requests (id, passenger_id, model_year, model, passengers, taken)"
                            + " values (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStmt = mySQLDB.prepareStatement(insertQuery);

        preparedStmt.setInt    (1, rID);
        preparedStmt.setInt    (2, passID); // get passengerid from passengers
        preparedStmt.setInt    (3, earliestYear);
        preparedStmt.setString (4, modelName);
        preparedStmt.setInt    (5, noPassengers);
        preparedStmt.setInt    (6, 0);
        
        preparedStmt.execute();
        // System.out.print("Your request is placed.");
        // preparedStmt.close();
        }
        
        catch (SQLException e ) {
        System.out.println("An error has occured on passRequestRide");
        e.printStackTrace();
        }

        //see matching drivers/vehicles that are available
        try{
            if (passID != -1 && noPassengers!= -1) {
                Statement stmt2 = mySQLDB.createStatement();
                String query = "SELECT id FROM Vehicles WHERE Seats >= " + 
                                    String.valueOf(noPassengers);
                                    
                if(earliestYear != -1){
                        query = query + " AND model_year >= " + String.valueOf(earliestYear);
                    }
                if(!modelName.equals("")){
                        query = query + " AND model Like '%" + modelName +"%'";
                    }
                ResultSet matchVehicleRs = stmt2.executeQuery(query);

                ArrayList<String> matchVehicleArr = new ArrayList<>();
                if(!matchVehicleRs.isBeforeFirst()){
                        System.out.println("No Such Vehicle Available");
                }
                else{
                    String matchVechicleQuery = "";
                    while(matchVehicleRs.next()){
                            matchVehicleArr.add(matchVehicleRs.getString("id"));
                        }
                        for (int i =0 ; i < matchVehicleArr.size(); i++){
                            if(i == 0){
                                matchVechicleQuery = matchVechicleQuery + "('";
                            }
                                matchVechicleQuery = matchVechicleQuery + matchVehicleArr.get(i) +"', '";
                            if (i == matchVehicleArr.size()-1){
                                matchVechicleQuery = matchVechicleQuery.substring(0,matchVechicleQuery.length() - 3) + ")";
                            }
                        }
                        String query2  = "SELECT * FROM Drivers WHERE vehicle_id IN" + matchVechicleQuery;
                        Statement stmt3 = mySQLDB.createStatement();
                        ResultSet rs3 = stmt3.executeQuery(query2);
                        int count = 0;

                        while (rs3.next()) {
                            ++count;
                        }
                        System.out.println("Your request is placed. " +String.valueOf(count) + " drivers are able to take the request.");
                    }

                }
            }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void passCheckTrip(Scanner scan, Connection mySQLDB)throws SQLException{
        // while condition
        System.out.println("Please enter your ID.");
        int passID =  Integer.parseInt(scan.nextLine()) ;    
    
        try{         
            Statement stmt = mySQLDB.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM Passengers WHERE id = " + passID);
            if(rs1.next()==false) {
                System.out.println("Passenger ID doesn't exist in table") ;
                passCheckTrip(scan, mySQLDB);      
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        //enter start date no error handle for now
        String startDateInput = null;

        // while condition
        System.out.println("Please enter the start date.");
        if(scan.hasNext()){
            startDateInput = scan.nextLine();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            dateFormatter.setLenient(false);
            Date parsedStartDate = null;
            try {
                parsedStartDate = dateFormatter.parse(startDateInput);

            } catch (ParseException e) {
                System.out.println("[ERROR]: Incorrect date format. [yyyy-MM-dd] ");
                passCheckTrip(scan, mySQLDB);
            }
        }
        else{
            System.out.println("[ERROR] Start date input");
            passCheckTrip(scan, mySQLDB);
        }
        ///enter end date no error handle for now

        // while condition
        String endDateInput= null;
        System.out.println("Please enter the end date.");
        if(scan.hasNext()){
            endDateInput = scan.nextLine();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            dateFormatter.setLenient(false);
            Date parsedEndDate = null;
            try {
                parsedEndDate = dateFormatter.parse(endDateInput);
            } catch (ParseException e) {
                System.out.println("[ERROR]: Incorrect date format. [yyyy-MM-dd] ");
                passCheckTrip(scan, mySQLDB);
            }

        }
        else{
            System.out.println("[ERROR]: End date input");
            passCheckTrip(scan, mySQLDB);
        }
        // query based on inputs
        try {           
            Statement stmt1 = mySQLDB.createStatement();
            String query1= "SELECT * FROM Trips WHERE passenger_id = " 
                            + String.valueOf(passID) 
                            + " AND start >= \'" + startDateInput + "\'"
                            + " AND end <= \'"+ endDateInput + "\'"
                            + " ORDER BY start DESC";
            ResultSet rs = stmt1.executeQuery(query1);   //rs: table showing all past trips of passenger
            // System.out.println(query1);
            System.out.println("Trip ID, Driver Name, Vehicle ID, Vehicle model, Start, End, Fee, Rating");
            if(rs.next()){
                do{
                    System.out.print(rs.getInt("id")+ ", ");
                    // Statement stmt2 = mySQLDB.createStatement();
                    String query2= "SELECT * FROM Drivers WHERE id =" + String.valueOf(rs.getInt("driver_id")) ;
                    PreparedStatement preparedStmt2 = mySQLDB.prepareStatement(query2);
                    ResultSet rs2 = preparedStmt2.executeQuery(); //rs2 table showing all drivers 
                    while (rs2.next()){
                        System.out.print(rs2.getString("name") + ", ");
                        System.out.print(rs2.getString("vehicle_id") + ", ");
                    }
                    System.out.print(rs.getString("start") + ", ");
                    System.out.print(rs.getString("end") + ", ");
                    System.out.println(rs.getString("rating"));
                }
                while(rs.next());
            }
            else{
                System.out.println("No matching trips.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void passRateTrip(Scanner scan, Connection mySQLDB)throws SQLException{

        // while condition
        System.out.println("Please enter your ID.");
        int passID =  Integer.parseInt(scan.nextLine()) ;    
        try{         
            Statement stmt1 = mySQLDB.createStatement();
            ResultSet rs1 = stmt1.executeQuery("SELECT * FROM Passengers WHERE id = " + passID);
            if(rs1.next()==false) {
                System.out.println("Passenger ID doesn't exist in table") ;
                stmt1.close();
                passRateTrip(scan, mySQLDB);      
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        // while condition
        System.out.println("Please enter the trip ID.");
        int tripID =  Integer.parseInt(scan.nextLine()) ;    
    
        try{         
            Statement stmt2 = mySQLDB.createStatement();
            ResultSet rs2= stmt2.executeQuery("SELECT * FROM Trips WHERE id = " + tripID 
                                              +" AND passenger_id =" + passID
                                              +" AND end IS NOT NULL");
            if(rs2.next()==false) {
                System.out.println("Trip ID invalid. Your completed trip ID not found.") ;
                stmt2.close();
                passRateTrip(scan, mySQLDB);      
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Please enter the rating.");
        int rating =  Integer.parseInt(scan.nextLine()); 
        
        try{
            //Update rating in Trips table
            String insertQuery = "UPDATE Trips SET rating =" + rating 
                                +" WHERE id = " + tripID;
            PreparedStatement preparedStmt = mySQLDB.prepareStatement(insertQuery);
    
            preparedStmt.execute();
            preparedStmt.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

        try{
            String qT = "SELECT * FROM Trips WHERE id =" + tripID;
            Statement stmt3 = mySQLDB.createStatement();
            ResultSet rs3=stmt3.executeQuery(qT);
            System.out.println("Trip ID, Driver Name, Vehicle ID, Vehicle model, Start, End, Fee, Rating");
            if(rs3.next()){ 
				System.out.println(rs3.getString(1)+","+rs3.getString(2)+","+rs3.getString(3)+","+rs3.getString(4)+","+rs3.getString(5)+","+rs3.getString(6)+","+rs3.getString(7));
            }
            stmt3.close();
        }    
        catch (SQLException e) {
            e.printStackTrace();
        }
        }
    
    //////////////////////////////////////////////////////////////////////////////////////// Start of Driver methods
   
    public static void driverTakeRide(Scanner scan, Connection mySQLDB)throws SQLException{
        System.out.println("Please enter your ID.");            ///Enter Driver ID
        int dID =  Integer.parseInt(scan.nextLine()) ;  

        try{         
            Statement stmt0 = mySQLDB.createStatement();
            ResultSet rs0 = stmt0.executeQuery("SELECT * FROM Drivers WHERE id = " + dID);    //Check if id entered is in drivers
            if(rs0.next()) {                                                            //if result set has a value (driver_id exists), then proceed
    
                PreparedStatement stmt6 = mySQLDB.prepareStatement("SELECT Vehicles.model FROM Vehicles, Drivers WHERE Drivers.id="+ dID+ " AND Drivers.vehicle_id = Vehicles.id ");
                
                ResultSet rs6 = stmt6.executeQuery();
                rs6.next();
                String model_request = rs6.getString(1);
                rs6.close();
                stmt6.close();

                try{
                    //String query1 = "CREATE OR REPLACE VIEW temp1 AS "
                                // +"SELECT * FROM Requests "
                                //+"WHERE taken = false ";
                    ///PreparedStatement stmt1 = mySQLDB.prepareStatement(query1);
                    ///stmt1.executeUpdate();
                    // System.out.println("WHY ARENTU PRINT");
                    String query3 = "SELECT Requests.id, Passengers.name, Requests.passengers " 
                                    + "FROM Requests, Passengers, Drivers, Vehicles " 
                                    + "WHERE Requests.passengers <= Vehicles.seats "
                                    + "AND Requests.passenger_id = Passengers.id "  
                                    + "AND Requests.model_year <= Vehicles.model_year " 
                                    + "AND Vehicles.model LIKE '%"+ model_request +"%' "
                                    + "AND Drivers.id="+ dID
                                    + " AND Vehicles.id = Drivers.vehicle_id"
                                    + " AND Requests.taken = 0"
                                    ;
                    
                    System.out.println(query3);
                                
                    PreparedStatement stmt2 = mySQLDB.prepareStatement(query3);
                    //PreparedStatement stmt3 = mySQLDB.prepareStatement("DROP VIEW Requests");
                    ResultSet rs2 = stmt2.executeQuery();
    
                    boolean empty = true;
                    while(rs2.next() ) {
                    // ResultSet processing here
                        empty = false;
                    }
                    if( empty ) {
                        System.out.println("No available requests!");
                        driverMenu(mySQLDB);
                    }
                    // ResultSet rs2a =  stmt2.executeQuery();
                    
                    rs2.first();
                    System.out.printf("Request ID, Passenger name, Passengers \n");
                    do{
                        System.out.printf(rs2.getInt(1) + "," + rs2.getString(2) + "," + rs2.getInt(3));
                        System.out.printf("\n");
                    }
                    while(rs2.next());

                    stmt6.close();
                    stmt0.close();
                    stmt2.close();
                    
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
                System.out.println("Please enter the request ID.");
                
                int reqID =  Integer.parseInt(scan.nextLine()) ;  //req id not used
                //insert new instance of trips, adds new start time
                
                //update request to taken
                try{
                    String updateQuery = "UPDATE Requests SET taken = 1 WHERE id = " + reqID;
                    PreparedStatement preparedStmt1 = mySQLDB.prepareStatement(updateQuery);
                    preparedStmt1.execute();
                    preparedStmt1.close();
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    } 

                //get request.passid
                String query4 ="SELECT passenger_id FROM Requests WHERE id = "+ reqID; 
                PreparedStatement preparedStmt3 = mySQLDB.prepareStatement(query4);
                ResultSet rs3 = preparedStmt3.executeQuery();
                rs3.next();
                int passenger_id = rs3.getInt(1);

                //query for max count
                String query5 ="SELECT MAX(id) FROM Trips";
                PreparedStatement preparedStmt5 = mySQLDB.prepareStatement(query5);
                ResultSet rs5= preparedStmt5.executeQuery();
                rs5.next();
                int newTripID = rs5.getInt(1) + 1 ; 

                try{
                    //Update requests table

                    String insertQuery = "INSERT INTO Trips (id, driver_id, passenger_id, start, rating)"
                                        + " values (?, ?, ?, NOW(), 0)";
                    PreparedStatement preparedStmt4 = mySQLDB.prepareStatement(insertQuery);
                    
                    preparedStmt4.setInt    (1, newTripID); 
                    preparedStmt4.setInt    (2, dID); // get passengerid from passengers
                    preparedStmt4.setInt    (3, passenger_id);
                    
                    preparedStmt4.execute();
                    
                    }
                    
                    catch (SQLException e ) {
                    System.out.println("An error has occured on passRequestRide");
                    e.printStackTrace();
                    }

                }
                else{
                    System.out.println("[ERROR]: Driver ID doesn't exist!");
                    driverTakeRide(scan, mySQLDB);                              //call method again
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void driverFinishTrip(Scanner scan, Connection mySQLDB)throws SQLException{
        int minFee=0;   
        try{
			System.out.println("Please enter your ID.");
            int dID =  Integer.parseInt(scan.nextLine()) ;
			 // parse the SNum obtained to integer
            PreparedStatement stmt1 = mySQLDB.prepareStatement("SELECT COUNT(*) FROM Trips WHERE driver_id=" + dID + " AND end IS NULL");

            ResultSet rs = stmt1.executeQuery();
			rs.next();
			int countUnfinished = rs.getInt(1);
			rs.close();
            stmt1.close();
            
			if (countUnfinished == 1){
                PreparedStatement stmt3 = mySQLDB.prepareStatement("SELECT id, passenger_id, start FROM Trips WHERE driver_id = "
                                                                    + dID + " AND end IS NULL");
                ResultSet rs2 = stmt3.executeQuery();
                System.out.println("Trip ID, Passenger ID, Start");
                while(rs2.next()){
                    System.out.printf(rs2.getString(1) + "," + rs2.getString(2) + "," + rs2.getString(3));
                    System.out.printf("\n");
                }
                System.out.println("Do you wish to finish the trip? [y/n]");
                String str1 = scan.nextLine(); //error handling

                if (str1.equals("y") | str1.equals("Y")){   //compare
                    // System.out.println("condition met");
                    String query4 ="SELECT Trips.id, Passengers.name, Trips.start, Trips.end, Trips.fee "  //get driver's past trips
                                    + "FROM Trips, Passengers "
                                    + "WHERE " + dID + " = Trips.driver_id "
                                    + "AND Trips.passenger_id = Passengers.id";
                    PreparedStatement stmt4 = mySQLDB.prepareStatement(query4);
                    ResultSet rs3 = stmt4.executeQuery();
                    
                    try{
                        System.out.println("update cur time");
                        rs2.first();
                        String insertQuery2 = "UPDATE Trips SET end = NOW() WHERE id = " + rs2.getString("id");  //update end to current time
                        System.out.println(rs2.getString("id"));
                        PreparedStatement preparedStmt = mySQLDB.prepareStatement(insertQuery2);
                        preparedStmt.execute();
                        preparedStmt.close();
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                        } 
                    ////get time diff in minutes
                    try{
                        System.out.println("select get diff");
                        String insertQuery3 = "SELECT TIMESTAMPDIFF(MINUTE, start, end) FROM Trips";               //get difference in
                        PreparedStatement preparedStmt3 = mySQLDB.prepareStatement(insertQuery3);
                        ResultSet rs5 = preparedStmt3.executeQuery();
                        rs5.next();
                        minFee = rs5.getInt(1);        // cannot finr minFee symbol
                        // System.out.println(minFee);
                        // preparedStmt3.close();
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                        }     

                    try{
                        System.out.println("update trips");
                        String insertQuery = "UPDATE Trips SET fee =" + minFee + " WHERE id = " + rs2.getString("id");
                        PreparedStatement preparedStmt4 = mySQLDB.prepareStatement(insertQuery);
                        preparedStmt4.execute();
                        System.out.println("updated fee");
                        // preparedStmt4.close();
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                        } 
                    //calc fee and insert to table  
                    
                    int updatedTripID = rs2.getInt(1); 

                    String query9 ="SELECT Trips.id, Passengers.name, Trips.start, Trips.end, Trips.fee "  //get driver's past trips
                    + "FROM Trips, Passengers "
                    + "WHERE " + dID + " = Trips.driver_id "
                    + "AND Trips.passenger_id = Passengers.id "
                    + "AND Trips.id = " + updatedTripID;

                    PreparedStatement stmt9 = mySQLDB.prepareStatement(query9);
                    ResultSet rs9 = stmt9.executeQuery();
                    
                    System.out.println("Trip ID, Passenger Name, Start, End, Fee");
                    while(rs9.next()){
                        System.out.printf(rs9.getString(1) + "," + rs9.getString(2) + "," + rs9.getString(3)+ "," + rs9.getString(4)+ "," + rs9.getString(5));
                        System.out.printf("\n");
                    }
                    rs3.close();
                    rs2.close();
                }	
			}
			else{
				System.out.printf("Error when returning trip!\n");
			}

		}catch (SQLException e){
			e.printStackTrace();
		}

    }

    public static void driverRating(Scanner scan, Connection mySQLDB)throws SQLException{
        System.out.println("Please enter your ID."); //passid
        int dID =  Integer.parseInt(scan.nextLine()) ;

        try{
            String query1 = "Select Count(*) FROM Trips where driver_id= "+ dID + " AND rating != 0";
            PreparedStatement stmt1 = mySQLDB.prepareStatement(query1);
            ResultSet rs1 = stmt1.executeQuery();
            rs1.next();
            int tripCount = rs1.getInt(1);
            rs1.close();
            stmt1.close();
            
			if (tripCount >= 5){
                String query2 = "Select ROUND(AVG (rating), 3) FROM (SELECT rating FROM Trips where driver_id = " + dID + " AND rating != 0 ORDER BY end DESC LIMIT 5) AS X";
                PreparedStatement stmt2 = mySQLDB.prepareStatement(query2);
                ResultSet rs2 = stmt2.executeQuery();
                rs2.next();
                float avgRating = rs2.getFloat(1);
                System.out.println("Your driver rating is "+ avgRating + ".");
                rs2.close();
                stmt2.close();
            }
            else{
                System.out.println("Not enough rated trips!");
            }

        }catch (SQLException e) {
        e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////// Start of Misc methods(can remove)

    public static void displayRequests(Connection mySQLDB)throws SQLException { //prints requests records
		try{
			Statement stmt=mySQLDB.createStatement();

			String qR="Select * from Requests";
        
			//to execute query
			ResultSet rs=stmt.executeQuery(qR);
			
			//to print the resultset on console
			if(rs.next()){ 
				do{
                    System.out.println(rs.getString(1)+","+rs.getString(2)+","+rs.getString(3)+","+rs.getString(4)+","+rs.getString(5)+","+rs.getString(6));
				}while(rs.next());
			}
			else{
				System.out.println("Record Not Found...");
            }   
			stmt.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

    public static void displayAll(Connection mySQLDB)throws SQLException { //prints all recordss
		try{
			Statement stmt=mySQLDB.createStatement();
			//query to display all records from table employee
			String qD="Select * from Drivers";
            String qV="Select * from Vehicles";
            String qP="Select * from Passengers";
            String qT="Select * from Trips";
			//to execute query
			ResultSet rs=stmt.executeQuery(qD);
			
			//to print the resultset on console
			if(rs.next()){ 
				do{
				System.out.println(rs.getString(1)+","+rs.getString(2)+","+rs.getString(3));
				}while(rs.next());
			}
			else{
				System.out.println("Record Not Found...");
            }
            
            rs=stmt.executeQuery(qV);

            if(rs.next()){ 
				do{
				System.out.println(rs.getString(1)+","+rs.getString(2)+","+rs.getString(3)+","+rs.getString(4));
				}while(rs.next());
			}
			else{
				System.out.println("Record Not Found...");
            }
            rs=stmt.executeQuery(qP);
            if(rs.next()){ 
				do{
				System.out.println(rs.getString(1)+","+rs.getString(2));
				}while(rs.next());
            }
			else{
				System.out.println("Record Not Found...");
            }
            rs=stmt.executeQuery(qT);
            if(rs.next()){ 
				do{
				System.out.println(rs.getString(1)+","+rs.getString(2)+","+rs.getString(3)+","+rs.getString(4)+","+rs.getString(5)+","+rs.getString(6)+","+rs.getString(7));
				}while(rs.next());
			}
			else{
				System.out.println("Record Not Found...");
            }
			stmt.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
        //////////////////////////////////////////////////////////////////////////////////////// Start of admin methods

    private static void create(Connection mySQLDB)throws SQLException {              //Create tables
        delete(mySQLDB);
        String driverTableQuery=  
            "CREATE TABLE Drivers" + 
             "(id INT(16), " +  
             "name VARCHAR(30), " +
             "vehicle_id VARCHAR(6), " +
             "PRIMARY KEY (id))";  
        String vehicleTableQuery= "CREATE TABLE Vehicles" + 
             "(id VARCHAR(6), " +  
             "model VARCHAR(30), " +
             "model_year INT(16), " +
             "seats INT(4), " +
             "PRIMARY KEY (id))";  
        String passengerTableQuery= "CREATE TABLE Passengers" + 
             "(id INT(16), " +  
             "name VARCHAR(30), " +
             "PRIMARY KEY (id))";  
        String tripsTableQuery= "CREATE TABLE Trips" + 
             "(id INT(16), " +  
             "driver_id INT(16), " +
             "passenger_id INT(16), " +
             "start VARCHAR(20)," +
             "end VARCHAR(20)," +
             "fee INT(16), " +
             "rating INT(16), " +
             "PRIMARY KEY (id))";
        String requestsTableQuery=  
             "CREATE TABLE Requests" + 
              "(id INT(16), " +  
              "passenger_id INT(32), " +
              "model_year INT(4), " +
              "model VARCHAR(32), " +
              "passengers INT(8), " +
              "taken INT(2) DEFAULT 0, " +
              "PRIMARY KEY (id))";         
        try {
        Statement statement = mySQLDB.createStatement();
        System.out.print("Processing...");

       // statement.executeUpdate(dropTableQuery);
        statement.executeUpdate(driverTableQuery);
        //System.out.println("Driver Table Created1");

        //statement.executeUpdate(dropTable1Query);
        statement.executeUpdate(vehicleTableQuery);
        //System.out.println("Vehicle Table Created1");

     //   statement.executeUpdate(dropTable2Query);
        statement.executeUpdate(passengerTableQuery);
        //System.out.println("Passenger Table Created1");

       // statement.executeUpdate(dropTable3Query);
        statement.executeUpdate(tripsTableQuery);
        statement.executeUpdate(requestsTableQuery);

        System.out.println("Done! Tables are created!");
        statement.close();
        }
    
        catch (SQLException e ) {
        System.out.println("An error has occured on Table Creation");
        e.printStackTrace();
        }
    }

    private static void load(Scanner scan, Connection mySQLDB) throws SQLException{
        String filePath = "";
        while(true){
            System.out.println("Please enter the folder path.");
            filePath = scan.nextLine();
			if((new File(filePath)).isDirectory()) 
				break;
            else 
				System.out.printf("Please input a valid directory...");
		    }
        try{
            Statement statement = mySQLDB.createStatement();
            
            String addDrivers= "LOAD DATA LOCAL INFILE \'"+  filePath + "/drivers.csv' INTO TABLE Drivers FIELDS TERMINATED BY ',' ;";
            String addVehicles= "LOAD DATA LOCAL INFILE \'"+ filePath + "/vehicles.csv' INTO TABLE Vehicles FIELDS TERMINATED BY ',' ;";
            String addPassengers= "LOAD DATA LOCAL INFILE \'"+  filePath + "/passengers.csv' INTO TABLE Passengers FIELDS TERMINATED BY ',' ;";
            String addTrips= "LOAD DATA LOCAL INFILE \'"+  filePath + "/trips.csv' INTO TABLE Trips FIELDS TERMINATED BY ',' ;";

            // String addDrivers= "LOAD DATA LOCAL INFILE 'drivers.csv' INTO TABLE Drivers FIELDS TERMINATED BY ',' ;";
            // String addVehicles= "LOAD DATA LOCAL INFILE 'vehicles.csv' INTO TABLE Vehicles FIELDS TERMINATED BY ',' ;";
            // String addPassengers= "LOAD DATA LOCAL INFILE 'passengers.csv' INTO TABLE Passengers FIELDS TERMINATED BY ',' ;";
            // String addTrips= "LOAD DATA LOCAL INFILE 'trips.csv' INTO TABLE Trips FIELDS TERMINATED BY ',' ;";

            System.out.print("Processing...");
            
            statement.executeUpdate(addDrivers);            
            statement.executeUpdate(addVehicles);
            statement.executeUpdate(addPassengers);
            statement.executeUpdate(addTrips);

            System.out.print("Data is loaded!\n");
            statement.close();
            }
        
            catch (SQLException e ) {
            System.out.println("An error has occured on Table Loading");
            e.printStackTrace();
            }
    }

    private static void delete(Connection mySQLDB)throws SQLException{
        String dropDriver= "DROP TABLE IF EXISTS Drivers;"; 
        String dropVehicles= "DROP TABLE IF EXISTS Vehicles;"; 
        String dropPassenger= "DROP TABLE IF EXISTS Passengers;"; 
        String dropTrips= "DROP TABLE IF EXISTS Trips;";
        String dropRequests= "DROP TABLE IF EXISTS Requests;"; 

        try{
            Statement statement = mySQLDB.createStatement();
            statement.executeUpdate(dropDriver);
            //System.out.println("Driver Table Deleted");
            statement.executeUpdate(dropVehicles);
            //System.out.println("Vehicles Table Deleted");
            statement.executeUpdate(dropPassenger);
            //System.out.println("Passengers Table Deleted");
            statement.executeUpdate(dropTrips);
            //System.out.println("Trips Table Deleted")
            statement.executeUpdate(dropRequests);    //not drop requests
            System.out.println("All Tables Deleted");
            statement.close();
        }
        catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
         }

    }

    public static void check(Scanner scan, Connection mySQLDB) throws SQLException{
        String[] table_name = {"Drivers", "Vehicles", "Passengers", "Trips"};
    
        System.out.println("Number of records in each table:");
            for (int i = 0; i < 4; i++){
                Statement stmt  = mySQLDB.createStatement();
                ResultSet rs = stmt.executeQuery("select count(*) from "+table_name[i]);   //add if table exists
    
                rs.next();
                System.out.println(table_name[i]+": "+rs.getString(1));
                rs.close();
                stmt.close();
            }
    }
      //////////////////////////////////////////////////////////////////////////////////////// Start of menu methods
  
    public static void adminMenu(Connection mySQLDB)throws SQLException {
        int adminMenuStatus = 1; 
        Scanner scan = new Scanner(System.in);

        while(adminMenuStatus == 1){
            System.out.println("Administrator, what would you like to do?");
            System.out.println("1. Create tables");
            System.out.println("2. Delete tables");
            System.out.println("3. Load data");
            System.out.println("4. Check Data");
            System.out.println("5. Go back");
            System.out.println("6. Display provided tables");
            System.out.println("7. Display Requests table");
            System.out.println("Please enter [1-5].");
            String str = scan.nextLine();
            try{
                int answer = Integer.parseInt(str);
                switch(answer){

                    case 1:
                        create(mySQLDB);
                        break;
                    case 2:
                        delete(mySQLDB);
                        break;
                    case 3:
                        load(scan, mySQLDB);
                        break;
                    case 4:          
                        check(scan, mySQLDB);
                        break;  
                    case 5:
                        adminMenuStatus = 0; 
                        return;
                    case 6:
                        displayAll(mySQLDB);
                        break;
                    case 7:
                        displayRequests(mySQLDB);
                        break;
                    default:
                        System.out.print("[ERROR] Please enter [1-5].");
                        break;
            }
        }
        catch(NumberFormatException e){
            System.out.println("[ERROR] Please enter [1-5].\n");
        }
        }
        scan.close();
        return;
    }

    public static void passMenu(Connection mySQLDB)throws SQLException {
        int passMenuStatus = 1; 
        Scanner scan = new Scanner(System.in);

        while(passMenuStatus == 1){
            System.out.println("Passenger, what would you like to do?");
            System.out.println("1. Request a ride");
            System.out.println("2. Check trip records");
            System.out.println("3. Rate a trip");
            System.out.println("4. Go back");
            System.out.println("Please enter [1-4].");
            String str = scan.nextLine();
            try{
                int answer = Integer.parseInt(str);
                switch(answer){
                    case 1:
                        passRequestRide(scan, mySQLDB);
                        break;
                    case 2:
                        passCheckTrip(scan, mySQLDB);
                        break;
                    case 3:
                        passRateTrip(scan, mySQLDB);
                        break;
                    case 4:
                        passMenuStatus = 0; 
                        return;
                    default:
                        System.out.println("[ERROR] Please enter [1-4].");
                        break;
            }
        }
        catch(NumberFormatException e){
            System.out.println("[ERROR] Please enter [1-4].\n");
        }
        }
        scan.close();
        return;
    }

    public static void driverMenu(Connection mySQLDB)throws SQLException {
        int driverMenuStatus = 1; 
        Scanner scan = new Scanner(System.in);

        while(driverMenuStatus == 1){
            System.out.println("Driver, what would you like to do?");
            System.out.println("1. Take a request");
            System.out.println("2. Finish a trip");
            System.out.println("3. Check driver rating");
            System.out.println("4. Go back");
            System.out.println("Please enter [1-4].");
            String str = scan.nextLine();
            try{
                int answer = Integer.parseInt(str);
                switch(answer){
                    case 1:
                        driverTakeRide(scan, mySQLDB);
                        break;
                    case 2:
                        driverFinishTrip(scan, mySQLDB);
                        break;
                    case 3:
                        driverRating(scan, mySQLDB);
                        break;
                    case 4:
                        driverMenuStatus = 0; 
                        return;
                    default:
                        System.out.print("[ERROR] Please enter [1-4].");
                        break;
            }
        }
        catch(NumberFormatException e){
            System.out.println("[ERROR] Please enter [1-4].\n");
        }
        }
        scan.close();
        return;
    }

	public static Connection connectToOracle(){
		Connection con = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
		} catch (ClassNotFoundException e){
			System.out.println("[Error]: Java MySQL DB Driver not found!!");
			System.exit(0);
		} catch (SQLException e){
			System.out.println(e);
		}
		return con;
	}
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
        String answer;
        int mainMenu = 1;
        Connection mySQLDB = connectToOracle();
        while(true){
			try{
				if(mySQLDB == null){
					answer = "0";
					System.out.println("[Error]: Database connection failed, system exit");
                }else{
                    while (mainMenu == 1){
                    System.out.println("Welcome! Who are you?");
                    System.out.println("1. An administrator");
                    System.out.println("2. A passenger");
                    System.out.println("3. A driver");
                    System.out.println("4. Exit this program");
                    System.out.println("Enter Your Choice: ");

                        if(mainMenu==1){
                            answer = scan.nextLine();
                            // create(mySQLDB);                //just so no errors if right to passenger. DELETE LATER
                            // load(scan, mySQLDB);
						switch (answer) { 
							case "1":
								adminMenu(mySQLDB);
								break;
							case "2":
                                passMenu(mySQLDB);
								break;
							case "3":
                                driverMenu(mySQLDB);
                                break;
							case "4":
                                mainMenu = 0;
                                break;
                            default:
                                System.out.println("[ERROR] Please enter [1-5].\n");
                        }
                        }
                    }
                    break;
                }
            }catch (Exception e){
				System.out.print("[Error]: ");
				System.out.println(e.getMessage());
            }
            scan.close();
            System.exit(0);
        }
    }
}
