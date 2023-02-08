package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread{

	
	int a,b;
	boolean estado;
    private boolean running;
	private List<Integer> primes;
	
	public PrimeFinderThread(int a, int b) {
		super();
		this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;
        this.running = true;
	}

        @Override
	public void run(){
            for (int i= a;i < b;i++){						
                if (isPrime(i)){
                    primes.add(i);
                }
                pause();
            }
            this.running=false;
	}
	
	boolean isPrime(int n) {
	    boolean ans;
            if (n > 2) { 
                ans = n%2 != 0;
                for(int i = 3;ans && i*i <= n; i+=2 ) {
                    ans = n % i != 0;
                }
            } else {
                ans = n == 2;
            }
	    return ans;
	}

	public List<Integer> getPrimes() {
		return primes;
	}

    public synchronized void hold(){
        estado = true;
    }

    public synchronized void restart(){
        estado = false;
        notifyAll();
    }

    public synchronized void pause(){
        while(estado){
            try{
                wait();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public boolean isRunning() {
        return this.running;
    }
}
