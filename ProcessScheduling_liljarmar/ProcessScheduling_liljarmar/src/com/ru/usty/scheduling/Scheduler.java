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
	boolean RR, FR, SPN, SPNN, SRT;
	Timer RoundRTimer = new Timer();
	int running;
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
		SPN = false;
		SPNN = false;
		SRT = false;
		running = 0;
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
			SPN = true;
			RoundRTimer.cancel();
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SRT:	//Shortest remaining time
			System.out.println("Starting new scheduling task: Shortest remaining time");
			SRT = true;
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
		if(FR || RR ){
			if(pqueue.isEmpty()){
				processExecution.switchToProcess(processID);
				pqueue.add(processID);
			}
			else{
				pqueue.add(processID);
			}
		}
		else if(pqueue.isEmpty() && !SPNN)
		{
			processExecution.switchToProcess(processID);
			System.out.println("test processID " + processID);
			running = processID;
			//pqueue.add(processID);
			if(SPN || SRT) SPNN = true;
			//if(RR) RRtimer(quantum);	
		}
		else{
			System.out.println("test processID " + processID);
			if(SRT){
				long nowTime = (processExecution.getProcessInfo(running).totalServiceTime - processExecution.getProcessInfo(running).elapsedExecutionTime);
				if(nowTime > processExecution.getProcessInfo(processID).totalServiceTime)
				{
					processExecution.switchToProcess(processID);
					pqueue.add(running);
					running = processID;
				}
				else pqueue.add(processID);
			
			//if(RR) RRtimer(quantum);
		}
			else pqueue.add(processID);}
		
	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void processFinished(int processID) {
		System.out.println("test");
		/**
		 * Add scheduling code here
		 */
		
		if(FR){
			pqueue.remove();
			FCFRRunning();
		}
		//pqueue.remove();
		else if(RR){
			RRRunning(processID);
		}
		else if(SPN){
			SPNRunning(processID);
		}
		else if(SRT){
			SRTRunning(processID);
		}
		System.out.println("BUID");
		
	}
	public void SPNRunning(int processID){
		if(!pqueue.isEmpty()){
			int sp = pqueue.peek();
			System.out.println("test1");
			Iterator<Integer> iter = pqueue.iterator();
			System.out.println("test2");
	    	// Check if the queue has any processes left
	    	while (iter.hasNext()){
	    		System.out.println("test3");
	   		// Get the next process and check if it's smaller than any current waiting processes
	    		int nextProcess = iter.next();

	   			if((processExecution.getProcessInfo(sp).totalServiceTime) > (processExecution.getProcessInfo(nextProcess).totalServiceTime))
	   			{
	   				System.out.println("test5");
	   				sp = nextProcess;
	   				System.out.println("smallest found");
	   			}
	   		}
	        pqueue.remove(sp);
	        System.out.println("test6");
	        processExecution.switchToProcess(sp);
	        System.out.println("switching to smallest");
		}
		else SPNN = false;
	}
	
	public void SRTRunning(int processID){
		if(!pqueue.isEmpty()){
			int sp = pqueue.peek();
			System.out.println("test1");
			Iterator<Integer> iter = pqueue.iterator();
			System.out.println("test2");
	    	// Check if the queue has any processes left
	    	while (iter.hasNext()){
	    		System.out.println("test3");
	   		// Get the next process and check if it's smaller than any current waiting processes
	    		int nextProcess = iter.next();
   				System.out.println("smallest found " + processExecution.getProcessInfo(nextProcess).totalServiceTime);

	   			if((processExecution.getProcessInfo(sp).totalServiceTime) > (processExecution.getProcessInfo(nextProcess).totalServiceTime))
	   			{
	   				System.out.println("test5");
	   				sp = nextProcess;
	   				System.out.println("smallest found");
	   			}
	   		}
	        pqueue.remove(sp);
	        System.out.println("test6");
	        processExecution.switchToProcess(sp);
	        running = sp;
	        System.out.println("switching to smallest");
		}
		else SPNN = false;
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

