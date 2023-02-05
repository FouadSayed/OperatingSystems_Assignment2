import java.util.*;

public class RR {
    private List<String> Names = new LinkedList<String>();
    private List<String> arrivals = new LinkedList<String>();
    private List<String> bursts = new LinkedList<String>();

    private int NumOfProcesses=0;
    private int Quantum=0;
    private List<Process> Processes = new LinkedList<Process>();
    private List<Process> CompletedProcesses = new LinkedList<Process>();
    private List<Process> ReadyQueue = new LinkedList<Process>();
    private int ContextSwitching;
    public void start(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter number of Processes: ");
        NumOfProcesses = scan.nextInt();

        System.out.println("Context Switching: ");
        ContextSwitching = scan.nextInt();

        System.out.println("Enter quantum time: ");
        Quantum = scan.nextInt();

        System.out.println("Enter processes names: ");
        for (int i = 0; i < NumOfProcesses; i++) {
            Names.add(scan.next());

        }
        System.out.println("Enter processes Arrival times: ");
        for (int i = 0; i < NumOfProcesses; i++) {
            arrivals.add(scan.next());

        }
        System.out.println("Enter processes Burst times: ");
        for (int i = 0; i < NumOfProcesses; i++) {
            bursts.add(scan.next());

        }
        for (int i = 0; i < NumOfProcesses; i++) {
            Process p = new Process();
            p.name = Names.get(i);
            p.Arrival = Integer.parseInt(arrivals.get(i));
            p.Burst = Integer.parseInt(bursts.get(i));
            Processes.add(p);
        }
        Processes.sort(Comparator.comparingInt(Process::GetArrival));

        int Time=0;
        while (CompletedProcesses.size()!=NumOfProcesses)
        {
            for (int i = 0; i < NumOfProcesses; i++) {
                if (Processes.get(i).Arrival == Time) {
                    ReadyQueue.add(Processes.get(i));
                }
            }

boolean f=true;
            if(CPU.CurrentProcess.Burst==0)
            {
                f=false;
                CompletedProcesses.add(CPU.CurrentProcess);
                System.out.println(CPU.CurrentProcess.name+" Completed"+" at "+Time);
                CPU.CurrentProcess.Completion=Time;
                CPU.Timer=0;
            }
            if(CPU.Timer==0)
            {
                if (f){
                    System.out.println(CPU.CurrentProcess.name+" stop at "+Time);
                }
                int Co_Sw=ContextSwitching;
                System.out.println("Context Switching at " + Time + " "+" and finishing at "+(Time+Co_Sw));
                while (Co_Sw!=0)
                {
                    Co_Sw--;
                    for (Process p:ReadyQueue) {
                        p.Waited++;
                    }
                    Time++;
                    for (int i = 0; i < NumOfProcesses; i++) {
                        if (Processes.get(i).Arrival == Time) {
                            ReadyQueue.add(Processes.get(i));
                        }
                    }

                }
            }


            if(CPU.Timer==0)
            {
                if(CPU.CurrentProcess.Burst>0) {

                    ReadyQueue.add(CPU.CurrentProcess);
                }

                if(ReadyQueue.size()!=0) {
                    System.out.println(ReadyQueue.get(0).name+" started at "+Time);
                    CPU.Timer = Quantum;
                    CPU.CurrentProcess=ReadyQueue.remove(0);
                }
                else {
                    CPU.CurrentProcess=new Process();
                    Time++;
                    continue;
                }

            }

            CPU.CurrentProcess.Burst--;
            CPU.Timer--;
            for (Process p:ReadyQueue) {
                p.Waited++;
            }
            Time++;
        }

        for (Process p : CompletedProcesses) {
            System.out.println(p.name + " Waited for " + p.Waited+" with turnaround time of " + p.Turnaround + " and completion time of " + p.Completion);
        }
        System.out.println("Average Waiting Time: "+" =" + AverageWaitingTime());
        System.out.println("Average Turnaround Time: "+" =" + AverageTurnaround());
    }



    private float AverageTurnaround()
    {
        float sum=0;
        for (Process p:CompletedProcesses) {
            sum+=p.Turnaround;
        }
        return (float)sum/NumOfProcesses;
    }

    private float AverageWaitingTime()
    {
        float sum=0;
        for (Process p:CompletedProcesses) {
            sum+=p.Waited;
        }
        return (float)sum/NumOfProcesses;
    }

}
