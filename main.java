
import java.util.Scanner;
import java.sql.*;
import java.io.*;

class admin{
    static Connection con = null;
    static Statement statement=null;

    private static void create() {              //Create tables
        String driverTableQuery= "CREATE TABLE Driver" + 
             "dID INT(64), " +  
             "Name VARCHAR, " +
             "vID VARCHAR, " +
             "PRIMARY KEY (vID))";  
    try {
        // Class.forName(jdbcDriver);
        con = main.connectToOracle();           //returns con- login credentials
        statement = con.createStatement();
        statement.executeUpdate(driverTableQuery);
        System.out.println("Table Created");
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
        System.out.println("deleted");
        return;
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
        answer = menuAns.nextLine();

        if(status==0){
        switch(answer){
             case "1":
                status =1;
                    create();
                    break;
            case "2":
                    status =1;
                    load();
                    break;
            case "3":
                    status =1;
                    delete();
                    break;
            case "4":
                    status = 1;               
                    check();
                    break;
            case "5":
                    status = 1; 
                    System.out.println("back to main\n\n");
                    break;

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
		System.out.println();
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
						answer = menuAns.nextLine();
						
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
