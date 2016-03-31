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
	boolean RR , FR;
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
		FR = false;
		/**
		 * Add general initialization code here (if needed)
		 */

		switch(policy) {
		case FCFS:	//First-come-first-served
			System.out.println("Starting new scheduling task: First-come-first-served");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
				FR = true;
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
	
		if(pqueue.isEmpty())
		{
			processExecution.switchToProcess(processID);
			pqueue.add(processID);
			if(RR) RRtimer(quantum);
		}
		else{
			pqueue.add(processID);
			if(RR) RRtimer(quantum);
		}
		
	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void processFinished(int processID) {

		/**
		 * Add scheduling code here
		 */
		
		if(FR){
			pqueue.remove();
			FCFRRunning();
		}
		//pqueue.remove();
		if(RR){
			RRRunning(processID);
		}
		System.out.println("BUID");
		
	}
	public void RRRunning(int processID){
			
				pqueue.remove(processID);
                RoundRTimer.cancel();
                RoundRTimer = new Timer();
                RRtimer(quantum);
			
			
	}
	public void FCFRRunning(){
		
		if(!pqueue.isEmpty())
		{
			processExecution.switchToProcess(pqueue.peek());
		}
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

