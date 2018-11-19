
import java.util.Scanner;
import java.sql.*;
import java.io.*;
public class main {

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
			//System.out.println(e);
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
								flag=1;
								break;

							case "2":
								System.out.println("2. A passenger");
								break;

							case "3":
								System.out.println("3. A driver");
								flag=1;
								break;

							case "4":
								flag=1;
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
