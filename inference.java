// Name: Ankeet Tendulkar
// Homework Assignment 3
/*
Developed a knowledge based inference engine. 
Given a knowledge base and a number of queries the goal was to find out if the queries could be inferred from the knowledge base or not.
The backward chaining algorithm was used to solve this problem.
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class inference {

	/**
	 * @param args
	 */
	static FileReader fr = null;
	static FileWriter fw = null;
	static BufferedReader br = null;
	static String Queries[];
	static Node KBConstants[] = null;
	static Node KBImplications[] = null;
	static ArrayList QueriesOuter,QueriesInner,substitutions,Explored,otherSubstitutionOuter,otherSubstitutionInner;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			fr = new FileReader(args[1]);
			File f = new File("output.txt");
			fw = new FileWriter(f,false);
			br = new BufferedReader(fr);
			
			String s;
			s = br.readLine();
			Queries = new String[Integer.parseInt(s)];
			for(int i = 0; i < Integer.parseInt(s); i++) {
				Queries[i] = br.readLine();
			}
			s = br.readLine();
			KBConstants = new Node[52];
			KBImplications = new Node[52];
			for(int i = 0; i < Integer.parseInt(s); i++) {
				insertInHashTable(br.readLine());
			}	
			//display();
			
			// Converting Queries to an ArrayList
			QueriesOuter = new ArrayList();
			for(int i = 0; i < Queries.length; i++) {
				//System.out.println(Queries[i].substring(0, Queries[i].indexOf("(")));
				QueriesInner = new ArrayList();
				QueriesInner.add(Queries[i].substring(0, Queries[i].indexOf("(")));
				QueriesInner.add("(");
				String t[];
				t = Queries[i].substring(Queries[i].indexOf("(") + 1, Queries[i].indexOf(")")).split(",");
				for(int j = 0; j < t.length; j++) {
					String x = new String(t[j]);
					QueriesInner.add(x);
				}
				QueriesInner.add(")");
				QueriesOuter.add(QueriesInner);
			}
						
			// Display Unification			
			for(int i = 0 ; i < QueriesOuter.size(); i++) {
				boolean result;
				Explored = null;
				Explored = new ArrayList();
				fw = new FileWriter(f,true);
				result = startBackwardChaining((ArrayList)QueriesOuter.get(i));
			
				String printResult = "";
				if(result) {
					printResult = "TRUE";
				}
				else {
					printResult = "FALSE";
				}
				System.out.println("Result = " + printResult);
				//fw = new FileWriter(f,true);
				fw.append(printResult + "\n");
				//display();
				fw.close();
				
			}
			fr.close();
					
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
		
	public static void display() {
		// Displaying Implications
		for(int i = 0; i < KBImplications.length; i++) {
			System.out.print(i + " ==> ");
			if(KBImplications[i] == null) {
				System.out.println("NULL");
			}
			else {
				displayHashTable(KBImplications[i]);
			}
		}
		
		// Displaying Constants
		for(int i = 0; i < KBConstants.length; i++) {
			System.out.print(i + " ==> ");
			if(KBConstants[i] == null) {
				System.out.println("NULL");
			}
			else {
				displayHashTable(KBConstants[i]);
			}
		}
	}
	public static void insertInHashTable(String t) {
		int hashValue = 0;
		char current;
		Node n = new Node(t);
		n.initializeNode();
		if(n.type.equals("Implication"))
			n.setParameters();
		if (n.type.equals("Implication")) {
			current = n.consequent.charAt(0);
			if(current == '~') {
				current = n.consequent.charAt(1);
				hashValue = (current % 65) + 26;
			}
			else {
				hashValue = current % 65;
			}
			if(KBImplications[hashValue] == null) {
				n.serialNumber = 0;
				KBImplications[hashValue] = n;
			}
			else {
				int c = 0;
				Node q = KBImplications[hashValue];
				while(q.next != null) {
					c++;
					q = q.next;
				}
				n.serialNumber = c;
				q.next = n;
			}			
		}
		if (n.type.equals("Constant")) {
			current = ((String)n.constants.get(0)).charAt(0);
			if(current == '~') {
				current = ((String)n.constants.get(0)).charAt(1);
				hashValue = (current % 65) + 26;
			}
			else {
				hashValue = current % 65;
			}
			if (KBConstants[hashValue] == null) {
				n.serialNumber = 0;
				KBConstants[hashValue] = n;
			}
			else {
				int co = 0;
				Node q = KBConstants[hashValue];
				while(q.next != null) {
					co++;
					q = q.next;
				}
				n.serialNumber = co;
				q.next = n;
			}
		}
	}
	
	public static void displayHashTable(Node n) {
		Node q = n;
		int count = 0;
		while (q != null) {
			System.out.print(count + " ==> ");
			if(q.type.equals("Implication")) {
				System.out.println("Consequent = " + q.consequent + "Predicate = " + (String)((ArrayList)q.predicateParametersOuter.get(0)).get(0));
			}
			if(q.type.equals("Constant")) {
				System.out.println("Constant = " + q.constants);
			}
			q = q.next;
			count++;
		}
	}
		
	public static boolean startBackwardChaining(ArrayList Query) { 
		//System.out.println("Query = " + Query);
		if(Explored.contains(Query)) {
			return false;
		}
		String r = null;
		for(int l = 0; l < Query.size(); l++) {
			r += ((String)Query.get(l)) + " ";
		}
		String r1 = r.substring(r.indexOf("(") + 1,r.indexOf(")") - 1);
		if(!isLowerCase(r1))	{	
			Explored.add(Query);
		}
		int countFacts = 0;
		boolean returnFlag;
		char temp = ((String)Query.get(0)).charAt(0);
		Node n,p = null;
		if(temp == '~') {
			n = KBConstants[(((String)Query.get(0)).charAt(1)%65) + 26];
		}
		else {
			n = KBConstants[temp%65];
		}

		int trueRound = 0;
		otherSubstitutionOuter = new ArrayList();
		while(n != null) {
			int i = 0;
			returnFlag = true;			
			String name1 = null, name2 = null;
			otherSubstitutionInner = new ArrayList();
			name1 = (String)n.constants.get(0);
			name2 = (String)Query.get(0);
			if(name1.equals(name2)) {
				if(n.constants.size() == Query.size()) {
					for(i = 2; i < n.constants.size() - 1; i++) {
						String temp1 = null, temp2 = null;
						temp1 = (String)n.constants.get(i);
						temp2 = (String)Query.get(i);
						if(isUpperCase(temp2.charAt(0)) && isUpperCase(temp1.charAt(0))) {
							//Means it is a constant
							if(!temp1.equals(temp2)) {
								if( n.next == null) {
//									if(trueRound == 0)
//										return false;
								}
								else {
									returnFlag = false;
								}
								if(n.next == null && !temp1.equals(temp2)) {
									returnFlag = false;
								}
							}
						}
						if(!isUpperCase(temp2.charAt(0)) && isUpperCase(temp1.charAt(0))) {
							//Replace with constant
							if(returnFlag) {	
								otherSubstitutionInner.add(temp2);
								otherSubstitutionInner.add(temp1);
							}					
						}	
					}
					
					if(!returnFlag) {
						otherSubstitutionInner = new ArrayList();
					}
					if(!otherSubstitutionInner.isEmpty()) {
						otherSubstitutionOuter.add(otherSubstitutionInner);
					}
					if(returnFlag && otherSubstitutionOuter.isEmpty()) {
						return true;
					}
				}
			}
			else {
				n = n.next;
			}
			if(returnFlag && !otherSubstitutionOuter.isEmpty()) {
				trueRound++;
			}
			if(n != null) {
				if((i == (n.constants.size() - 1))){
						n = n.next;
				}
			}
		}
		if(!otherSubstitutionOuter.isEmpty()) {
			return true;
		}

		boolean rT,returnType = false,OR = false;
		char temp1 = ((String)Query.get(0)).charAt(0);
		Node m = null;
		if(temp1 == '~') {
			m = KBImplications[(((String)Query.get(0)).charAt(1)%65) + 26];
		}
		else {
			m = KBImplications[temp1%65];
		}
		
		while(m != null && !OR) {

			Node n1 = new Node(m);
			// Check if the name matches
			if (((String)n1.consequentParameters.get(0)).equals((String)Query.get(0))) {
				if(n1.consequentParameters.size() == Query.size()) {	
					for(int i = 2; i < n1.consequentParameters.size() - 1; i++) {
						if(!isLowerCase((String)Query.get(i))) {
							String temporary = (String)n1.consequentParameters.get(i);												
							Collections.replaceAll(n1.consequentParameters,temporary,(String)Query.get(i));
							// for all in the outer loop
							for(int j = 0; j < n1.predicateParametersOuter.size(); j++) {
								ArrayList t = (ArrayList)n1.predicateParametersOuter.get(j); 
								if(t.contains(temporary)) {
									Collections.replaceAll(t,temporary,(String)Query.get(i));
									//System.out.println((String)t.get(2));
								}
							}
						}
					}
					for(int i = 2; i < n1.consequentParameters.size() - 1; i++) {
						if(isLowerCase((String)Query.get(i))) {
							String temporary = (String)n1.consequentParameters.get(i);												
							Collections.replaceAll(n1.consequentParameters,temporary,(String)Query.get(i));
							// for all in the outer loop
							for(int j = 0; j < n1.predicateParametersOuter.size(); j++) {
								ArrayList t = (ArrayList)n1.predicateParametersOuter.get(j); 
								if(t.contains(temporary)) {
									Collections.replaceAll(t,temporary,(String)Query.get(i));
									//System.out.println((String)t.get(2));
								}
							}
						}
					}
				}
			}
			// for all implications
			// for all substitutions
			// for all conjuncts
			if (((String)n1.consequentParameters.get(0)).equals((String)Query.get(0))) {
				do {
					returnType = true;
					makeSubstitution(n1);
					for(int k = 0; k < n1.predicateParametersOuter.size(); k++) {
						ArrayList q = new ArrayList();
						q = (ArrayList)n1.predicateParametersOuter.get(k);
						rT = startBackwardChaining(q);
						
						if(!Explored.isEmpty()) {
							Explored.remove(Explored.size() - 1);			
						}
						
						if(!otherSubstitutionOuter.isEmpty()) {
							makeClone(n1);
						}
						if(rT && !n1.possibleSubstitutionOuter.isEmpty()) {
							makeSubstitution(n1);
						}
						
						if(!rT && !n1.removals.isEmpty()) {
							makeRemoval(n1);
						}
						
						returnType = returnType && rT;
						if(!returnType) {
							break;
						}
					}
					if(returnType) {
						if(!Explored.isEmpty()) {
							Explored.remove(Explored.size() - 1);			
						}
						break;
					}
				}while(!n1.possibleSubstitutionOuter.isEmpty());
			}
			if(returnType) {
				return true;
			}
			
			if(m.next == null) {	
				if(returnType == false) {
					OR = false;
					return false;
				}
			}
			
			m = m.next;
		}
		return false;
	}
	
	public static boolean isLowerCase(String q1) {
		String q2[] = q1.split(" ");
		for(int i = 0; i < q2.length; i++) {
			if(!q2[i].equals("")) {
				if(!isUpperCase(q2[i].charAt(0))) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static void makeClone(Node n1) {
		n1.possibleSubstitutionOuter = new ArrayList();
		for(int i = 0; i < otherSubstitutionOuter.size(); i++) {
			ArrayList inner = (ArrayList)otherSubstitutionOuter.get(i);
			n1.possibleSubstitutionInner = new ArrayList();
			for(int j = 0; j < inner.size(); j++) {
				n1.possibleSubstitutionInner = (ArrayList)inner.clone();
			}
			n1.possibleSubstitutionOuter.add(n1.possibleSubstitutionInner);
		}
	}
	
	public static void makeSubstitution(Node n1) {
		if(!n1.possibleSubstitutionOuter.isEmpty()) {
			ArrayList t1 = (ArrayList)n1.possibleSubstitutionOuter.get(0);
			for(int i = 0; i < t1.size(); i += 2) {
				for(int j = 0; j < n1.predicateParametersOuter.size(); j++) {
					ArrayList t2 = (ArrayList)n1.predicateParametersOuter.get(j); 
					if(t2.contains((String)t1.get(i))) {
						Collections.replaceAll(t2,(String)t1.get(i),(String)t1.get(i + 1));
					}
				}
				if(n1.consequentParameters.contains((String)t1.get(i))) {
					Collections.replaceAll(n1.consequentParameters, (String)t1.get(i), (String)t1.get(i + 1));
				}
				n1.removals.add((String)t1.get(i));
				n1.removals.add((String)t1.get(i + 1));
			}
			
			n1.possibleSubstitutionOuter.remove(0);
		}
	}
	
	public static void makeRemoval(Node n1) {
		for(int i = 0; i < n1.removals.size(); i += 2) {
			for(int j = 0; j < n1.predicateParametersOuter.size(); j++) {
				ArrayList t2 = (ArrayList)n1.predicateParametersOuter.get(j); 
				if(t2.contains((String)n1.removals.get(i + 1))) {
					Collections.replaceAll(t2,(String)n1.removals.get(i + 1),(String)n1.removals.get(i));
				}
			}
			if(n1.consequentParameters.contains((String)n1.removals.get(i + 1))) {
				Collections.replaceAll(n1.consequentParameters, (String)n1.removals.get(i + 1), (String)n1.removals.get(i));
			}
			if(!n1.removals.isEmpty())
				n1.removals.remove(0);
			if(!n1.removals.isEmpty())
				n1.removals.remove(0);
		}
		n1.removals = new ArrayList();
	}
	
	public static boolean isUpperCase(char a){
		if (a >= 'A' && a <= 'Z') {
			return true;
		}
		else {
			return false;
		}
	}
	
}

@SuppressWarnings("unchecked")
class Node {
	Node next;
	String predicates[], consequent,t, temp1[], lhs = "",temp2[], type = null;
	ArrayList predicateParametersOuter;
	ArrayList predicateParametersInner;
	ArrayList consequentParameters;
	ArrayList constants;
	ArrayList substitutionsNode = new ArrayList();
	ArrayList possibleSubstitutionOuter = new ArrayList();
	ArrayList possibleSubstitutionInner = new ArrayList();
	ArrayList removals = new ArrayList();
	
	String lhsParametersTemp[],lhsParameters,rhsParameters,rhsParametersTemp,cons[];
	int serialNumber;
	boolean checked = false;
	public Node(String s) {
		t = s;
		next = null;
	}
	
	public Node(Node a) {
		this.consequent = a.consequent;
		this.consequentParameters = (ArrayList)a.consequentParameters.clone();
		if(a.constants != null)
			this.constants = (ArrayList)a.constants.clone();
		this.lhs = a.lhs;
		this.lhsParameters = a.lhsParameters;
		this.lhsParametersTemp = a.lhsParametersTemp;
		this.next = a.next;
		this.predicateParametersOuter = new ArrayList();
		for(int i = 0; i < a.predicateParametersOuter.size(); i++) {
			ArrayList inner = (ArrayList)a.predicateParametersOuter.get(i);
			this.predicateParametersInner = new ArrayList();
			for(int j = 0; j < inner.size(); j++) {
				this.predicateParametersInner = (ArrayList)inner.clone();
			}
			this.predicateParametersOuter.add(this.predicateParametersInner);
		}
		this.predicates = a.predicates;
		this.rhsParameters = a.rhsParameters;
		this.rhsParametersTemp = a.rhsParametersTemp;
		this.t = a.t;
		this.temp1 = a.temp1;
		this.temp2 = a.temp2;
		this.type = a.type;
		this.constants = a.constants;
		this.checked = a.checked;
		if(!a.substitutionsNode.isEmpty())
			this.substitutionsNode = (ArrayList)a.substitutionsNode.clone();
		if(!a.possibleSubstitutionOuter.isEmpty()) {
			for(int i = 0; i < a.possibleSubstitutionOuter.size(); i++) {
				ArrayList inner = (ArrayList)a.possibleSubstitutionOuter.get(i);
				this.possibleSubstitutionInner = new ArrayList();
				for(int j = 0; j < inner.size(); j++) {
					this.possibleSubstitutionInner = (ArrayList)inner.clone();
				}
				this.possibleSubstitutionOuter.add(this.possibleSubstitutionInner);
			}
		}
		this.serialNumber = a.serialNumber;
		this.removals = (ArrayList)a.removals.clone();
	}
	
	public void initializeNode() {
		
		if(t.contains("=>")){
			// This must be an implication
			type = "Implication";
			temp1 = t.split(" ");
			int i = 0;
			while(!temp1[i].equals("=>")) {
				lhs += temp1[i];
				i++;
			}
			consequent = temp1[i+1];
			if(lhs.contains("^")) {
				predicates = lhs.split("\\^");		
			}
			else {
				predicates = new String[1];
				predicates[0] = lhs;
			}
		}
		else {
			// This must be a constant
			type = "Constant";
			constants =  new ArrayList();
			constants.add(t.substring(0, t.indexOf("(")));
			constants.add("(");
			cons = (t.substring(t.indexOf("(") + 1, t.indexOf(")"))).split(",");
			for(int i = 0; i < cons.length; i++) {
				String x = new String(cons[i]);
				constants.add(x);
			}
			constants.add(")");
		}
	}
		
	public void setParameters(){
		rhsParameters = consequent.substring(consequent.indexOf("(") + 1, consequent.indexOf(")"));
		temp2 = rhsParameters.split(",");
		consequentParameters = new ArrayList();
		consequentParameters.add(consequent.substring(0, consequent.indexOf("(")));
		consequentParameters.add("(");
		for(int i = 0; i < temp2.length; i++) {
			consequentParameters.add(temp2[i]);
		}
		consequentParameters.add(")");	
		predicateParametersOuter = new ArrayList();
		for(int i = 0; i < predicates.length; i++) {
			predicateParametersInner = new ArrayList();
			predicateParametersInner.add(predicates[i].substring(0, predicates[i].indexOf("(")));
			predicateParametersInner.add("(");
			lhsParameters = predicates[i].substring(predicates[i].indexOf("(") + 1, predicates[i].indexOf(")"));
			if(lhsParameters.contains(",")) {
				lhsParametersTemp = lhsParameters.split(",");
				for(int j = 0; j < lhsParametersTemp.length; j++) {
					String x = new String(lhsParametersTemp[j]);
					predicateParametersInner.add(x);
				}
			}
			else {
				String x = new String(lhsParameters);
				predicateParametersInner.add(x);
			}
			predicateParametersInner.add(")");
			predicateParametersOuter.add(predicateParametersInner);
		}
	}
}