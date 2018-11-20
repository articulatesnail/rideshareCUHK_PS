
import java.util.Scanner;
import java.sql.*;
import java.io.*;
//import au.com.bytecode.opencsv.CSVReader;


class admin{
    static Connection con = null;
    static Statement statement=null;

    private static void create() {              //Create tables
    
        String dropTableQuery= "DROP TABLE IF EXISTS Driver;"; 
        String dropTable1Query= "DROP TABLE IF EXISTS Vehicles;"; 
        String dropTable2Query= "DROP TABLE IF EXISTS Passenger;"; 
        String dropTable3Query= "DROP TABLE IF EXISTS Trips;"; 

        String driverTableQuery=  
            "CREATE TABLE Driver" + 
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
        String passengerTableQuery= "CREATE TABLE Passenger" + 
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
        // Class.forName(jdbcDriver);
        con = main.connectToOracle();           //returns con- login credentials
        statement = con.createStatement();

        statement.executeUpdate(dropTableQuery);
        statement.executeUpdate(driverTableQuery);
        System.out.println("Driver Table Created");

        statement.executeUpdate(dropTable1Query);
        statement.executeUpdate(vehicleTableQuery);
        System.out.println("Vehicle Table Created");

        statement.executeUpdate(dropTable2Query);
        statement.executeUpdate(passengerTableQuery);
        System.out.println("Passenger Table Created");

        statement.executeUpdate(dropTable3Query);
        statement.executeUpdate(tripsTableQuery);
        System.out.println("Trips Table Created");
    }
    
    catch (SQLException e ) {
        System.out.println("An error has occured on Table Creation");
        e.printStackTrace();
    }
    }

    private static void load(){

        
        System.out.println("loaded!");
        return;
    }

    private static void delete(){

        String dropDriver= "DROP TABLE IF EXISTS Driver;"; 
        String dropVehicles= "DROP TABLE IF EXISTS Vehicles;"; 
        String dropPassenger= "DROP TABLE IF EXISTS Passenger;"; 
        String dropTrips= "DROP TABLE IF EXISTS Trips;"; 

        try{
            con = main.connectToOracle();           //returns con- login credentials
            statement = con.createStatement();

            statement.executeUpdate(dropDriver);
            System.out.println("Driver Table Deleted");
            statement.executeUpdate(dropVehicles);
            System.out.println("Vehicles Table Deleted");
            statement.executeUpdate(dropPassenger);
            System.out.println("Passengers Table Deleted");
            statement.executeUpdate(dropTrips);
            System.out.println("Trips Table Deleted");

            System.out.println("All Tables Deleted");
        }
        catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
         }
         catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
         }
         finally{
            //finally block used to close resources
            try{
               if(statement!=null)
                  con.close();
            }catch(SQLException se){
            }// do nothing
            try{
               if(con!=null)
                  con.close();
            }catch(SQLException se){
               se.printStackTrace();
            }
        }//end finally try
         

        }
        

        

        
    

    private static void check(){
        System.out.println("checked");
        return;
    } 
    
    public static void initializeAdminPrompt() {
        int status = 0; 
        Scanner menuAns = new Scanner(System.in);
        String answer;

    while(status == 0){
        System.out.println("Administrator, what would you like to do?");
        System.out.println("1. Create tables");
        System.out.println("2. Delete tables");
        System.out.println("3. Load data");
        System.out.println("4. Check Data");
        System.out.println("5. Go back");
        System.out.print("Please enter [1-5].\n\n");
    

        if(status==0){
            answer = menuAns.nextLine();
            switch(answer){
                case "1":
                    create();
                    break;
                 case "2":
                     delete();
                    break;
                case "3":
                    load();
                    break;
                case "4":          
                    check();
                    break;
                case "5":
                    status = 1; 
                    System.out.println("back to main\n\n");
                    return;
                default:
                    System.out.print("[ERROR] Please enter [1-5]. \n");
            }
        }
    }
    menuAns.close();
    System.out.println("finished");
    return;
    }
}
public class main{
    	// CHANGE START
	public static String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db5";
	public static String dbUsername = "Group5";
	public static String dbPassword = "ride";
	// CHANGE END

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
		Scanner menuAns = new Scanner(System.in);
        String answer;
        int flag=0;
        
		while(true){
			try{
				Connection mySQLDB = connectToOracle();
				if(mySQLDB == null){
					answer = "0";
					System.out.println("[Error]: Database connection failed, system exit");
				}else{
					while (flag==0){
						System.out.println("Welcome! Who are you?");
						System.out.println("1. An administrator");
						System.out.println("2. A passenger");
						System.out.println("3. A driver");
						System.out.println("4. Exit this program");
                        System.out.print("Enter Your Choice: ");
                        
                        if(flag==0){
                        answer = menuAns.nextLine();            // error: No line found
						
						switch (answer) { 
							case "1":
								System.out.println("1. An administrator");
								//flag=1;
								admin.initializeAdminPrompt();
								break;
							case "2":
                                System.out.println("2. A passenger");
                                //flag=1;
								break;
							case "3":
								System.out.println("3. A driver");
								//flag=1;
								break;
							case "4":
                                flag=1;
                                System.out.println("program exit.");
								return;
							default:
								System.out.println("Error!");	
                        }
                        }
					}
				}

				break;
			}catch (Exception e){
				System.out.print("[Error]: ");
				System.out.println(e.getMessage());
			}
		}
		menuAns.close();
		System.exit(0);
	}
}
