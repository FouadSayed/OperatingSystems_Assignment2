import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class PP {

    private static int NumOfP=0;

    static Process[] Processes ;
    static Queue<Process> ReadyQue = new LinkedList<Process>();
    static Queue<Process> completed = new LinkedList<Process>();
    public  void start(){
        TakeInputs();
        OrderFirstCome();

        int Time=0;

        int limit=20;
        int Q=5;


        //low number represent high priority
        while (completed.size()!=NumOfP)
        {
            ReceiveAt(Time);

            //aging:
            for (Process p:ReadyQue) {
                if(p.starving%Q==0&&p.starving!=0)
                {
//                System.out.println(p.starving+" HEY "+Time+" ");
                    p.Priority--;
                }
            }
            reOrder();



            //Preempt without context switching as it's not mentioned in the assignment
            if(ReadyQue.size()!=0)
                if(ReadyQue.peek().Priority<CPU.CurrentProcess.Priority &&CPU.CurrentProcess.Burst>0)
                {
                    InsertInPlace_RQ(CPU.CurrentProcess,ReadyQue);
                    CPU.CurrentProcess=ReadyQue.poll();
                    System.out.println(Time+" "+CPU.CurrentProcess.name+"("+CPU.CurrentProcess.Priority+")");
                }

            //first or after empty time
            if(CPU.CurrentProcess.Burst<0)
            {
                if(ReadyQue.size()!=0)
                {
                    CPU.CurrentProcess=ReadyQue.poll();
                    System.out.println(Time+" "+CPU.CurrentProcess.name+" ");
                }
            }

            //completed
            if(CPU.CurrentProcess.Burst==0)
            {

                AddToCompletedQue(CPU.CurrentProcess,Time);
                if(ReadyQue.size()==0){CPU.CurrentProcess=new Process();
                    System.out.println(Time);}
                else {CPU.CurrentProcess=ReadyQue.poll(); System.out.println(Time+" "+CPU.CurrentProcess.name+" ");}
            }

            //running
            if(CPU.CurrentProcess.Burst>0)
            {
                CPU.CurrentProcess.Burst--;
                IncreaseWaitedTime();
                //starving:
                for (Process p:ReadyQue) {
                    if(p.Waited>limit) p.starving++;
                }

            }

            Time++;
        }

        for (Process p:completed) {
            System.out.println(p.name);
        }

        Statistics();

    }


    static void AddToCompletedQue(Process p,int T)
    {
        completed.offer(p);
        p.Completion=T;
        p.setTurnaround();
    }


    static void ReceiveAt(int arrival_T)
    {
        for (int i = 0; i <NumOfP ; i++) {
            if(Processes[i].Arrival==arrival_T)
            {
                InsertInPlace_RQ(Processes[i],ReadyQue);
            }
        }
    }


    static void reOrder()
    {
        Process t;
        Queue<Process> temp = new LinkedList<Process>();
        int n=ReadyQue.size();
        for (int i = 0; i <n ; i++) {
            t=ReadyQue.poll();
            InsertInPlace_RQ(t,temp);
        }
        ReadyQue=temp;
    }

    static void InsertInPlace_RQ(Process p,Queue<Process> Que)
    {
        if(Que.size()==0){Que.offer(p); return; }

        Process temp;
        boolean inserted=false;
        int n=Que.size();

        for (int i=0;i<n;i++) {
            temp=Que.poll();
            if(p.Priority<temp.Priority&&!inserted)
            {
                inserted=true;
                Que.offer(p);
            }
            Que.offer(temp);
        }
        if(!inserted) {
            Que.offer(p);
        }

    }

    private static void OrderFirstCome() {

        for (int i = 0; i <NumOfP-1 ; i++) {
            for (int j = 0; j <NumOfP-1 ; j++) {

                if(Processes[j].Arrival>Processes[j+1].Arrival)
                {
                    Process temp=Processes[j];
                    Processes[j]=Processes[j+1];
                    Processes[j+1]=temp;
                }
            }
        }
    }


    static void TakeInputs()
    {
        Scanner scan=new Scanner(System.in);
        System.out.println("Enter number of Processes: ");
        NumOfP = scan.nextInt();
        Initialize();

//        System.out.println("Quantum Time: ");
//        Quantum= scan.nextInt();

//        System.out.println("Context Switching: ");
//        ContextSwitching=scan.nextInt();


        scan.nextLine();
        System.out.println("Enter processes names: ");
        for (int i = 0; i < NumOfP; i++) {
//            System.out.println("Enter process " + (i + 1) + " name: ");
            Processes[i].name = scan.next();
        }

        System.out.println("Enter processes Arrival times: ");
        for (int i = 0; i < NumOfP; i++) {
//            System.out.println("Enter process " + (i + 1) + " Arrival time: ");
            Processes[i].Arrival = scan.nextInt();
        }

        System.out.println("Enter processes  Burst times: ");
        for (int i = 0; i < NumOfP; i++) {
//            System.out.println("Enter process " + (i + 1) + " Burst time: ");
            Processes[i].Burst = scan.nextInt();
        }

        System.out.println("Enter processes Priority: ");
        for (int i = 0; i <NumOfP; i++) {
//          System.out.println("Enter process " + (i + 1) + " priority: ");
            Processes[i].Priority = scan.nextInt();
        }

    }


    static void Initialize()
    {
        Processes = new Process[NumOfP];
        for (int i = 0; i < NumOfP ; i++) {
            Processes[i]=new Process();
        }
    }



    static void Statistics()
    {
        for (Process p:completed) {
            System.out.print(p.name+" Waited for "+p.Waited);
            System.out.println(" with turnaround time of "+p.Turnaround);
        }
        System.out.println("Average Waiting Time: ");
        System.out.println(" ="+AverageWaitingTime());
        System.out.println("Average Turnaround Time: ");
        System.out.println(" ="+AverageTurnaround());
    }

    static float AverageTurnaround()
    {
        float sum=0;
        for (Process p:completed) {
//            System.out.print(p.Turnaround+" + ");
            sum+=p.Turnaround;
        }
//        System.out.println("/"+NumOfP);
        return (float)sum/NumOfP;
    }

    static float AverageWaitingTime()
    {
        float sum=0;
        for (Process p:completed) {
//            System.out.print(p.Waited+" + ");
            sum+=p.Waited;
        }
//        System.out.println("/"+NumOfP);
        return (float)sum/NumOfP;
    }

    static void IncreaseWaitedTime()
    {
        int n=ReadyQue.size();
        for (Process p:ReadyQue) {
            p.Waited++;
        }
    }

}
