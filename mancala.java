// Name: Ankeet Tendulkar
// Homework Assignment 2
/*
The goal of this project was to find out the best next move of a player in a game given the current state of the game. 
The game taken into consideration for this project was Mancala. 
This project involved making use of Artificial Intelligent algorithms like the Greedy, Mini-Max and Alpha-Beta Pruning for finding the best next move for the player.
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class mancala {

	static boolean isGreedy;
	static int evalPlayer,evalOtherPlayer;
	public static ArrayList childNodes;
	static FileReader fr = null;
	static int lineNo = 1,lineNoAlphaBeta = 1;
	public static BufferedReader br = null;
	public static File f1,f2;
	public static FileWriter fw1 = null,fw2 = null,fw3 = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s;
		try {
			fr = new FileReader(args[1]);
			br = new BufferedReader(fr);
			f1 = new File("next_state.txt");
			f2 = new File("traverse_log.txt");
			fw1 = new FileWriter(f1,false);
			fw2 = new FileWriter(f2,false);
			while((s = br.readLine()) != null) {
				// If input is not a number then handle
				if(s.equals("")){
					System.out.println("EmptyLine");
					continue;
				}
				switch(Integer.parseInt(s)) {
					case 1: Node n1 = new Node();
					        isGreedy = true;
					        fw1 = new FileWriter(f1,true);
							initialize(n1);
							//display(n1);
							childNodes = null;
							MinimaxDecision(n1, 1);
							fw1.close();
							break;
				    
					case 2: Node n2 = new Node();
							isGreedy = false;
					        fw1 = new FileWriter(f1,true);
							fw2 = new FileWriter(f2,true);
							initialize(n2);
							//display(n2);
							childNodes = null;
							MinimaxDecision(n2, n2.cutoffDepth);
							//display(n2);
							fw1.close();
							fw2.close();
							break;
						
					case 3: Node n3 = new Node();
							isGreedy = false;
					        fw1 = new FileWriter(f1,true);
							fw2 = new FileWriter(f2,true);
					        initialize(n3);
					        //display(n3);
					        childNodes = null;
					        AlphaBetaMinimaxDecision(n3, n3.cutoffDepth);
					        //display(n3);
					        fw1.close();
					        fw2.close();
					        break;
						
					case 4:// Competition
							break;
				}
			}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void initialize(Node n) {
		
		String temp[];
		try {
			n.playerNo = Integer.parseInt(br.readLine());
			
			if (n.playerNo == 2){
				n.otherPlayerNo = 1;
				n.playerIndex = 0;
				n.otherPlayer = 1;
				n.playerName = "A";
				n.opponentName = "B";
			}
			if (n.playerNo == 1) {
				n.otherPlayerNo = 2;
				n.playerIndex = 1;
				n.otherPlayer = 0;
				n.playerName = "B";
				n.opponentName = "A";
			}
			
			// Global variables 
			evalPlayer = n.playerIndex;
			evalOtherPlayer = n.otherPlayer;
			
			n.cutoffDepth = Integer.parseInt(br.readLine());
		
			// For P2
			temp = (br.readLine()).split(" ");
			n.setBoardLength(temp.length);
			n.boardState[0] = convertToInteger(temp);
			temp = (br.readLine()).split(" ");
			n.boardState[1] = convertToInteger(temp);
			// Index 0 is for player 1 and index 1 is for player 2
			n.mancala[0] = Integer.parseInt(br.readLine());
			n.mancala[1] = Integer.parseInt(br.readLine());

			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int[] convertToInteger(String a[]) {
		int b[] = new int[a.length];
		for(int i = 0 ; i < a.length ; i++) {
			b[i] = Integer.parseInt(a[i]);
		}
		return b;
	}
	
	public static void display(Node a){
		System.out.println("Player No: " + a.playerNo);
		System.out.println("Cut off depth: " + a.cutoffDepth);
		for(int i = 0 ; i < 2 ; i++) {
			for(int j = 0 ; j < a.cap ; j++) {
				System.out.print(a.boardState[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("Player 2 Mancala: " + a.mancala[0]);
		System.out.println("Player 1 Mancala: " + a.mancala[1]);
	}
		
	public static void startGreedy(Node temp, int i) {
		int count = temp.boardState[temp.playerIndex][i];
		if(isLegal(count)){
			if(temp.playerIndex == 1) {
				temp.terminal = false;
				temp.otherPlayer = 0;
				temp.shiftForPlayer1(i, count);
				if(isTerminal(temp,temp.playerIndex) || isTerminal(temp,temp.otherPlayer)) {
					temp.terminal = true;
					if(temp.repeat == true) {
						temp.recursion = true;
					}
					updateState(temp,temp.playerIndex,temp.otherPlayer);
					temp.repeat = false;
					return;
				}
				else {
					return;
				}
			}
			if(temp.playerIndex == 0) {
				temp.terminal = false;
				temp.otherPlayer = 1;
				temp.shiftForPlayer2(i, count);
				if(isTerminal(temp,temp.playerIndex) || isTerminal(temp,temp.otherPlayer)) {
					temp.terminal = true;
					if(temp.repeat == true) {
						temp.recursion = true;
					}
					updateState(temp,temp.playerIndex,temp.otherPlayer);
					temp.repeat = false;
					return;
				}
				else {
					return;
				}
				
			}
		}
		return;		
	}
	
	public static boolean isTerminal(Node temp, int playerIndex) {
		int i;
		for(i = 0 ; i < temp.cap ; i++) {
			if(temp.boardState[playerIndex][i] != 0) {
				return false;
			}
		}
		return true;
	}
	
	public static Node updateState(Node temp, int playerIndex,int otherPlayer) {
		int remainingStones = 0;
		for( int i = 0; i < temp.cap; i++ ) {
			remainingStones += temp.boardState[otherPlayer][i];
			temp.boardState[otherPlayer][i] = 0;
			
		}
		temp.mancala[otherPlayer] += remainingStones;
		remainingStones = 0;
		for( int i = 0; i < temp.cap; i++ ) {
			remainingStones += temp.boardState[playerIndex][i];
			temp.boardState[playerIndex][i] = 0;	
		}
		temp.mancala[playerIndex] += remainingStones;		
		return temp;
	}
	
	
	public static Node findMax() {
		int max = Integer.MIN_VALUE;
		int value;
		Node temp = null;
		Node maxNode = null;
		maxNode = (Node)childNodes.get(0);
		max = maxNode.evalFunc;
		for(int i = 1 ; i < childNodes.size(); i++) {
			temp = (Node)childNodes.get(i);
			value = temp.evalFunc;
			if(value > max) {
				maxNode = temp;
				max = value;
			}
		}
		return maxNode;
	}	
	
	public static boolean isLegal(int t){
		if (t == 0) {
			return false;
		}
		else if(t > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Minimax And Greedy 
	public static void MinimaxDecision(Node a,int depth) {
		Node temp,maximumNode;

		temp = new Node(a);	
		temp.cutoffDepth = depth;

		childNodes = new ArrayList();
		try {
			fw2.append("Node,Depth,Value\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MaxValue(temp,temp.cutoffDepth,1,0,evalPlayer,evalOtherPlayer,false);
		maximumNode = findMax();
		fileNextState(maximumNode);
	}
	
	public static void fileNextState(Node a) {
		try {
			for(int i = 0 ; i < 2 ; i++) {
				for(int j = 0 ; j < a.cap ; j++) {
					fw1.append(a.boardState[i][j] + " ");
				}
				fw1.append("\n");
			}
			fw1.append(a.mancala[0] + "\n");
			fw1.append(a.mancala[1] + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static void displayChildren() {
		for(int i = 0; i < childNodes.size(); i++ ){
			Node temp = (Node)childNodes.get(i);
			display(temp);
		}
	}
		
	public static int MaxValue(Node m,int depth,int c,int k, int playerIndex,int op, boolean isRepeat) {
		
		int tempPlayerIndex = playerIndex;
		int tempOtherPlayer = op;
		int count,value,otherPlayer;
		otherPlayer = op;
		count  = c;
		value = Integer.MIN_VALUE;
		
		// Cut Off State
		if(count == (depth + 1)) {
			//displayMinimaxTraverse(otherPlayer, k, count, value,false);
			displayMinimaxTraverse(otherPlayer, k, count, m.getEvalValue(evalPlayer,evalOtherPlayer),false);
			return m.getEvalValue(evalPlayer,evalOtherPlayer);
		}
		
		if(isEmpty(m) && m.terminal && m.recursion){
			//displayMinimaxTraverse(otherPlayer, k, count, value, isRepeat);
			value = m.getEvalValue(evalPlayer,evalOtherPlayer);
			displayMinimaxTraverse(otherPlayer, k, count, value, isRepeat);
			return m.getEvalValue(evalPlayer,evalOtherPlayer);
		}
		
		if(isEmpty(m) && !m.terminal && !m.recursion){
			//displayMinimaxTraverse(otherPlayer, k, count, value, isRepeat);
			value = m.getEvalValue(evalPlayer,evalOtherPlayer);
			displayMinimaxTraverse(otherPlayer, k, count, value, isRepeat);
			return m.getEvalValue(evalPlayer,evalOtherPlayer);
		}
		
		if(isEmpty(m) && m.terminal && !m.recursion){
			//displayMinimaxTraverse(otherPlayer, k, count, value, isRepeat);
			value = m.getEvalValue(evalPlayer,evalOtherPlayer);
			displayMinimaxTraverse(otherPlayer, k, count, value, isRepeat);
			return m.getEvalValue(evalPlayer,evalOtherPlayer);
		}
					
		for(int j = 0 ; j < m.cap ; j++) {
			Node newNode = new Node(m);
						
			newNode.playerIndex = playerIndex;
			if(isLegal(m.boardState[playerIndex][j])){
				playerIndex = tempPlayerIndex;
				otherPlayer = tempOtherPlayer;
				if(count == 1 && !isRepeat) {
					displayMinimaxTraverse(-1, j, count, value,isRepeat);
				}
				else {
					displayMinimaxTraverse(otherPlayer, k, count, value,isRepeat);
				}
				startGreedy(newNode,j);
				if(isRepeat) {
					if(playerIndex == 0) {
						otherPlayer = 1;
					}
					if(playerIndex == 1) {
						otherPlayer = 0;
					}
				}
				if(newNode.repeat) {
					value = Max(value,MaxValue(newNode, depth, count,j,playerIndex,playerIndex,true));
				}
				else if (newNode.terminal) {
					if(!newNode.recursion) {
			
						value = Max(value,MinValue(newNode, depth, count,j,otherPlayer,playerIndex,true));
					}
					else {
						value = Max(value,MaxValue(newNode, depth, count,j,otherPlayer,playerIndex,true));
					}
					newNode.evalFunc = value;
					if(count == 1) {
						childNodes.add(newNode);
					}
				}
				else {
					value = Max(value,MinValue(newNode, depth,count + 1,j,otherPlayer,playerIndex,false));
					newNode.evalFunc = value;
					if(count == 1) {
						childNodes.add(newNode);
					}
				}
			}
		}
		
		playerIndex = tempPlayerIndex;
		otherPlayer = tempOtherPlayer;
		
		if(count == 1 && !isRepeat) {
			displayMinimaxTraverse(-1, k, count, value,isRepeat);
		}
				
		else if(isEndGame(m,playerIndex) || isEndGame(m,otherPlayer)) {
			//displayMinimaxTraverse(otherPlayer, k, count, value, isRepeat);
			value = updateBoardAfterEndGame(m,otherPlayer,playerIndex);
			displayMinimaxTraverse(otherPlayer, k, count, value, isRepeat);
		}
		else {			
			displayMinimaxTraverse(otherPlayer, k, count, value,isRepeat);
		}
		return value;
	}
	
	public static int MinValue(Node n, int depth, int c, int h, int playerIndex, int op, boolean isRepeat) {

		int count,value,otherPlayer;
		int tempPlayerIndex = playerIndex;
		int tempOtherPlayer = op;
		otherPlayer = op;
		count = c; 
		value = Integer.MAX_VALUE;
		
		if(count == (depth + 1)) {
			//displayMinimaxTraverse(otherPlayer, h, count, value, false);
			displayMinimaxTraverse(otherPlayer, h, count, n.getEvalValue(evalPlayer,evalOtherPlayer),false);
			return n.getEvalValue(evalPlayer,evalOtherPlayer);
		}
		
		if(isEmpty(n) && n.terminal && n.recursion){
			//displayMinimaxTraverse(otherPlayer, h, count, value, isRepeat);
			value = n.getEvalValue(evalPlayer,evalOtherPlayer);
			displayMinimaxTraverse(otherPlayer, h, count, value, isRepeat);
			return n.getEvalValue(evalPlayer,evalOtherPlayer);
		}
		
		if(isEmpty(n) && !n.terminal && !n.recursion) {
			value = n.getEvalValue(evalPlayer,evalOtherPlayer);
			displayMinimaxTraverse(otherPlayer, h, count, value, isRepeat);
			return n.getEvalValue(evalPlayer,evalOtherPlayer);
		}
		
		if(isEmpty(n) && n.terminal && !n.recursion) {
			//displayMinimaxTraverse(otherPlayer, h, count, value, isRepeat);
			value = n.getEvalValue(evalPlayer,evalOtherPlayer);
			displayMinimaxTraverse(otherPlayer, h, count, value, isRepeat);
			return n.getEvalValue(evalPlayer,evalOtherPlayer);
		}
		
		//for all children
		for(int i = 0 ; i < n.cap ; i++) {
			Node newNode = new Node(n);
						
			newNode.playerIndex = playerIndex;
			if(isLegal(n.boardState[playerIndex][i])){
				playerIndex = tempPlayerIndex;
				otherPlayer = tempOtherPlayer;
				displayMinimaxTraverse(otherPlayer, h, count, value,isRepeat);
				
				startGreedy(newNode,i);
				if(isRepeat) {
					if(playerIndex == 0) {
						otherPlayer = 1;
					}
					if(playerIndex == 1) {
						otherPlayer = 0;
					}
				}
				
				if(newNode.repeat) {
					value = Min(value,MinValue(newNode, depth, count,i,playerIndex,playerIndex,true));
					newNode.evalFunc = value;
				}
				else if (newNode.terminal) {
					if(!newNode.recursion) {
						value = Min(value,MaxValue(newNode, depth, count,i,otherPlayer,playerIndex,true));
					}
					else {
						value = Min(value,MinValue(newNode, depth, count,i,otherPlayer,playerIndex,true));
					}
					newNode.evalFunc = value;
				}
				else {					
					value = Min(value,MaxValue(newNode, depth,count+1,i,otherPlayer,playerIndex,false));
					newNode.evalFunc = value;	
				}
			}
		}
		playerIndex = tempPlayerIndex;
		otherPlayer = tempOtherPlayer;
		if(!isEndGame(n,playerIndex) || !isEndGame(n,otherPlayer)) {
			displayMinimaxTraverse(otherPlayer, h, count, value,isRepeat);
		}
		else {
			//displayMinimaxTraverse(otherPlayer, h, count, value, isRepeat);
			value = updateBoardAfterEndGame(n,otherPlayer,playerIndex);
			displayMinimaxTraverse(otherPlayer, h, count, value, isRepeat);	
		}
		
		return value;		
	}
	
	public static boolean isEndGame(Node n,int pIndex) {

		for(int i = 0 ; i < n.cap ; i++) {
			if(n.boardState[pIndex][i] != 0){
				return false;
			}
		}
		return true;
	}
	
	public static int updateBoardAfterEndGame(Node n,int pIndex,int oIndex) {
		int val = 0;
		for(int i = 0 ; i < n.cap; i++) {
			if(n.boardState[pIndex][i] != 0){
				val += n.boardState[pIndex][i];
				n.boardState[pIndex][i] = 0;
			}
		}
		n.mancala[pIndex] += val;
		val = 0;
		for(int i = 0 ; i < n.cap; i++) {
			if(n.boardState[oIndex][i] != 0){
				val += n.boardState[oIndex][i];
				n.boardState[oIndex][i] = 0;
			}
		}
		n.mancala[oIndex] += val;
		return n.getEvalValue(evalPlayer, evalOtherPlayer);		
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////
	
		
////////////////////////////////////////////////////////////////////////////////////////////////////
	// Alpha Beta Pruning
	
	public static void AlphaBetaMinimaxDecision(Node a,int depth) {
		Node temp,maximumNode;
		int alpha,beta;
		temp = new Node(a);	
		temp.cutoffDepth = depth;

		alpha = Integer.MIN_VALUE;
		beta = Integer.MAX_VALUE;
		
		childNodes = new ArrayList();
		try {
			fw2.append("Node,Depth,Value,Alpha,Beta\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AlphaBetaMaxValue(temp,temp.cutoffDepth,1,0,evalPlayer,evalOtherPlayer,false,alpha,beta);
		maximumNode = findMax();
		fileNextState(maximumNode);
	}
	
	
	public static int AlphaBetaMaxValue(Node m,int depth,int c,int k, int playerIndex,int op, boolean isRepeat, int alpha, int beta) {
		
		int tempPlayerIndex = playerIndex;
		int tempOtherPlayer = op;
		int count,value,otherPlayer;
		otherPlayer = op;
		count  = c;
		value = Integer.MIN_VALUE;
		
		// Cut Off State
		if(count == (depth + 1)) {
			//displayMinimaxTraverse(otherPlayer, k, count, value,false);
			displayAlphaBetaMinimaxTraverse(otherPlayer, k, count, m.getEvalValue(evalPlayer,evalOtherPlayer),false,alpha,beta);
			return m.getEvalValue(evalPlayer,evalOtherPlayer);
		}
		
		if(isEmpty(m) && m.terminal && m.recursion){
			//displayMinimaxTraverse(otherPlayer, k, count, value, isRepeat);
			value = m.getEvalValue(evalPlayer,evalOtherPlayer);
			displayAlphaBetaMinimaxTraverse(otherPlayer, k, count, value, isRepeat,alpha,beta);
			return m.getEvalValue(evalPlayer,evalOtherPlayer);
		}
		
		if(isEmpty(m) && !m.terminal && !m.recursion){
			//displayMinimaxTraverse(otherPlayer, k, count, value, isRepeat);
			value = m.getEvalValue(evalPlayer,evalOtherPlayer);
			displayAlphaBetaMinimaxTraverse(otherPlayer, k, count, value, isRepeat,alpha,beta);
			return m.getEvalValue(evalPlayer,evalOtherPlayer);
		}
		
		if(isEmpty(m) && m.terminal && !m.recursion){
			//displayMinimaxTraverse(otherPlayer, k, count, value, isRepeat);
			value = m.getEvalValue(evalPlayer,evalOtherPlayer);
			displayAlphaBetaMinimaxTraverse(otherPlayer, k, count, value, isRepeat,alpha,beta);
			return m.getEvalValue(evalPlayer,evalOtherPlayer);
		}
					
		for(int j = 0 ; j < m.cap ; j++) {
			Node newNode = new Node(m);
						
			newNode.playerIndex = playerIndex;
			if(isLegal(m.boardState[playerIndex][j])){
				playerIndex = tempPlayerIndex;
				otherPlayer = tempOtherPlayer;
				if(count == 1 && !isRepeat) {
					displayAlphaBetaMinimaxTraverse(-1, j, count, value,isRepeat,alpha,beta);
				}
				else {
					displayAlphaBetaMinimaxTraverse(otherPlayer, k, count, value,isRepeat,alpha,beta);
				}
				startGreedy(newNode,j);
				if(isRepeat) {
					if(playerIndex == 0) {
						otherPlayer = 1;
					}
					if(playerIndex == 1) {
						otherPlayer = 0;
					}
				}
				if(newNode.repeat) {
					value = Max(value,AlphaBetaMaxValue(newNode, depth, count,j,playerIndex,playerIndex,true,alpha,beta));
				}
				else if (newNode.terminal) {
					if(!newNode.recursion) {			
						value = Max(value,AlphaBetaMinValue(newNode, depth, count,j,otherPlayer,playerIndex,true,alpha,beta));
					}
					else {
						value = Max(value,AlphaBetaMaxValue(newNode, depth, count,j,otherPlayer,playerIndex,true,alpha,beta));
					}
					newNode.evalFunc = value;
					if(count == 1) {
						childNodes.add(newNode);
					}
				}
				else {
					value = Max(value,AlphaBetaMinValue(newNode, depth,count + 1,j,otherPlayer,playerIndex,false,alpha,beta));
					newNode.evalFunc = value;
					if(count == 1) {
						childNodes.add(newNode);
					}
				}
				if(value >= beta) {
					displayAlphaBetaMinimaxTraverse(tempOtherPlayer, k, count, value, isRepeat,alpha,beta);
					return value;
				}
				alpha = Max(alpha, value);
			}
		}
		
		playerIndex = tempPlayerIndex;
		otherPlayer = tempOtherPlayer;
		
		if(count == 1 && !isRepeat) {	
			displayAlphaBetaMinimaxTraverse(-1, k, count, value,isRepeat,alpha,beta);
		}
				
		else if(isEndGame(m,playerIndex) || isEndGame(m,otherPlayer)) {
			//displayMinimaxTraverse(otherPlayer, k, count, value, isRepeat);
			value = updateBoardAfterEndGame(m,otherPlayer,playerIndex);
			displayAlphaBetaMinimaxTraverse(otherPlayer, k, count, value, isRepeat,alpha,beta);
		}
		else {			
			displayAlphaBetaMinimaxTraverse(otherPlayer, k, count, value,isRepeat,alpha,beta);
		}
		return value;
	}
	
	
	
	
	public static int AlphaBetaMinValue(Node n, int depth, int c, int h, int playerIndex, int op, boolean isRepeat, int alpha, int beta) {
		
		int count,value,otherPlayer;
		int tempPlayerIndex = playerIndex;
		int tempOtherPlayer = op;
		otherPlayer = op;
		count = c; 
		value = Integer.MAX_VALUE;
		
		// May Have to change player index here
		if(count == (depth + 1)) {
			//displayMinimaxTraverse(otherPlayer, h, count, value, false);
			displayAlphaBetaMinimaxTraverse(otherPlayer, h, count, n.getEvalValue(evalPlayer,evalOtherPlayer),false,alpha,beta);
			return n.getEvalValue(evalPlayer,evalOtherPlayer);
		}
		
		if(isEmpty(n) && n.terminal && n.recursion){
			//displayMinimaxTraverse(otherPlayer, h, count, value, isRepeat);
			value = n.getEvalValue(evalPlayer,evalOtherPlayer);
			displayAlphaBetaMinimaxTraverse(otherPlayer, h, count, value, isRepeat,alpha,beta);
			return n.getEvalValue(evalPlayer,evalOtherPlayer);
		}
		
		if(isEmpty(n) && !n.terminal && !n.recursion) {
			value = n.getEvalValue(evalPlayer,evalOtherPlayer);
			displayAlphaBetaMinimaxTraverse(otherPlayer, h, count, value, isRepeat,alpha,beta);
			return n.getEvalValue(evalPlayer,evalOtherPlayer);
		}
		
		if(isEmpty(n) && n.terminal && !n.recursion) {
			//displayMinimaxTraverse(otherPlayer, h, count, value, isRepeat);
			value = n.getEvalValue(evalPlayer,evalOtherPlayer);
			displayAlphaBetaMinimaxTraverse(otherPlayer, h, count, value, isRepeat,alpha,beta);
			return n.getEvalValue(evalPlayer,evalOtherPlayer);
		}
		
		//for all children
		for(int i = 0 ; i < n.cap ; i++) {
			Node newNode = new Node(n);
						
			newNode.playerIndex = playerIndex;
			if(isLegal(n.boardState[playerIndex][i])){
				playerIndex = tempPlayerIndex;
				otherPlayer = tempOtherPlayer;
				displayAlphaBetaMinimaxTraverse(otherPlayer, h, count, value,isRepeat,alpha,beta);
				
				startGreedy(newNode,i);
				if(isRepeat) {
					if(playerIndex == 0) {
						otherPlayer = 1;
					}
					if(playerIndex == 1) {
						otherPlayer = 0;
					}
				}
				
				if(newNode.repeat) {
					value = Min(value,AlphaBetaMinValue(newNode, depth, count,i,playerIndex,playerIndex,true,alpha,beta));
					newNode.evalFunc = value;
				}
				else if (newNode.terminal) {
					if(!newNode.recursion) {
						value = Min(value,AlphaBetaMaxValue(newNode, depth, count,i,otherPlayer,playerIndex,true,alpha,beta));
					}
					else {
						value = Min(value,AlphaBetaMinValue(newNode, depth, count,i,otherPlayer,playerIndex,true,alpha,beta));
					}
					newNode.evalFunc = value;
				}
				else {					
					value = Min(value,AlphaBetaMaxValue(newNode, depth,count+1,i,otherPlayer,playerIndex,false,alpha,beta));
					newNode.evalFunc = value;
					
				}
				if(value <= alpha) {
					displayAlphaBetaMinimaxTraverse(tempOtherPlayer, h, count, value, isRepeat,alpha,beta);
					return value;
				}
				beta = Min(beta, value);
			}
		}
		playerIndex = tempPlayerIndex;
		otherPlayer = tempOtherPlayer;
		if(!isEndGame(n,playerIndex) || !isEndGame(n,otherPlayer)) {
			displayAlphaBetaMinimaxTraverse(otherPlayer, h, count, value,isRepeat,alpha,beta);
		}
		else {
			//displayMinimaxTraverse(otherPlayer, h, count, value, isRepeat);
			value = updateBoardAfterEndGame(n,otherPlayer,playerIndex);
			displayAlphaBetaMinimaxTraverse(otherPlayer, h, count, value, isRepeat,alpha,beta);	
		}
		
		return value;		
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			
	
	
	public static boolean isEmpty(Node n) {
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < n.cap; j++) {
				if(n.boardState[i][j] != 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static int Min(int a, int b){
		if(a <= b) {
			return a;
		}
		else {
			return b;
		}
	}
	
	public static int Max(int a, int b) {
		if(a >= b) {
			return a;
		}
		else {
			return b;
		}
	}
	
	public static void displayMinimaxTraverse(int playerInd,int i,int c, int val, boolean isRepeat){
		if(!isGreedy) {
			lineNo++;
			String value = "",player = "",index = "";
			int count = c;
			try {
				value = val + "";
				index = (i + 2) + "";
				if(val == Integer.MAX_VALUE) {
					value = "Infinity";
				}
				if(val == Integer.MIN_VALUE) {
					value = "-Infinity";
				}
				if (playerInd == 0) {
					player = "A";
				}
				if (playerInd == 1 ) {
					player = "B";
				}
				if (playerInd == -1) {
					player = "root";
					index = "";
				}
				if (!isRepeat) {
					count = count - 1;
				}
				fw2.append(player + index + "," + count + "," + value + "\n");
				//System.out.println(lineNo + " => " + player + index + "," + count + "," + value);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
		
	// Display Minimax Alpha Beta
	public static void displayAlphaBetaMinimaxTraverse(int playerInd,int i,int c, int val, boolean isRepeat,int alpha,int beta){
		lineNoAlphaBeta++;
		String value = "",player = "",index = "",a,b;
		int count = c;
		try {
			a = alpha + "";
			b = beta + "";
			value = val + "";
			index = (i + 2) + "";
			if(val == Integer.MAX_VALUE) {
				value = "Infinity";
			}
			if(val == Integer.MIN_VALUE) {
				value = "-Infinity";
			}
			if (playerInd == 0) {
				player = "A";
			}
			if (playerInd == 1 ) {
				player = "B";
			}
			if (playerInd == -1) {
				player = "root";
				index = "";
			}
			if (!isRepeat) {
				count = count - 1;
			}
			if(alpha == Integer.MAX_VALUE) {
				a = "Infinity";
			}
			if(alpha == Integer.MIN_VALUE) {
				a = "-Infinity";
			}
			if(beta == Integer.MAX_VALUE) {
				b = "Infinity";
			}
			if(beta == Integer.MIN_VALUE) {
				b = "-Infinity";
			}
			fw2.append(player + index + "," + count + "," + value + "," + a + "," + b + "\n");
			//System.out.print(lineNoAlphaBeta + "=>" + player + index + "," + count + "," + value + "," + a + "," + b + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class Node {
	boolean repeat,terminal,recursion;
	int playerNo,otherPlayerNo,playerIndex,otherPlayer,cutoffDepth,currentDepth,cap,evalFunc,alpha,beta;
	int[][] boardState;
	int[] mancala;
	String playerName,opponentName;
	
	public Node(){
		mancala = new int[2];
	}
	
	public void setBoardLength(int cap) {
		this.cap = cap;
		boardState = new int[2][cap];
	}
	
	public Node(Node a){
		this.playerNo = a.playerNo;
		this.otherPlayerNo = a.otherPlayerNo;
		this.playerIndex = a.playerIndex;
		this.otherPlayer = a.otherPlayer;
		this.cutoffDepth = a.cutoffDepth;
		this.cap = a.cap;
		this.evalFunc = a.evalFunc;
		this.alpha = a.alpha;
		this.beta = a.beta;
		this.playerName = a.playerName;
		this.opponentName = a.opponentName;
		this.boardState = (int[][]) a.boardState.clone();
		this.boardState[0] = (int[])a.boardState[0].clone();
		this.boardState[1] = (int[])a.boardState[1].clone();
		this.mancala = (int[]) a.mancala.clone();
		this.terminal = terminal;
		this.recursion = recursion;
	}
		
	public Node shiftForPlayer1(int loc,int stones) {
		repeat = false;
		int round = 0;
		boardState[playerIndex][loc] = 0;
		while(stones > 0) {
			if(round == 0) {
				loc = loc + 1;
			}
			for(int i = loc ; i < cap ; i++) {
				if (stones > 0){
					// If last stone is in empty pit 
					if((boardState[playerIndex][i] == 0) && (stones == 1)) {
						mancala[playerIndex] += 1;
						mancala[playerIndex] += boardState[otherPlayer][i];
						boardState[otherPlayer][i] = 0;
						stones--;
						repeat = false;
						return this;
					}
					boardState[playerIndex][i] += 1;
					stones--;
				}
				if (stones == 0) {
					return this;
				}
			}
			if(playerIndex == 1) {
				if (stones > 0){
					mancala[playerIndex] += 1;
					stones--;
					//Means last stone here
					if(stones == 0) {
						repeat = true;
					}
				}
				if (stones == 0) {
					return this;
				}
			}
			for(int j = cap-1 ; j >= 0 ; j--) {
				if (stones > 0){	
					boardState[otherPlayer][j] += 1;
					stones--;
				}
				if (stones == 0) {
					return this;
				}
			}
			loc = 0;
			round++;
		}
		return null;
	}
	
	public Node shiftForPlayer2(int loc,int stones) {
		int round = 0;
		boardState[playerIndex][loc] = 0;
		while(stones > 0) {
			
			// Only for the first round
			if(round == 0) {
				loc = loc - 1;
			}
			for(int j = loc ; j >= 0 ; j--) {
				if (stones > 0){
					// If last stone is in empty pit 
					if((boardState[playerIndex][j] == 0) && (stones == 1)) {
						mancala[playerIndex] += 1;
						mancala[playerIndex] += boardState[otherPlayer][j];
						boardState[otherPlayer][j] = 0;
						stones--;
						repeat = false;
						return this;
					}
					boardState[playerIndex][j] += 1;
					stones--;
				}
				if (stones == 0) {
					return this;
				}
			}
			
			if(playerIndex == 0) {
				if (stones > 0){	
					mancala[playerIndex] += 1;
					stones--;
					//Means last stone here
					if(stones == 0) {
						repeat = true;
					}
				}
				if (stones == 0) {
					return this;
				}
			}
			for(int i = 0 ; i < cap ; i++) {
				if (stones > 0){
					boardState[otherPlayer][i] += 1;
					stones--;
				}
				if (stones == 0) {
					return this;
				}
			}
			loc = cap - 1;
			round++;
		}
		return null;
	}
	
	public int getEvalValue(int a, int b) {
		int value = mancala[a] - mancala[b];
		return value;
	}
}
