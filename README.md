# CPU Scheduling Simulator

A console-based Java application that simulates CPU scheduling using **Non-Preemptive Priority Scheduling** and **Round Robin Scheduling**. This project demonstrates the practical implementation of Data Structures and Algorithms (DSA) concepts such as Singly Linked List, Insertion Sort, Circular Queue, and Min-Heap.

##  Project Overview

The CPU Scheduling Simulator demonstrates how an operating system selects processes for CPU execution using different scheduling algorithms.

The user enters process details such as Process ID, Arrival Time, Burst Time, and Priority. The program simulates the selected scheduling algorithm and displays the execution order through a Gantt chart.

It also calculates the Completion Time, Turnaround Time, Waiting Time, and their averages.

##  Objectives

- Implement CPU scheduling algorithms using Java.
- Understand the working of Non-Preemptive Priority Scheduling.
- Understand the working of Round Robin Scheduling.
- Apply Data Structures and Algorithms to a practical problem.
- Calculate process scheduling performance metrics.
- Display the execution sequence using a Gantt chart.

##  Technologies Used

- **Programming Language:** Java
- **Data Structures:** Singly Linked List, Circular Queue, Min-Heap
- **Algorithm:** Insertion Sort
- **IDE:** VS Code / IntelliJ IDEA / Any Java IDE
- **Version Control:** Git and GitHub

##  DSA Concepts Implemented

| Concept | Purpose |
|---|---|
| Singly Linked List | Stores process information |
| Insertion Sort | Sorts processes by arrival time |
| Circular Queue | Manages the ready queue in Round Robin |
| Min-Heap | Selects the highest-priority process |
| CPU Scheduling | Simulates process execution |

##  Scheduling Algorithms

### 1. Priority Scheduling (Non-Preemptive)

- The process with the highest priority is selected for execution.
- A **lower priority number indicates higher priority**.
- Once a process starts executing, it continues until completion.
- A Min-Heap is used to manage the ready processes.

### 2. Round Robin Scheduling

- Each process receives CPU time according to a fixed time quantum.
- If a process is not completed within its quantum, it is placed back into the ready queue.
- Newly arrived processes are added to the queue before the running process is re-queued.
- An array-based Circular Queue is used to manage the ready queue.

##  Scheduling Metrics

The program calculates the following values:

| Metric | Formula |
|---|---|
| Completion Time (CT) | Time at which a process finishes |
| Turnaround Time (TAT) | CT − AT |
| Waiting Time (WT) | TAT − BT |
| Average Waiting Time | Total WT / Number of Processes |
| Average Turnaround Time | Total TAT / Number of Processes |

##  Features

- Console-based user interaction
- Two CPU scheduling algorithms
- Process input and validation
- Sorting processes by arrival time
- Gantt chart generation
- Execution trace for scheduling operations
- Calculation of individual and average waiting time
- Calculation of individual and average turnaround time
- Handling of CPU idle time
- Invalid input detection

##  How to Run

### Prerequisites

- Java Development Kit (JDK) installed
- Command Prompt or terminal
- Git (optional)

### Steps

1. Clone the repository:

   ```bash
   git clone https://github.com/YOUR-USERNAME/CPU-Scheduling-Simulator.git
   ```

2. Open the project folder:

   ```bash
   cd CPU-Scheduling-Simulator
   ```

3. Compile the Java program:

   ```bash
   javac CPUSchedulingSimulator.java
   ```

4. Run the program:

   ```bash
   java CPUSchedulingSimulator
   ```

##  Sample Test Cases

The project includes the following test scenarios:

- Normal Priority Scheduling input
- Boundary / minimum input with one process
- Invalid burst time input
- Round Robin scheduling with different arrival times

##  Future Scope

The project can be extended with:

- First Come First Serve (FCFS) Scheduling
- Shortest Job First (SJF) Scheduling
- Preemptive Priority Scheduling
- Reading process details from a file
- Comparing multiple scheduling algorithms
- Graphical Gantt chart visualization

##  Learning Outcomes

Through this project, I learned how to:

- Apply data structures to real-world problems.
- Implement scheduling algorithms using Java.
- Understand the behavior of CPU scheduling.
- Work with linked lists, queues, and heaps.
- Calculate process performance metrics.
- Use Git and GitHub for project management.

##  Author

**Palak Tiwari**

Bachelor of Technology  
Computer Science and Engineering (AI & ML)

##  Project Information

- **Subject:** Data Structures and Algorithms (DSA)
- **Project Title:** CPU Scheduling Simulator
- **Language:** Java
- **Academic Session:** 2026–27

---

*This project was developed as part of the Data Structures and Algorithms coursework.*
