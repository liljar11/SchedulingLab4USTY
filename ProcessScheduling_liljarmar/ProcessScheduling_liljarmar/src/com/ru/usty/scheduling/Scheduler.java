package com.ru.usty.scheduling;

import java.util.LinkedList;
import java.util.Queue;
import java.util.*;

import com.ru.usty.scheduling.process.ProcessExecution;

public class Scheduler {

	ProcessExecution processExecution;
	Policy policy;
	int quantum;
	Queue <help> pqueue;
	boolean ifRunning;
	boolean RR, FR, SPN, SPNN, SRT, HRRN;
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
		pqueue = new LinkedList<help>();
		ifRunning = false;
		RR = false;
		FR = false;
		SPN = false;
		SPNN = false;
		SRT = false;
		HRRN = false;
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
			HRRN = true;
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
				
				pqueue.add(new help(processID, 0));
			}
			else{
				pqueue.add(new help(processID, 0));
			}
		}
		else if(pqueue.isEmpty() && !SPNN)
		{
			processExecution.switchToProcess(processID);
			System.out.println("test processID " + processID);
			running = processID;
			if(SPN || SRT || HRRN) SPNN = true;
		}
		else{
			System.out.println("test processID " + processID);
			if(HRRN){
				System.out.println("tolur " + processExecution.getProcessInfo(processID).totalServiceTime+ " " + processExecution.getProcessInfo(processID).elapsedWaitingTime+ " " + processExecution.getProcessInfo(processID).totalServiceTime );

				pqueue.add(new help(processID, 0));
			}			
			else if(SRT){
				long nowTime = (processExecution.getProcessInfo(running).totalServiceTime - processExecution.getProcessInfo(running).elapsedExecutionTime);
				if(nowTime > processExecution.getProcessInfo(processID).totalServiceTime)
				{
					processExecution.switchToProcess(processID);
					pqueue.remove(running);
					pqueue.add(new help(running, (nowTime)));
					running = processID;
				}
				else pqueue.add(new help(processID, processExecution.getProcessInfo(processID).totalServiceTime));
			}
			else pqueue.add(new help(processID, processExecution.getProcessInfo(processID).totalServiceTime));
		}
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
		else if(RR){
			if(!pqueue.isEmpty()){
				RRRunning(processID);
			}	
		}
		else if(SPN){
			SRTRunning(processID);
		}
		else if(SRT){
			SRTRunning(processID);
		}
		else if(HRRN){
			HRRNRunning(processID);
		}
		System.out.println("BUID");
		
	}
	
	public void SRTRunning(int processID){
		if(!pqueue.isEmpty()){
			help first = pqueue.peek();
			Iterator<help> iter = pqueue.iterator();
	    	while (iter.hasNext()){
	    		help nextProcess = iter.next();

	   			if((first.Time) > (nextProcess.Time))
	   			{
	   				first = nextProcess;
	   				System.out.println("smallest found");
	   			}
	   		}
	        pqueue.remove(first);
	        processExecution.switchToProcess(first.ID);
	        running = first.ID;
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
			processExecution.switchToProcess(pqueue.peek().ID);
		}
	}
	
	public void RRtimer(int quantum){
		RoundRTimer.scheduleAtFixedRate(
			new TimerTask(){
				@Override
				public void run(){
					if(!pqueue.isEmpty()){
						int temp = pqueue.remove().ID;
						processExecution.switchToProcess(temp);
						pqueue.add(new help(temp, 0));
					}
				}
			},0, quantum);
	}
	
	public void HRRNRunning(int processID){
		if(!pqueue.isEmpty()){
			help first = pqueue.peek();
			Iterator<help> iter = pqueue.iterator();
	    	while (iter.hasNext()){
	    		help nextProcess = iter.next();

	    		if((processExecution.getProcessInfo(first.ID).totalServiceTime + processExecution.getProcessInfo(first.ID).elapsedWaitingTime / processExecution.getProcessInfo(first.ID).totalServiceTime) < (processExecution.getProcessInfo(nextProcess.ID).totalServiceTime + processExecution.getProcessInfo(nextProcess.ID).elapsedWaitingTime / processExecution.getProcessInfo(nextProcess.ID).totalServiceTime))
	   			{
	   				first = nextProcess;
	   				System.out.println("smallest found");
	   			}
	   		}
	        pqueue.remove(first);
	        processExecution.switchToProcess(first.ID);
	        running = first.ID;
	        System.out.println("switching to smallest");
		}
		else SPNN = false;
	}
}

