import java.util.*;

public class SJFS {
    private int NumOfProcesses;
    private List<Process> Processes = new LinkedList<Process>();
    private List<Process> ReadyQueue = new LinkedList<Process>();
    private List<Process> CompletedProcesses = new LinkedList<Process>();
    private int ContextSwitching;
    private List<String> Names = new LinkedList<String>();
    private List<String> arrivals = new LinkedList<String>();
    private List<String> bursts = new LinkedList<String>();

    public void start() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter number of Processes: ");
        NumOfProcesses = scan.nextInt();

        System.out.println("Context Switching: ");
        ContextSwitching = scan.nextInt();

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

        while (CompletedProcesses.size() != NumOfProcesses) {
            for (int i = 0; i < NumOfProcesses; i++) {
                if (Processes.get(i).Arrival == CPU.Timer) {
                    ReadyQueue.add(Processes.get(i));
                    ReadyQueue.sort(Comparator.comparingInt(Process::GetBurst));

                }
            }

            if (ReadyQueue.size() != 0)
                if ((ReadyQueue.get(0).Burst < CPU.CurrentProcess.Burst && CPU.CurrentProcess.Burst > 0)) {
                    int Co_Sw = ContextSwitching;
                    System.out.println("Context Switching at " + CPU.Timer + " "+" and finishing at "+(CPU.Timer+Co_Sw));
                    while (Co_Sw != 0) {

                        Co_Sw--;
                        for (Process p : ReadyQueue) {
                            p.Waited++;
                        }
                        CPU.Timer++;
                        for (int i = 0; i < NumOfProcesses; i++) {
                            if (Processes.get(i).Arrival == CPU.Timer) {
                                ReadyQueue.add(Processes.get(i));
                                ReadyQueue.sort(Comparator.comparingInt(Process::GetBurst));

                            }
                        }
                    }
                    ReadyQueue.add(CPU.CurrentProcess);
                    ReadyQueue.sort(Comparator.comparingInt(Process::GetBurst));
                    CPU.CurrentProcess = ReadyQueue.get(0);
                    System.out.println("Process " + CPU.CurrentProcess.name + " started at " + CPU.Timer);
                    ReadyQueue.remove(0);
                }

            if (CPU.CurrentProcess.Burst < 0) {
                if (ReadyQueue.size() != 0) {
                    int Co_Sw = ContextSwitching;
                    System.out.println("Context Switching at " + CPU.Timer + " "+" and finishing at "+(CPU.Timer+Co_Sw));
                    while (Co_Sw != 0) {
                        Co_Sw--;
                        for (Process p : ReadyQueue) {
                            p.Waited++;
                        }
                        CPU.Timer++;
                        for (int i = 0; i < NumOfProcesses; i++) {
                            if (Processes.get(i).Arrival == CPU.Timer) {
                                ReadyQueue.add(Processes.get(i));
                                ReadyQueue.sort(Comparator.comparingInt(Process::GetBurst));

                            }
                        }
                    }
                    CPU.CurrentProcess = ReadyQueue.get(0);
                    ReadyQueue.remove(0);
                    System.out.println("Process " + CPU.CurrentProcess.name + " started at " + CPU.Timer);
                }
            }


            if (CPU.CurrentProcess.Burst == 0) {
                CompletedProcesses.add(CPU.CurrentProcess);
                CPU.CurrentProcess.Completion = CPU.Timer;
                CPU.CurrentProcess.setTurnaround();
                System.out.println("Process " + CPU.CurrentProcess.name + " finished at " + CPU.Timer);
                if (ReadyQueue.size() != 0) {
                    int Co_Sw = ContextSwitching;
                    System.out.println("Context Switching at " + CPU.Timer + " "+" and finishing at "+(CPU.Timer+Co_Sw));
                    while (Co_Sw != 0) {
                        Co_Sw--;
                        for (Process p : ReadyQueue) {
                            p.Waited++;
                        }
                        CPU.Timer++;
                        for (int i = 0; i < NumOfProcesses; i++) {
                            if (Processes.get(i).Arrival == CPU.Timer) {
                                ReadyQueue.add(Processes.get(i));
                                ReadyQueue.sort(Comparator.comparingInt(Process::GetBurst));

                            }
                        }
                    }
                    CPU.CurrentProcess = ReadyQueue.get(0);
                    System.out.println("Process " + CPU.CurrentProcess.name + " started at " + CPU.Timer);
                    ReadyQueue.remove(0);
                } else {
                    CPU.CurrentProcess = new Process();
                }


            }

            if (CPU.CurrentProcess.Burst > 0) {
                CPU.CurrentProcess.Burst--;
                for (Process p : ReadyQueue) {
                    p.Waited++;
                }
            }


            CPU.Timer++;
        }
        for (Process p : CompletedProcesses) {
            System.out.println(p.name + " Waited for " + p.Waited+" with turnaround time of " + p.Turnaround + " and completion time of " + p.Completion);
        }
        System.out.println("Average Waiting Time: "+" =" + AverageWaitingTime());
        System.out.println("Average Turnaround Time: "+" =" + AverageTurnaround());


    }


    private float AverageTurnaround() {
        float sum = 0;
        for (Process p : CompletedProcesses) {
            sum += p.Turnaround;
        }
        return (float) sum / NumOfProcesses;
    }

    private float AverageWaitingTime() {
        float sum = 0;
        for (Process p : CompletedProcesses) {
            sum += p.Waited;
        }
        return (float) sum / NumOfProcesses;
    }


}
