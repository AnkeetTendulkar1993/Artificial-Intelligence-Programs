// Name : Ankeet Tendulkar
// Homework Assignment 1
// Implementation of BFS and DFS algorithms

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class waterFlow {

	static FileReader fi = null; 
	static FileWriter fo = null;		
	static BufferedReader br;
	static String source = null;
	static String[] destination = null;
	static String[] middleNodes = null;
	static String[] graph;
	static int noOfPipes = 0;
	static int startTime = 0;
	static int time = 0;
	static int destNode;
	static int graphLength = 0;
	static int max;
	static boolean flag = true;
	
	public static void main(String[] args) throws IOException{
		
		try {
			fi = new FileReader(args[1]);
			File f = new File("output.txt");
			fo = new FileWriter(f,false);
			br = new BufferedReader(fi);
			System.out.println("Start of main");			
			String s,t;
			t = br.readLine();
			if(t.equals("")){
				System.out.println("The number of test cases were not printed");
			}
			else {
				int noOfTasks = Integer.parseInt(t.trim());
			}
			int lineNo = 0;
			while((s = br.readLine()) != null ) {
				if(s.isEmpty()){
					System.out.println("Empty Line");
					continue;
				}
				System.out.println(s);
				
				if (s.equalsIgnoreCase("BFS")) {
					try{
						fo = new FileWriter(f,true);
						System.out.println("Inside BFS Condition");
						initialize();
						BFS();
						display();
						System.out.println("Outside BFS Condition");
						fo.close();
					}
					catch(Exception e) {
						System.out.println("Exception in BFS");
						fo.append("\n");
						fo.close();
					}
				}
				else if(s.equalsIgnoreCase("DFS")){
					try {
						fo = new FileWriter(f,true);
						System.out.println("Inside DFS Condition");
						initialize();
						DFS();
						display();
						System.out.println("Outside DFS Condition");
						fo.close();
					}
					catch(Exception e) {
						System.out.println("Exception in DFS");
						fo.append("\n");
						fo.close();
					}
				}
				else if(s.equalsIgnoreCase("UCS")){
					try {
						fo = new FileWriter(f,true);
						System.out.println("Inside UCS Condition");
						initialize();
						UCS();
						display();
						System.out.println("Outside UCS Condition");
						fo.close();
					}
					catch(Exception e) {
						System.out.println("Exception in UCS");
						fo.append("\n");
						fo.close();
					}
				}
				else{
					System.out.println("None of the cases");
				}
				lineNo++;
			}
			fi.close();
		}
		catch(Exception e) {
			System.out.println("Exception from main");
		}		
	}
		
	// Display the basic data obtained from the text file
	public static void display() {	
		System.out.println("Source = " + source);
		System.out.println("Destination = " + destination[0]);
		System.out.println("Middle Nodes  = " + middleNodes[0]);
		System.out.println("Number of Pipes = " + noOfPipes);
		System.out.println("Start Time = " + startTime);		
	}
	
	// Initializes local variables with data obtained from the input file
	public static void initialize() {
		try {
			source = br.readLine().trim();
			destination = (br.readLine()).split(" ");
			middleNodes = (br.readLine()).split(" ");
			noOfPipes = Integer.parseInt(br.readLine().trim());
			graph = new String[noOfPipes];
			max = 0;
			for (int i = 0; i < noOfPipes; i++) {
				graph[i] = br.readLine();
				graphLength = (graph[i].split(" ")).length;
				if(graphLength > max) {
					max = graphLength;
				}
			}			
			startTime = Integer.parseInt(br.readLine().trim());
			flag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception in initialization");
			e.printStackTrace();
		}
	}

	// Checks if the node is a destination node
	public static boolean isDestination(Node a) {
		for (int i = 0 ; i < destination.length ; i++) {
			if(destination[i].equals(a.name)) {
				return true;
			}
		}
		return false;
	}
	
	// Checks if the node is a middle node node
		public static boolean isMiddleNode(Node a) {
			for (int i = 0 ; i < middleNodes.length ; i++) {
				if(middleNodes[i].equals(a.name)) {
					return true;
				}
			}
			return false;
		}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	// Performs BFS Algorithm
	public static void BFS() {
			System.out.println("---------- B F S ----------");
			String[] children = null;
			String[] temp1,temp2;
			Node parent;
			int count = 0;
			int cost = startTime,tempCost = 0;
			int cap = 1 + destination.length + middleNodes.length;

			BFSQueue frontier = new BFSQueue(cap);
			BFSQueue explored = new BFSQueue(cap);
			Node n = new Node(source,0,cost);
			n.parent = null;
			frontier.insert(n);
			if(isDestination(n)){
				try {
					fo.append(n.name + " " + (n.pathCost)%24 + "\n");
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} // End of if
			else {
				do {
					count++;
					children = null; 
					if(frontier.isEmpty()) {
						return;
					}
					frontier.displayQueue();
					explored.displayQueue();
					parent = frontier.pop();
					explored.insert(parent);
					children = childNode(parent.name,"BFS");
					if(children == null) {
						if(frontier.isEmpty()){
							try {
								fo.append("None\n");
								flag = false;
								return;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						continue;
					}
					parent.setChildren(children.length);
					// Displaying children 
					Node n1;
					for(int i = 0 ; i < children.length ; i++) {
						System.out.println(children[i]);
						temp1 = children[i].split(" ");
						String offTimes = "";
						if(Integer.parseInt(temp1[3]) != 0){
							for(int j = 4; j < (4 + Integer.parseInt(temp1[3])) ; j++) {
								offTimes += temp1[j] + " ";
							}
						}
						tempCost = parent.pathCost + 1;
						System.out.println("Temp Cost = " + tempCost);
						n1 = new Node(temp1[1],count,tempCost);
						n1.setOffTimes(Integer.parseInt(temp1[3]),offTimes);
						parent.children[i] = n1;
						n1.parent = parent;
						
						if (!((frontier.isPresent(n1)) || (explored.isPresent(n1)))) {
							if(isDestination(n1)) {
								try {
									fo.append(n1.name + " " + (n1.pathCost)%24 + "\n");
									flag = false;
									return;
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}	
							}
							else if(isMiddleNode(n1)){
								frontier.insert(n1);
							}
							else {
								try {
									fo.append("None\n");
									flag = false;
									return;
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}	
					}				
				}while(flag);
			} // End of else		
	} // End of BFS
	
	//////////////////////////////////////////////////////////////////////////////////////////
	// Performs DFS Algorithm
	public static void DFS() {
		System.out.println("---------- D F S ----------");
		String[] children = null;
		String[] temp1,temp2;
		Node parent;
		int depthCount = 0;
		int cost = startTime,tempCost = 0;
		int cap = 1 + destination.length + middleNodes.length;
		
		DFSQueue frontier = new DFSQueue(cap);
		BFSQueue explored = new BFSQueue(cap);
		Node n = new Node(source,0,cost);
		n.parent = null;
		frontier.insert(n);
		if(isDestination(n)){
			try {
				fo.append(n.name + " " + n.pathCost%24 + "\n");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // End of if
		else {
			do {
				depthCount++;
				children = null; 
				if(frontier.isEmpty()) {
					try {
						fo.append("None\n");
						flag = false;
						return;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				parent = frontier.pop();
				explored.insert(parent);
				children = childNode(parent.name,"DFS");

				if(isDestination(parent)) {
					try {
						fo.append(parent.name + " " + (parent.pathCost)%24 + "\n");
						flag = false;
						return;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
				
				// If no children
				if((children == null)&& !isDestination(parent)) {
					depthCount--;
					continue;
				}
				parent.setChildren(children.length);
				
				// Finding and displaying children 
				Node n1;
				for(int i = 0 ; i < children.length ; i++) {
					System.out.println(children[i]);
					temp1 = children[i].split(" ");
					String offTimes = "";
					if(Integer.parseInt(temp1[3]) != 0){
						for(int j = 4; j < (4 + Integer.parseInt(temp1[3])) ; j++) {
							offTimes += temp1[j] + " ";
						}
					}
					tempCost = parent.pathCost + 1;
					System.out.println("Temp Cost = " + tempCost);
					n1 = new Node(temp1[1],depthCount,tempCost);
					n1.setOffTimes(Integer.parseInt(temp1[3]),offTimes);
					parent.children[i] = n1;
					n1.parent = parent;
					
					// Checks if node is present in the frontier or the explored
					if (!((frontier.isPresent(n1)) || (explored.isPresent(n1)))) {
					//if (!(explored.isPresent(n1))) {
						frontier.insert(n1);	
					}
				}
							
			}while(flag);
		} // End of else	
	} // End of DFS

	//////////////////////////////////////////////////////////////////////////////////////////////
	// Performs the UCS alogrithm
	public static void UCS() {
		System.out.println("---------- U C S -----------");
		String[] children = null;
		String[] temp1,temp2;  // Temporary arrays
		Node parent;  // Parent node reference
		int count = 0; 
		int cost = startTime,tempCost = 0;
		int cap = 5 + destination.length + middleNodes.length;
		
		// Creating a priority queue for UCS  
		UCSPriorityQueue frontier = new UCSPriorityQueue(cap);
		
		// Use BFSQueue for explored because it is a normal queue and not a priority queue 
		BFSQueue explored = new BFSQueue(cap);
		
		Node n = new Node(source,0,cost);
		n.setOffTimes(0,"");
		n.parent = null;
		frontier.insert(n);
		
		do {
			count++;
			children = null;
			System.out.println("Frontier till now");
			frontier.display();
			if(frontier.isEmpty()) {
				System.out.println("Frontier is Empty");
				try {
					fo.append("None\n");
					flag = false;
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			parent = frontier.pop();
			System.out.println("Parent =>"+ parent.name);
			if(isDestination(parent)){
				try {
					explored.insert(parent);
					explored.displayQueue();
					System.out.println("Writing to Output file");
					System.out.println("Parent and cost " + parent.name + " " + (parent.pathCost%24));
					fo.append(parent.name + " " + (parent.pathCost%24) + "\n");
					flag = false;
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			explored.insert(parent);
			children = childNode(parent.name,"UCS");
			
			// Condition for no children
			if(children == null && !isDestination(parent)) {
				if(!frontier.isEmpty()){
					parent = frontier.pop();
					if(isDestination(parent)){
						try {
							explored.insert(parent);
							explored.displayQueue();
							System.out.println("Writing to Output file");
							System.out.println("Parent and cost " + parent.name + " " + (parent.pathCost%24));
							fo.append(parent.name + " " + (parent.pathCost%24) + "\n");
							flag = false;
							return;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}					
				}
				children = childNode(parent.name,"UCS");
			}

			parent.setChildren(children.length);
			Node n1;
			for(int i = 0 ; i < children.length ; i++){
				tempCost = 0;
				temp1 = children[i].split(" ");
				String offTimes = "";
				if(Integer.parseInt(temp1[3]) != 0){
					for(int j = 4; j < (4 + Integer.parseInt(temp1[3])) ; j++) {
						offTimes += temp1[j] + " ";
					}
				}
				tempCost = parent.pathCost + Integer.parseInt(temp1[2]);
				System.out.println("Temp Cost = " + tempCost);
				n1 = new Node(temp1[1],count,tempCost);
				n1.setOffTimes(Integer.parseInt(temp1[3]),offTimes);
				parent.children[i] = n1;
				n1.parent = parent;
				System.out.println("Parent and cost " + parent.pathCost + " => " + n1.parent.name);
				
				// Checking for off time condition
				if(n1.isOffTime(n1.parent.pathCost%24)) {
					continue;
				}
						
				if (!((frontier.isPresent(n1)) || (explored.isPresent(n1)))) {
					frontier.insert(n1);
				}
				else if((frontier.isPresent(n1)) && (n1.pathCost < frontier.getCost(n1))){
					System.out.println("Cost of existing node = " + frontier.getCost(n1));
					frontier.replace(n1);
				}
				else if ((n1.pathCost >= frontier.getCost(n1)) && (!frontier.isEmpty())){
					continue;
				}
				else {
					try {
						fo.append("None\n");
						flag = false;
						return;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
			}				
		}while(flag);
	}// End of UCS
	
	// Calculation of the children nodes
	public static String[] childNode(String p,String type) {
		String[] temp1,temp2,temp3 = null;
		String children = "";
		String[] childrenArray;
		for(int i = 0 ; i < graph.length; i++){
			temp1 = graph[i].split(" ");
			if (temp1[0].equalsIgnoreCase(p)){
				children += graph[i] + "----";
			}			
		}
		if(children.equals("")) {
			childrenArray = null;
			return childrenArray;
		}
		System.out.println(children);
		
		// Sorting
		temp1 = null;
		temp2 = null;
		String temp = "";
		childrenArray = children.split("----");
		System.out.println(childrenArray.length);
		for(int i = 0 ; i < childrenArray.length ; i++){
			for(int j = 0 ; j < childrenArray.length ; j++) {
				//To remove exception
				if(j == (childrenArray.length - 1)) {
					break;
				}
				temp1 = childrenArray[j].split(" ");
				temp2 = childrenArray[j+1].split(" ");
				System.out.println(temp1[1].charAt(0) + "  " + temp2[1].charAt(0));
				int min = 0;
				if(temp1[1].length() <= temp2[1].length()) {
					min = temp1[1].length();
				}
				else {
					min = temp2[1].length();
				}
				if((type.equals("BFS")) || (type.equals("UCS")) || type.equals("DFS")) {

					if(temp1[1].compareToIgnoreCase(temp2[1]) >= 0) {
						temp = childrenArray[j];
						childrenArray[j] = childrenArray[j+1];
						childrenArray[j+1] = temp;
					}
				
				}
			} // End of Inner for loop
			
		} // End of Outer for loop
		
		// Reversing the order for DFS
		if(type.equals("DFS")) {
			String reverse[] = new String[childrenArray.length];
			int count = childrenArray.length-1;
			for (int i = 0 ; i < childrenArray.length ; i++){
				if(count == -1){
					break;
				}
				reverse[count] = childrenArray[i];
				count--;
			}
			// Assigning the reverse array to childrenArray
			childrenArray = reverse;
		}
	
		for(int i = 0 ; i < childrenArray.length ; i++){
			System.out.println("This is the child array");
			System.out.println(childrenArray[i]);
		}
		// For UCS
		return childrenArray;
	}

} // End of Waterflow class 


///////////////////////////////////////////////////////////////////////////////////
// BFS Queue
class BFSQueue {
	private int front,rear,capacity;
	private Node q[];
	public BFSQueue(int capacity){
		this.capacity = capacity;
		q = new Node[capacity];
		front = rear = -1;		
	}
	
	public boolean isEmpty() {
		if((rear == -1) || (front == rear + 1)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void insert(Node a){
		if(rear == (q.length - 1)) {
			System.out.println("**** Queue Overflow ****");
			return;
		}
		rear++;
		q[rear] = a;
		if(front == -1){
			front++;
		}
	}
	
	public Node pop() {
		if(isEmpty()){
			System.out.println("**** Queue Empty ****");
			return null;
		}
		Node z = q[front];
		front++;
		return z;
	}
	
	public boolean isPresent(Node a){
		for(int i = front ; i <= rear ; i++){
			if((q[i].name).equals(a.name)){
				return true;
			}
		}
		return false;
	}
	
	public void displayQueue() {
		if(isEmpty()) {
			System.out.println("Queue Empty");
			return;
		}
		System.out.println("******************************");
		for(int i = front ; i <= rear ; i++) {
			System.out.print(q[i].name+"=>");
		}
		System.out.println();
	}
}// End of BFSQueue

/////////////////////////////////////////////////////////////////////////////////
// Priority Queue for UCS
class UCSPriorityQueue {
	int front,rear,capacity,cost;
	Node[] q;
	public UCSPriorityQueue(int capacity){
		this.capacity = capacity;
		System.out.println("Creating array of Nodes");
		q = new Node[capacity];
		System.out.println("Instantiation of array over");
		front = rear = -1;
	}

	public boolean isPresent(Node a) {
		for(int i = front ; i <= rear ; i++) {
			if((a.name).equals(q[i].name)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isEmpty(){
		if((rear == -1) || (front == rear + 1 )) {
			return true;
		}
		return false;
	}
	
	public void insert(Node a){
		if(rear == (capacity - 1)){
			System.out.println("Queue Overflow");
			return;
		}	
		if (rear == -1) {
			rear++;
			q[rear] = a;
			front++;
			return;
		}
		
		int i;
		for( i = front ; i <= rear ; i++) {
			if(a.pathCost == q[i].pathCost) {
				if(a.name.charAt(0) < q[i].name.charAt(0)) {
					break;
				}
			}
			if(a.pathCost < q[i].pathCost) {
				break;
			}
		}
		for(int j = rear ; j >= i ; j--) {
			q[j+1] = q[j];
		}
		q[i] = a;
		rear++;
		System.out.println("Front = "+ front + " Rear = " + rear);
	}
	
	public void display() {
		for (int k = front ; k <= rear ; k++) {
			System.out.println(q[k].name);
		}
	}
	
	public Node pop() {
		if(isEmpty()) {
			System.out.println("Queue Underflow");
			return null;
		}
		Node a = q[front];
		front++;
		return a;
	}
	
	public int getCost(Node a) {
		System.out.println(front + " " + rear);
		for(int i = front ; i <= rear ; i++) {
			System.out.println(q[i].name + " => " + q[i].pathCost);
			if((a.name).equals(q[i].name)){
				return q[i].pathCost;
			}
		}
		return 0;
	}
	
	public void replace(Node a) {
		for(int i = front ; i <= rear ; i++) {
			if((a.name).equals(q[i].name)){
				q[i] = a;
				sort();
				display();
			}
		}
	}
	
	public void sort(){
		if(front == rear){
			return;
		}
		for(int i = front ; i < rear ; i++ ) {
			for (int j = front; j < rear ; j++) {
				if(q[j].pathCost > q[j+1].pathCost) {
					Node temp = q[j];
					q[j] = q[j+1];
					q[j+1] = temp;
				}
			}
		}
	}
	
	public Node frontNode() {
		if(isEmpty()){
			System.out.println("No Front Node . Queue Empty");
			return null;
		}
		Node z = q[front];
		return z;
	}	
}// End of UCSQueue

////////////////////////////////////////////////////////////////////////////
// Basic node for the tree or graph
class Node {
	Node parent;
	Node[] children;
	String name,offTimes;
	String[] closedTime;
	int depth,pathCost,countOffTimes;
	public Node(String name,int depth,int pathCost){
		this.name = name;
		this.depth = depth;
		this.pathCost = pathCost;
	}
	public void setChildren(int num){
		children = new Node[num];
	}
	public void setOffTimes(int countOffTimes, String offTimes){
		this.offTimes = offTimes;
		this.countOffTimes = countOffTimes;
		System.out.println("The off times are = " + offTimes);
		System.out.println("The count off times are = " + countOffTimes);
		
	}
	// Returns true if the pipe is not working
	public boolean isOffTime(int time) {
		int upper,lower;
		if(countOffTimes == 0){
			return false;
		}
		String[] tempOutside = offTimes.split(" ");
		String[][] tempInside = new String[countOffTimes][2];
		for(int i = 0 ; i < tempOutside.length ; i++) {
			tempInside[i] = tempOutside[i].split("-");
		}
		for(int i = 0 ; i < countOffTimes ; i++) {	
			lower = Integer.parseInt(tempInside[i][0]);
			upper = Integer.parseInt(tempInside[i][1]);
			if((time >= lower) && (time <= upper)){
				return  true;
			}
		}
		return false;
	}
}

//////////////////////////////////////////////////////////////////////////////
// Stack for DFS
class DFSQueue {
	private Node s[];
	private int cap,top;
	public DFSQueue(int cap) {
		this.cap = cap;
		s = new Node[cap];
		top = -1;
	}
	
	public boolean isEmpty() {
		if(top == -1) {
			System.out.println("Stack Empty");
			return true;
		}
		else {
			return false;
		}		
	}
	
	public void insert(Node a) {
		if(top == (cap - 1)) {
			System.out.println("Stack Overflow");
			return;
		}
		top++;
		s[top] = a;
	}
	
	public Node pop() {
		if (isEmpty()){
			System.out.println("Stack Empty");
			/////////////////////////////////////////////////////////////////////////////
			return null;
		}
		Node z = s[top];
		top--;
		return z;
	}
	public boolean isPresent(Node a) {
		for(int i = 0; i <= top ; i++ ){
			if ((s[i].name).equals(a.name)) {
				return true;
			}
		}
		return false;
	}
	
	public Node frontNode() {
		if (isEmpty()){
			System.out.println("Stack Empty");
			/////////////////////////////////////////////////////////////////////////////
			return null;
		}
		Node z = s[top];
		return z;
	}
	
	public void display() {
		System.out.println("----****Stack****----");
		for(int i = top ; i >= 0 ; i-- ) {
			System.out.println(s[i].name);
		}
	}
}// End of DFSQueue
