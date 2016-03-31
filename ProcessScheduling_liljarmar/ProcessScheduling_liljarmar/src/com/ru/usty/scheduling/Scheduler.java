package com.ru.usty.scheduling;

import java.util.LinkedList;
import java.util.Queue;
import java.util.*;

import com.ru.usty.scheduling.process.ProcessExecution;

public class Scheduler {

	ProcessExecution processExecution;
	Policy policy;
	int quantum;
	Queue <Integer> pqueue;
	boolean ifRunning;
	boolean RR;
	boolean SPN;
	Timer RoundRTimer = new Timer();
	/**
	 * Add any objects and variables here (if needed)
	 */
	

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public Scheduler(ProcessExecution processExecution) {
		this.processExecution = processExecution;

		/**
		 * Add general initialization code here (if needed)
		 */
		
	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void startScheduling(Policy policy, int quantum) {
		this.policy = policy;
		this.quantum = quantum;
		pqueue = new LinkedList<Integer>();
		ifRunning = false;
		RR = false;
		SPN = false;
		
		/**
		 * Add general initialization code here (if needed)
		 */

		switch(policy) {
		case FCFS:	//First-come-first-served
			System.out.println("Starting new scheduling task: First-come-first-served");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case RR:	//Round robin
			System.out.println("Starting new scheduling task: Round robin, quantum = " + quantum);
			RR = true;
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SPN:	//Shortest process next
			System.out.println("Starting new scheduling task: Shortest process next");
			SPN = true;
			RoundRTimer.cancel();
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SRT:	//Shortest remaining time
			System.out.println("Starting new scheduling task: Shortest remaining time");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case HRRN:	//Highest response ratio next
			System.out.println("Starting new scheduling task: Highest response ratio next");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case FB:	//Feedback
			System.out.println("Starting new scheduling task: Feedback, quantum = " + quantum);
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		}

		/**
		 * Add general scheduling or initialization code here (if needed)
		 */

	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void processAdded(int processID) {

		/**
		 * Add scheduling code here
		 */
		
		if(ifRunning == false)
		{
			if(RR == true){
				//System.out.println("RR");
				pqueue.add(processID);
				RRtimer(quantum);
			}
			else if(SPN == true){
				//System.out.println("SPN");
					if(pqueue.isEmpty()){
						//System.out.println("SPN empty");
						processExecution.switchToProcess(processID);
					}
			}
			else{
				//System.out.println("FCFS");
				if(pqueue.isEmpty()){
					//System.out.println("FCFS empty");
					processExecution.switchToProcess(processID);
					pqueue.add(processID);
				}
				else{
					//System.out.println("FCFS not empty");
					pqueue.add(processID);
				}
			}		
			ifRunning = true;
		}
		else{
			pqueue.add(processID);
		}
	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void processFinished(int processID) {

		/**
		 * Add scheduling code here
		 */
		if(!pqueue.isEmpty()){
			if(RR == true){
				pqueue.remove(processID);
                RoundRTimer.cancel();
                RoundRTimer = new Timer();
                RRtimer(quantum);
			}
			else if(SPN == true){
                // Find the smallest processor
				int sp = pqueue.peek();
				Iterator<Integer> iter = pqueue.iterator();
            	// Check if the queue has any processes left
            	while (iter.hasNext()){
           		// Get the next process and check if it's smaller than any current waiting processes
          			int nextProcess = iter.next();
           			if((processExecution.getProcessInfo(sp).totalServiceTime) > (processExecution.getProcessInfo(nextProcess).totalServiceTime)){
           				sp = nextProcess;
           				System.out.println("smallest found");
           			}
           		}
                pqueue.remove(sp);
                processExecution.switchToProcess(sp);
                System.out.println("switching to smallest");
            }
			else{
				pqueue.remove();
				ifRunning = false;
				if(!pqueue.isEmpty()) processExecution.switchToProcess(pqueue.peek());
			}
		}
		else{
			ifRunning = false;
		}
		System.out.println("Process finished");
		
	}
	
	public void RRtimer(int quantum){
		RoundRTimer.scheduleAtFixedRate(
			new TimerTask(){
				@Override
				public void run(){
					if(!pqueue.isEmpty()){
						int temp = pqueue.remove();
						processExecution.switchToProcess(temp);
						pqueue.add(temp);
					}
				}
			},0, quantum);
	}
}

