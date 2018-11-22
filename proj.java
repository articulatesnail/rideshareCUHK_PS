
import java.util.Scanner;
import java.sql.*;
import java.io.*;
import java.util.Date;
import java.util.ArrayList;

//import au.com.bytecode.opencsv.CSVReader;

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
        
            System.out.println("Please enter your ID.");
            int passID =  Integer.parseInt(scan.nextLine()) ;     
        try{         
                Statement stmt = mySQLDB.createStatement();
                ResultSet rs1 = stmt.executeQuery("SELECT * FROM Passengers WHERE id = " + passID);
                    if(rs1.next()) {
                        ResultSet matchVehicleRs = stmt.executeQuery("SELECT * FROM Requests WHERE passenger_id = " + passID);
                        if(matchVehicleRs.next()){
                            System.out.println("You already have a pending request");
                            return;
                        }
                    }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        System.out.println("Please enter the number of passengers.");
            int noPassengers =  Integer.parseInt(scan.nextLine());              //on empty input  (enter), jumps back to main menu

        System.out.println("Please enter the earliest model year. (Press enter to skip)");
            earliestYear = Integer.parseInt(scan.nextLine());

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
        preparedStmt.setBoolean(6, true);
        
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

        //return available driver count
        
    }

    public static void passCheckTrip(Connection mySQLDB)throws SQLException{

    }

    public static void passRateTrip(Scanner scan, Connection mySQLDB)throws SQLException{

    }
    //////////////////////////////////////////////////////////////////////////////////////// Start of Driver methods
   
    public static void driverTakeRide(Scanner scan, Connection mySQLDB)throws SQLException{
        System.out.println("Please enter your ID.");
        int dID =  Integer.parseInt(scan.nextLine()) ;  

    try{         
            Statement stmt0 = mySQLDB.createStatement();
            ResultSet rs0 = stmt0.executeQuery("SELECT * FROM Drivers WHERE id = " + dID);
                if(rs0.next()) {                                                            //if result set has a value (driver_id exists), then proceed
                    try{         
                        Statement stmt = mySQLDB.createStatement();
                        ResultSet rs1 = stmt.executeQuery("SELECT * FROM Trips WHERE driver_id = " + dID + "AND end = NULL");
                            if(rs1.next()) {
                                System.out.println("You already have an unfinished trip");
                                return;
                                }
                            else{
            
                                try{
                                    String query1="CREATE OR REPLACE VIEW temp1 AS "+
                                                  " SELECT * FROM Requests "+
                                                  " WHERE taken = false ";
                                    PreparedStatement stmt1 = mySQLDB.prepareStatement(query1);
                                    stmt1.executeUpdate();
                                    String query2 = "SELECT temp1.id, Passengers.Name, temp1.passengers "+ 
                                                    "FROM temp1, Passengers, Drivers, Vehicles "+ 
                                                    "WHERE temp1.passengers <= Vehicles.seats "+ 
                                                    "AND temp1.model_year>=Vehicles.model_year "+ 
                                                    "AND temp1.model LIKE %Vehicles.model%";
                                    PreparedStatement stmt2 = mySQLDB.prepareStatement(query2);
                                    PreparedStatement stmt3 = mySQLDB.prepareStatement("DROP VIEW temp1");
                                    ResultSet rs2 = stmt2.executeQuery();
                                    stmt3.executeUpdate();
                                    System.out.printf("Request ID, Passenger name, Passengers \n");
                                    while(rs2.next()){
                                        for (int i = 1; i <= 3; i++){
                                            System.out.printf("," + rs2.getString(i));
                                        }    
                                        System.out.printf("|\n");
                                    }
                                    stmt.close();
                                    rs2.close();
                                    stmt2.close();
                                }catch (SQLException e){
                                    System.out.println(e.getMessage());
                                }
                                System.out.println("Please enter the request ID");
                                int reqID =  Integer.parseInt(scan.nextLine()) ;  //req id not used
            
                                
                            }
                            
                    } catch (SQLException e) {
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
        
        try{
			System.out.println("Please enter your ID.");
            int dID =  Integer.parseInt(scan.nextLine()) ;
			 // parse the SNum obtained to integer
            PreparedStatement stmt1 = mySQLDB.prepareStatement("SELECT COUNT(*) FROM Trips WHERE dID = ? AND End = NULL");
			stmt1.setInt(1, dID);
            ResultSet rs = stmt1.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			rs.close();
            stmt1.close();
            
			if (count == 1){
                PreparedStatement stmt3 = mySQLDB.prepareStatement("SELECT id, Passengerid, Start FROM Trips WHERE dID = ? AND End = NULL");
                ResultSet rs2 = stmt3.executeQuery();
                System.out.println("Trip ID, Passenger ID, Start");
                while(rs2.next()){
                    for (int i = 1; i <= 3; i++){
                        System.out.printf("," + rs2.getString(i));
                    }    
                    System.out.printf("\n");
                }
                rs2.close();
                System.out.println("Do you wish to finish the trip?");
                String str1 = scan.nextLine();

                if (str1=="y" || str1 == "Y"){
                    PreparedStatement stmt2 = mySQLDB.prepareStatement("INSERT INTO Trips VALUES (?,?,?,?,CURDATE()),?,? ON DUPLICATE KEY UPDATE End = CURDATE()");
				    stmt2.setInt(1, dID);
				    stmt2.setDate(5, java.sql.Date.valueOf("2012-12-12")); 
                    stmt2.executeUpdate();
                    stmt2.close();
                    String query4 ="SELECT Trips.id, Passengers.id, Trips.Start, Trips.End, Trips.Fee "+ 
                                   "FROM Trips, Passengers "+
                                   "WHERE dID = Trips.Driverid AND Trips.Passengerid=Passengers.id";
                    PreparedStatement stmt4 = mySQLDB.prepareStatement(query4);
                    ResultSet rs3 = stmt4.executeQuery();
                    System.out.println("Trip ID, Passenger Name, Start, End, Fee");
                    while(rs3.next()){
                    for (int i = 1; i <= 5; i++){
                        System.out.printf("," + rs3.getString(i));
                    }    
                    System.out.printf("\n");
                }
                rs3.close();
				    
                }
				
			}
			else{
				System.out.printf("Error when returning spacecraft!\n");
			}

		}catch (SQLException e){
			System.out.println("Requested trip does not exist in database");
		}

    }

    public static void driverRating(Scanner scan, Connection mySQLDB)throws SQLException{
        System.out.println("Please enter your ID.");
        int dID =  Integer.parseInt(scan.nextLine()) ;

        try{
			
            PreparedStatement stmt1 = mySQLDB.prepareStatement("SELECT COUNT(*), AVG(Rating) FROM Trips WHERE Driverid=? GROUP BY Driverid  ");
			stmt1.setInt(1, dID);
            ResultSet rs = stmt1.executeQuery();
			rs.next();
            int count = rs.getInt(1);
            float driverrating= rs.getFloat(2);
			
            
			if (count >= 5){

    
            }

            rs.close();
            stmt1.close();
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
             "PRIMARY KEY (id));";  
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
             "passengerid INT(16), " +
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
              "taken BOOLEAN, " +
              "PRIMARY KEY (id));";         
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
        // while(true){
        //     String filePath = "";

        //     System.out.print("Please enter the folder path.");
        //     filePath = scan.nextLine();
		// 	if((new File(filePath)).isDirectory()) 
		// 		break;
        //     else 
		// 		System.out.printf("Please input a valid directory...");
		//     }
        try{
            Statement statement = mySQLDB.createStatement();
            
            // String addDrivers= "LOAD DATA LOCAL INFILE "+  filePath + "/drivers.csv' INTO TABLE Drivers FIELDS TERMINATED BY ',' ;";
            // String addVehicles= "LOAD DATA LOCAL INFILE "+ filePath + "/vehicles.csv' INTO TABLE Vehicles FIELDS TERMINATED BY ',' ;";
            // String addPassengers= "LOAD DATA LOCAL INFILE "+  filePath + " /passengers.csv' INTO TABLE Passengers FIELDS TERMINATED BY ',' ;";
            // String addTrips= "LOAD DATA LOCAL INFILE "+  filePath + " /trips.csv' INTO TABLE Trips FIELDS TERMINATED BY ',' ;";

            String addDrivers= "LOAD DATA LOCAL INFILE 'drivers.csv' INTO TABLE Drivers FIELDS TERMINATED BY ',' ;";
            String addVehicles= "LOAD DATA LOCAL INFILE 'vehicles.csv' INTO TABLE Vehicles FIELDS TERMINATED BY ',' ;";
            String addPassengers= "LOAD DATA LOCAL INFILE 'passengers.csv' INTO TABLE Passengers FIELDS TERMINATED BY ',' ;";
            String addTrips= "LOAD DATA LOCAL INFILE 'trips.csv' INTO TABLE Trips FIELDS TERMINATED BY ',' ;";

            // System.out.printf("Please input a valid directory...");
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
            statement.executeUpdate(dropRequests);
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
                        passCheckTrip(mySQLDB);
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
                        load(scan, mySQLDB);
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
        int mainMenu = 0;

        while(true){
			try{
				Connection mySQLDB = connectToOracle();
				if(mySQLDB == null){
					answer = "0";
					System.out.println("[Error]: Database connection failed, system exit");
                }else{
                    while (mainMenu == 0){
                    System.out.println("Welcome! Who are you?");
                    System.out.println("1. An administrator");
                    System.out.println("2. A passenger");
                    System.out.println("3. A driver");
                    System.out.println("4. Exit this program");
                    System.out.println("Enter Your Choice: ");

                        if(mainMenu==0){
                            answer = scan.nextLine();
                            create(mySQLDB);
                            load(scan, mySQLDB);
                            
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
                                return;
                            default:
                                System.out.println("[ERROR] Please enter [1-5].\n");
                        }
                    }
                }
                }
                break;
            }catch (Exception e){
				System.out.print("[Error]: ");
				System.out.println(e.getMessage());
            }
            scan.close();
            System.exit(0);
        }
    }
}
