// prompt
import java.util.Scanner;

public class admin{
    private static void create() {              //Create tables
        System.out.println("created");
    }
    
    private static void load(){
        System.out.println("loaded!");
    }

    private static void delete(){
        System.out.println("deleted");
    }

    private static void check(){
        System.out.println("checked");
    } 
    
    public static void main(String args[]) {
        int status = 0; 
        Scanner menuAns = new Scanner(System.in);
        String answer;


        System.out.println("Administrator, what would you like to do?");
        System.out.println("1. Create tables");
        System.out.println("2. Delete tables");
        System.out.println("3. Load data");
        System.out.println("4. Check Data");
        System.out.println("5. Go back");
    while(status == 0){
        System.out.print("Please enter [1-5].\n");

        answer = menuAns.nextLine();

        if(status == 0 ){

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
                System.out.println("back to main\n");
                break;
                default:
                    System.out.print("[ERROR] Please enter [1-5]. \n");
            }
        }
    }
    menuAns.close();
    System.out.println("finished");
}
}