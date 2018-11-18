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
    
    public static void main(String args[]){
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
        System.out.print("Please enter [1-5].");

        answer = menuAns.nextLine();

        if(status == 0 ){

            switch(answer){
                case '1':
                    status =1;
                    create();
                case '2':
                    status =1;
                    load();
                case '3':
                    status =1;
                    delete();
                case '4':
                    status = 1;               
                    check();
                case '5':
                    return;
            }
        }

        else
            System.out.print("[ERROR] Please enter [1-5].");
    }
}
}