
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

	public static void main(String args[]) {
		Scanner menuAns = new Scanner(System.in);
		String answer;
		System.out.println();
		while(true){
			try{
				Connection mySQLDB = connectToOracle();
				if(mySQLDB == null){
					answer = "0";
					System.out.println("[Error]: Database connection failed, system exit");
				}else{
					//CHANGE STARTS
					System.out.println("Administrator, what would you like to do?");
					System.out.println("What kinds of operation would you like to perform?");
					System.out.println("1. Operations for administrator");
					System.out.println("2. Operations for exploration companies (rental customers)");
					System.out.println("3. Operations for spacecraft rental staff");
					System.out.println("0. Exit this program");
					System.out.print("Enter Your Choice: ");

					answer = menuAns.nextLine();
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
