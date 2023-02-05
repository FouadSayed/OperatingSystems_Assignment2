import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println("1. SJFS");
            System.out.println("2. round robin");
            System.out.println("3. Preemptive priority");
            System.out.println("4. AG");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int UserChoice = sc.nextInt();
            switch (UserChoice){
                case 1:
                    SJFS sjfs = new SJFS();
                    sjfs.start();
                    break;
                case 2:
                    RR rr = new RR();
                    rr.start();
                    break;
                case 3:
                    PP preemptivePriority = new PP();
                    preemptivePriority.start();
                    break;
                case 4:
                    AG ag = new AG();
                    ag.start();
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }


        }
    }
}