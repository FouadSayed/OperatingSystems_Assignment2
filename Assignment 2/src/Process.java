public class Process {
    public String toString() {
        return String.valueOf(getName() + " : " + getPriority());
    }

    public  String name;
    public  int Arrival;
    public  int Burst;
    public  int Completion;
    public  int Turnaround;
    public  int Waited;
    public  int Priority;
    public  int starving;
    public  int Quantum;
    int idxAtAll;
    public Process(){
        name="";
        Waited=0;
        Turnaround=0;

        Arrival=-1;
        Burst=-1;
        Completion=-1;


        starving=0;
    }

    public Process(String name, int burst_time, int arrival_time, int priority, int quantum_time) {
        this.name = name;
        this.Burst = burst_time;
        this.Arrival = arrival_time;
        this.Priority = priority;
        this.Quantum = quantum_time;

    }


    public int GetBurst()
    {
        return Burst;
    }
    public int GetArrival()
    {
        return Arrival;
    }
    public String getName() {
        return name;
    }

    public int getBurst_time() {
        return Burst;
    }

    public int getArrival_time() {
        return Arrival;
    }

    public int getPriority() {
        return Priority;
    }

    public int getQuantum_time() {
        return Quantum;
    }

    public void setQuantum_time(int quantum_time) {
        this.Quantum = quantum_time;
    }

    public void setBurst_time(int burst_time) {
        this.Burst = burst_time;
    }

    public int getTurnaround_Time() {
        return Turnaround;
    }

    public void setTurnaround_Time(int turnaround_Time) {
        this.Turnaround = turnaround_Time;
    }

    public void setPriority(int priority) {
        this.Priority = priority;
    }


    public void setTurnaround() {
        Turnaround = Completion-Arrival;
    }
}
