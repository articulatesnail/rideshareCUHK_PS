
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
    
    private static void create(Connection mySQLDB) {              //Create tables
    
        delete(mySQLDB);
    
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
        Statement statement = mySQLDB.createStatement();

       // statement.executeUpdate(dropTableQuery);
        statement.executeUpdate(driverTableQuery);
        System.out.println("Driver Table Created1");

        //statement.executeUpdate(dropTable1Query);
        statement.executeUpdate(vehicleTableQuery);
        System.out.println("Vehicle Table Created1");

     //   statement.executeUpdate(dropTable2Query);
        statement.executeUpdate(passengerTableQuery);
        System.out.println("Passenger Table Created1");

       // statement.executeUpdate(dropTable3Query);
        statement.executeUpdate(tripsTableQuery);
        System.out.println("Trips Table Created1");
    }
    
    catch (SQLException e ) {
        System.out.println("An error has occured on Table Creation");
        e.printStackTrace();
    }
    }

    private static void load(Connection mySQLDB){
        System.out.println("loaded!");
    }

    private static void delete(Connection mySQLDB){

        String dropDriver= "DROP TABLE IF EXISTS Driver;"; 
        String dropVehicles= "DROP TABLE IF EXISTS Vehicles;"; 
        String dropPassenger= "DROP TABLE IF EXISTS Passenger;"; 
        String dropTrips= "DROP TABLE IF EXISTS Trips;"; 

        try{
            Statement statement = mySQLDB.createStatement();
            statement.executeUpdate(dropDriver);
            System.out.println("Driver Table Deleted");
            statement.executeUpdate(dropVehicles);
            System.out.println("Vehicles Table Deleted");
            statement.executeUpdate(dropPassenger);
            System.out.println("Passengers Table Deleted");
            statement.executeUpdate(dropTrips);
            System.out.println("Trips Table Deleted");

            System.out.println("All Tables Deleted");
            statement.close();
        }
        catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
         }
        //  catch(Exception e){
        //     //Handle errors for Class.forName
        //     e.printStackTrace();
        //  }
        //  finally{
        //     //finally block used to close resources
        //     try{
        //        if(statement!=null)
        //           statement.close();
        //     }catch(SQLException se){
        //     }// do nothing
        //     try{
        //        if(con!=null)
        //           statement.close();
        //     }catch(SQLException se){
        //        se.printStackTrace();
        //     }
        // }//end finally try

        }

    private static void check(){
        System.out.println("checked");
        return;
    } 
    
    public static void adminMenu(Connection mySQLDB)throws SQLException {
        int adminMenuStatus = 1; 
        Scanner scan = new Scanner(System.in);

    while(adminMenuStatus == 1){
        System.out.println("Administrator, what would you like to do?");
        System.out.println("1. Create tablesss");
        System.out.println(" Delete tables2");
        System.out.println("3. Load data2");
        System.out.println("4. Check Data2");
        System.out.println("5. Go back");
        System.out.print("Please enter [1-5].\n");
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
                    check();
                    break;  
                case 5:
                    adminMenuStatus = 0; 
                    System.out.println("back to main");
                    break;
                default:
                    System.out.print("[ERROR] Please enter [1-5].\n");
                    break;
            }
        }
        catch(NumberFormatException e){
            System.out.printf("Invalid input. Enter only 0, 1, 2 or 3\n");
        }
    }
    scan.close();
    System.out.println("finished");
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
	public static void main(String[] args) throws SQLException{
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
								adminMenu(mySQLDB);
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
                                System.out.println("program exit");
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
