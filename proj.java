
import java.util.Scanner;
import java.sql.*;
import java.io.*;
//import au.com.bytecode.opencsv.CSVReader;

public class proj{
        	// CHANGE START
	public static String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db5";
	public static String dbUsername = "Group5";
	public static String dbPassword = "ride";
    // CHANGE END
   
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
    
    private static void create(Connection mySQLDB)throws SQLException {              //Create tables
        delete(mySQLDB);
        String driverTableQuery=  
            "CREATE TABLE Drivers" + 
             "(id INT(16), " +  
             "Name VARCHAR(30), " +
             "vid VARCHAR(6), " +
             "PRIMARY KEY (ID));";  
        String vehicleTableQuery= "CREATE TABLE Vehicles" + 
             "(id VARCHAR(6), " +  
             "Model VARCHAR(30), " +
             "Model_Year INT(16), " +
             "Seats INT(4), " +
             "PRIMARY KEY (ID))";  
        String passengerTableQuery= "CREATE TABLE Passengers" + 
             "(id INT(16), " +  
             "Name VARCHAR(30), " +
             "PRIMARY KEY (ID))";  
        String tripsTableQuery= "CREATE TABLE Trips" + 
             "(id INT(16), " +  
             "Driverid INT(16), " +
             "Passengerid INT(16), " +
             "Start VARCHAR(20)," +
             "End VARCHAR(20)," +
             "Fee INT(16), " +
             "Rating INT(16), " +
             "PRIMARY KEY (ID))";  
        try {
        Statement statement = mySQLDB.createStatement();

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
        System.out.println("Tables Created");
        statement.close();
        }
    
        catch (SQLException e ) {
        System.out.println("An error has occured on Table Creation");
        e.printStackTrace();
        }
    }

    private static void load(Connection mySQLDB)throws SQLException{

        String addDrivers= "LOAD DATA LOCAL INFILE 'drivers.csv' INTO TABLE Drivers FIELDS TERMINATED BY ',' ;";
        String addVehicles= "LOAD DATA LOCAL INFILE 'vehicles.csv' INTO TABLE Vehicles FIELDS TERMINATED BY ',' ;";
        String addPassengers= "LOAD DATA LOCAL INFILE 'passengers.csv' INTO TABLE Passengers FIELDS TERMINATED BY ',' ;";
        String addTrips= "LOAD DATA LOCAL INFILE 'trips.csv' INTO TABLE Trips FIELDS TERMINATED BY ',' ;";

        try {
            Statement statement = mySQLDB.createStatement();
           
            statement.executeUpdate(addDrivers);
            System.out.println("Driver Table Loaded");
            
            statement.executeUpdate(addVehicles);
            System.out.println("Vehicle Table Loaded");
         
            statement.executeUpdate(addPassengers);
            System.out.println("Passenger Table Loaded");
    
            statement.executeUpdate(addTrips);
            System.out.println("Trips Table Loaded");

            System.out.println("loaded!");
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

        try{
            Statement statement = mySQLDB.createStatement();
            statement.executeUpdate(dropDriver);
            //System.out.println("Driver Table Deleted");
            statement.executeUpdate(dropVehicles);
            //System.out.println("Vehicles Table Deleted");
            statement.executeUpdate(dropPassenger);
            //System.out.println("Passengers Table Deleted");
            statement.executeUpdate(dropTrips);
            //System.out.println("Trips Table Deleted");
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
                ResultSet rs = stmt.executeQuery("select count(*) from "+table_name[i]);
    
                rs.next();
                System.out.println(table_name[i]+": "+rs.getString(1));
                rs.close();
                stmt.close();
            }
    }
    
    public static void adminMenu(Connection mySQLDB)throws SQLException {
        int adminMenuStatus = 1; 
        Scanner scan = new Scanner(System.in);

        while(adminMenuStatus == 1){
            System.out.println("Passenger, what would you like to do?");
            System.out.println("0. Display Driver");
            System.out.println("1. Create tables");
            System.out.println("2. Delete tables");
            System.out.println("3. Load data");
            System.out.println("4. Check Data");
            System.out.println("5. Go back");
            System.out.println("Please enter [1-5].");
            String str = scan.nextLine();
            try{
                int answer = Integer.parseInt(str);
                switch(answer){
                    case 0:
                        displayAll(mySQLDB);
                        break;
                    case 1:
                        create(mySQLDB);
                        break;
                    case 2:
                        delete(mySQLDB);
                        break;
                    case 3:
                        load(mySQLDB);
                        break;
                    case 4:          
                        check(scan, mySQLDB);
                        break;  
                    case 5:
                        adminMenuStatus = 0; 
                        return;
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
                        create(mySQLDB);
                        break;
                    case 2:
                        delete(mySQLDB);
                        break;
                    case 3:
                        load(mySQLDB);
                        break;
                    case 4:
                        passMenuStatus = 0; 
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
                        create(mySQLDB);
                        break;
                    case 2:
                        delete(mySQLDB);
                        break;
                    case 3:
                        load(mySQLDB);
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
                                System.out.println("program exit");
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
