import java.util.Scanner;

/**
 * CPU Scheduling Simulator
 * ------------------------------------------------------------
 * Algorithms : Priority Scheduling (non-preemptive) and Round Robin
 * Concepts   : Singly Linked List, own Sorting method, Circular Queue, Priority Queue (Min-Heap)
 * Formulas   : TAT = CT - AT      WT = TAT - BT      (all times in ms)
 * Rule       : in Priority Scheduling a LOWER number means HIGHER priority
 */
public class CPUSchedulingSimulator {
    static Scanner sc = new Scanner(System.in);

    // ==========================================================
    // 1. PROCESS = one node of the singly linked list
    //    id  : name of the process (P1, P2 ...)
    //    at  : arrival time        bt : burst time (total CPU time needed)
    //    pr  : priority            rem: remaining burst time (used by Round Robin)
    //    ct  : completion time     order: entered order (used to break ties)
    //    next: link to the next process in the list
    // ==========================================================
    static class Process {
        String id; int at, bt, pr, rem, ct, order; Process next;
        Process(String id, int at, int bt, int pr, int order) {
            this.id = id; this.at = at; this.bt = bt; this.pr = pr; this.rem = bt; this.order = order;
        }
    }

    // ==========================================================
    // 2. CIRCULAR QUEUE (array based) = ready queue of Round Robin
    //    front = index of the first process, count = processes inside.
    //    The "% a.length" makes the index wrap around to 0 at the end of the array.
    // ==========================================================
    static class CircularQueue {
        Process[] a; int front, count;
        CircularQueue(int n) { a = new Process[n]; }
        boolean isEmpty() { return count == 0; }
        void enqueue(Process p) { a[(front + count++) % a.length] = p; }          // add at rear
        Process dequeue() { Process p = a[front]; front = (front + 1) % a.length; count--; return p; } // remove from front
        String show() {                                                           // queue content, for the trace
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < count; i++) sb.append(a[(front + i) % a.length].id).append(i < count - 1 ? ", " : "");
            return sb.append("]").toString();
        }
    }

    // ==========================================================
    // 3. MIN-HEAP = priority queue of Priority Scheduling
    //    The root h[0] is always the process that must run next:
    //    lowest priority number first; if equal -> earlier arrival; if equal -> entered first.
    // ==========================================================
    static class MinHeap {
        Process[] h; int size;
        MinHeap(int n) { h = new Process[n]; }
        boolean less(Process x, Process y) {
            if (x.pr != y.pr) return x.pr < y.pr;
            if (x.at != y.at) return x.at < y.at;
            return x.order < y.order;
        }
        void swap(int i, int j) { Process t = h[i]; h[i] = h[j]; h[j] = t; }
        void insert(Process p) {                        // put at the end, then move UP while better than its parent
            int i = size++; h[i] = p;
            while (i > 0 && less(h[i], h[(i - 1) / 2])) { swap(i, (i - 1) / 2); i = (i - 1) / 2; }
        }
        Process extractMin() {                          // take the root, move the last one to the root, move it DOWN
            Process top = h[0]; h[0] = h[--size]; int i = 0;
            while (true) {
                int l = 2 * i + 1, r = l + 1, m = i;
                if (l < size && less(h[l], h[m])) m = l;
                if (r < size && less(h[r], h[m])) m = r;
                if (m == i) break;
                swap(i, m); i = m;
            }
            return top;
        }
    }

    // ==========================================================
    // 4. OWN SORTING METHOD = insertion sort on the linked list (by arrival time)
    //    Each node is taken from the old list and inserted at its correct place in a new sorted list.
    //    "<=" in the inner loop keeps equal arrival times in the entered order (stable sort).
    // ==========================================================
    static Process sortByArrival(Process head) {
        Process sorted = null;
        while (head != null) {
            Process cur = head; head = head.next;
            if (sorted == null || cur.at < sorted.at) { cur.next = sorted; sorted = cur; }   // becomes the new first node
            else {
                Process s = sorted;
                while (s.next != null && s.next.at <= cur.at) s = s.next;                     // find the place
                cur.next = s.next; s.next = cur;
            }
        }
        return sorted;
    }

    // ==========================================================
    // 5. INPUT + VALIDATION
    //    Reads a whole number >= min. If the number is too small, prints msg and asks again.
    //    If the text is not a number at all, it also asks again.
    // ==========================================================
    static int read(String prompt, int min, String msg) {
        while (true) {
            System.out.print(prompt);
            try {
                int v = Integer.parseInt(sc.nextLine().trim());
                if (v >= min) return v;
                System.out.println(msg);
            } catch (NumberFormatException e) { System.out.println("Please enter a whole number."); }
        }
    }

    // Gantt chart pieces (text line, boxes, and time axis) built one segment at a time
    static StringBuilder gantt = new StringBuilder("Gantt: "), bar = new StringBuilder("|"), times = new StringBuilder("0");
    static void addSegment(String name, int start, int end) {
        String cell = " " + name + " ";
        gantt.append(name).append("(").append(start).append("-").append(end).append(") ");
        bar.append(cell).append("|");
        times.append(" ".repeat(Math.max(1, cell.length() + 1 - String.valueOf(start).length()))).append(end);
    }

    public static void main(String[] args) {
        System.out.println("=== CPU Scheduling Simulator ===");
        int choice = read("1. Priority (non-preemptive)   2. Round Robin  -> choose: ", 1, "Choose 1 or 2.");
        while (choice > 2) choice = read("Choose 1 or 2: ", 1, "Choose 1 or 2.");
        int n = read("Number of processes: ", 1, "Number of processes must be greater than 0.");

        // ---------- read the processes and append each one to the linked list ----------
        Process head = null, tail = null;
        for (int i = 0; i < n; i++) {
            System.out.println("Process " + (i + 1) + ":");
            String id;
            while (true) {                               // process ID must be non-empty and different from earlier IDs
                System.out.print("  Process ID (e.g. P1): ");
                id = sc.nextLine().trim();
                boolean used = false;
                for (Process t = head; t != null; t = t.next) if (t.id.equalsIgnoreCase(id)) used = true;
                if (id.isEmpty()) System.out.println("Invalid process ID. It cannot be empty.");
                else if (used) System.out.println("Invalid process ID. It is already used.");
                else break;
            }
            int at = read("  Arrival Time (AT): ", 0, "Invalid arrival time. It must be 0 or greater.");
            int bt = read("  Burst Time (BT): ", 1, "Invalid burst time. It must be greater than 0.");
            int pr = choice == 1 ? read("  Priority (lower number = higher priority): ", 1, "Invalid priority. It must be greater than 0.") : 1;
            Process p = new Process(id, at, bt, pr, i);
            if (head == null) head = tail = p; else { tail.next = p; tail = p; }
        }
        int q = choice == 2 ? read("Time Quantum: ", 1, "Invalid time quantum. It must be greater than 0.") : 0;

        // ---------- sort by arrival time and show the new order ----------
        head = sortByArrival(head);
        StringBuilder order = new StringBuilder("Sorted by arrival: ");
        for (Process p = head; p != null; p = p.next) order.append(p.id).append("(AT ").append(p.at).append(")").append(p.next != null ? ", " : "");
        System.out.println("\n" + order);
        System.out.println("\n--- Execution trace (" + (choice == 1 ? "Priority" : "Round Robin, Quantum " + q) + ") ---");

        // ---------- the scheduling loop ----------
        Process ptr = head;                              // first process that has NOT yet entered the ready structure
        int time = 0, done = 0;
        MinHeap heap = new MinHeap(n);                   // used by Priority Scheduling
        CircularQueue queue = new CircularQueue(n);      // used by Round Robin (a process is in it at most once -> size n is enough)

        while (done < n) {
            // If nothing is ready, the CPU is idle: jump to the next arrival time.
            if (choice == 1 ? heap.size == 0 : queue.isEmpty()) {
                if (ptr.at > time) {
                    System.out.println("Time " + time + ": CPU idle until " + ptr.at);
                    addSegment("Idle", time, ptr.at);
                    time = ptr.at;
                }
            }
            // Move every process that has arrived by 'time' into the heap / queue.
            while (ptr != null && ptr.at <= time) {
                System.out.println("Time " + time + ": " + ptr.id + " (arrival time " + ptr.at + ")"
                        + (choice == 1 ? ", priority " + ptr.pr + " -> added to heap" : " -> added to queue"));
                if (choice == 1) heap.insert(ptr); else queue.enqueue(ptr);
                ptr = ptr.next;
            }
            // Pick the next process: heap root (Priority) or front of the queue (Round Robin).
            Process p = choice == 1 ? heap.extractMin() : queue.dequeue();
            // Priority runs the whole burst (non-preemptive); Round Robin runs one quantum or less.
            int run = choice == 1 ? p.bt : Math.min(q, p.rem);
            System.out.println("Time " + time + ": " + p.id + " starts" + (choice == 1 ? " (priority " + p.pr + ")" : " (remaining " + p.rem + ")")
                    + " and runs for " + run + " ms -> " + time + "-" + (time + run));
            addSegment(p.id, time, time + run);
            time += run; p.rem -= run;

            if (choice == 2) {
                // Processes that arrived while p was running join BEFORE p goes back to the queue.
                while (ptr != null && ptr.at <= time) {
                    System.out.println("Time " + time + ": " + ptr.id + " (arrival time " + ptr.at + ") -> added to queue (before " + p.id + " is re-queued)");
                    queue.enqueue(ptr); ptr = ptr.next;
                }
                if (p.rem > 0) { queue.enqueue(p); System.out.println("          " + p.id + " not finished (remaining " + p.rem + ") -> back to queue " + queue.show()); }
            }
            if (p.rem == 0) { p.ct = time; done++; System.out.println("          " + p.id + " finished at time " + time); }
        }

        // ---------- results ----------
        System.out.println("\n" + gantt.toString().trim());
        System.out.println(bar);
        System.out.println(times);

        System.out.println("\nProcess  AT   BT   " + (choice == 1 ? "Priority  " : "") + "CT   TAT  WT");
        StringBuilder wtLine = new StringBuilder("WT: "), tatLine = new StringBuilder("TAT: ");
        double sumWt = 0, sumTat = 0;
        for (Process p = head; p != null; p = p.next) {
            int tat = p.ct - p.at, wt = tat - p.bt;                 // TAT = CT - AT, WT = TAT - BT
            System.out.println(String.format("%-9s%-5d%-5d", p.id, p.at, p.bt) + (choice == 1 ? String.format("%-10d", p.pr) : "")
                    + String.format("%-5d%-5d%-5d", p.ct, tat, wt));
            wtLine.append(p.id).append(" ").append(wt).append(p.next != null ? ", " : "");
            tatLine.append(p.id).append(" ").append(tat).append(p.next != null ? ", " : "");
            sumWt += wt; sumTat += tat;
        }
        System.out.println("\n" + wtLine + " | " + tatLine);
        System.out.println("Avg WT: " + Math.round(sumWt / n * 100) / 100.0 + " | Avg TAT: " + Math.round(sumTat / n * 100) / 100.0);
    }
}