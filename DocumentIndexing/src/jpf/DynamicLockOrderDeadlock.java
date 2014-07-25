package jpf;

import gov.nasa.jpf.vm.Verify;

import java.util.Random;

public class DynamicLockOrderDeadlock {
	static class Account {
		private int balance;
		public Account(int amount){
			balance=amount;
		}
		public int getBalance(){
			return balance;
		}
		public void debit(int amount){
			balance-=amount;
		}
		public void credit(int amount){
			balance+=amount;
		}
	}
	
	@SuppressWarnings("serial")
	static class InsufficientBalanceException extends Exception {
	}

	private static final int NUM_THREADS = 5;
	private static final int NUM_ACCOUNTS = 5;
	private static final int NUM_ITERATIONS = 10;
	private static final Random gen = new Random();
	private static final Account[] accounts = new Account[NUM_ACCOUNTS];
	
	static void transferMoney(Account from,	Account to, int amount) throws InsufficientBalanceException {
		synchronized (from) {
			synchronized (to) {
				Verify.beginAtomic();							// the matter here is deadlock while acquiring resources, not a 
				if (from.getBalance() < amount)					// "lost-update" race condition, hence atomicity does not change
					throw new InsufficientBalanceException();	// application's semantics regarding the deadlock liveness property
				from.debit(amount);
				to.credit(amount);
				Verify.endAtomic();
			}
		}
	}

	static class TransferThread extends Thread {
		public void run() {
			for (int i=0; i<NUM_ITERATIONS; i++){
				Verify.beginAtomic();
				int fromAcc = gen.nextInt(NUM_ACCOUNTS);
				int toAcc = 0;
				do {
					toAcc = gen.nextInt(NUM_ACCOUNTS);
				} while (toAcc == fromAcc);
				int amount = gen.nextInt(10);
				Verify.endAtomic();
				try {
//					log("Transferring from "+fromAcc+" to "+toAcc+" amount "+amount+"...");
					transferMoney(accounts[fromAcc],accounts[toAcc],amount);
//					log("done.");
				} catch (InsufficientBalanceException ex){
//					log("Not enough money.");
				}
			}
		}
		private void log(String msg){
			synchronized(System.out){
				System.out.println("["+this+"] "+msg);
			}
		}
	}
	
	public static void main(String[] args) {
		Verify.beginAtomic();
		for (int i=0; i<accounts.length; i++){
			accounts[i] = new Account(100);
		}
		for (int i=0; i<NUM_THREADS; i++){
			new TransferThread().start();
		}
		Verify.endAtomic();
	}
}
