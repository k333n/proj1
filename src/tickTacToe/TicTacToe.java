package tickTacToe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TicTacToe {
	int[/* X */][/* Y */] map;
	public final String name;
	public gameController<Integer, TicTacToe> gc;

	
	TicTacToe(String name)
	{
		map = new int[3][3];
		this.name = name;
		gc = new gameController<>(this);
		main.p.println("TicTacToe " + name + " created!");
		startGame();
	}
	
	
	public void startGame()  
	{
		
		main.p.println(name + " started!");
		main.p.println(this);
		
		ExecutorService executorService = Executors.newFixedThreadPool(1); 
		Future <Integer> f = (Future<Integer>) executorService.submit(gc);
		gameController.gameCondition gameCondition = gc.new gameCondition(f,gc);
		
		Scanner input = new Scanner(System.in);
	
		Random r = new Random();

		for(int loop=1; loop !=0; loop++)
			try 
			{
				if(loop ==1) new Thread(gameCondition).start();
				if(gameCondition.update().length() > 1) 
				{
					main.p.println(gameCondition.update());
					break;
				}
				 
			
					map[r.nextInt(3)][r.nextInt(3)] = 1;
					map[r.nextInt(3)][r.nextInt(3)] = -1;


					main.p.println(this);
					Thread.sleep(1000);
				
			}
			catch (Exception i) 
			{
				System.out.println(i);
				System.exit(0);
			}
		
		executorService.shutdown();
	}
	
	
	
	
	
	public String toString() 
	{
		StringBuilder mapReturn = new StringBuilder();
		for(int x=0 ; x < map.length; x++)
		{
			for(int y =0; y < map[x].length; y++) 
			{
				int cur = map[x][y];
				switch (cur) 
				{
					case -1 : mapReturn.append("[o]");
					break;
					case 1  : mapReturn.append("[x]");
					break;
					default : mapReturn.append("[ ]" );
					break;
				}
			}
			mapReturn.append("\n");
		}
		return mapReturn.toString();
	}
	
	
	private boolean isTaken(position p) 
	{
		return ( !(map[p.posX][p.posY] == 0)) ? true : false;
	}

	
	
	/*main.p.println( isTaken( new position().setPos(1, 1) ) ); */
	class position
	{
		int posX,posY;
		position setPos(int x, int y) 
		{
			posX =x;
			posY =y;
			return this;
		}
		
	}
	

	
	
	
	class gameController <f, T > implements Callable {
		T game;
		gameController(T game){
			this.game = game;

		}
		@Override
		public f call() throws Exception {
						
			main.p.println( "Game Controller for " + ((TicTacToe)game).name + " initialised");
			for (int i =0 ; i != -1 ; i++) {
				try
				{
					Thread.sleep(1000);
					if (isWin(1)) throw new  InterruptedException ("1");
					if (isWin(-1)) throw new  InterruptedException ("-1");

				}catch (InterruptedException err )
				{
					
					main.p.println(" Game Controller " +((TicTacToe)game).name + " interruped");
				
					return (f) ((Integer)Integer.valueOf(err.getMessage()));
				}
			}
			
			return (f) ((Integer)0);

		}
		public boolean isWin(int tocheck) 
		{
			int[][] m = TicTacToe.this.map;
			return (tocheck == 1) ?  
					(m[0][0] == 1 && m[0][1] == 1 && m[0][2] ==1) ||
					(m[1][0] == 1 && m[1][1] == 1 && m[1][2] ==1) ||
					(m[2][0] == 1 && m[2][1] == 1 && m[2][2] ==1) 
					: 
					(m[0][0] == -1 && m[0][1] == -1 && m[0][2] == -1) ||
					(m[1][0] == -1 && m[1][1] == -1 && m[1][2] == -1) ||
					(m[2][0] == -1 && m[2][1] == -1 && m[2][2] == -1) ;
		}
		
		private class gameCondition extends Thread{
			private Future<f> gameService;
			private gameController gc;
			private String condition;
			gameCondition(Future<f> gameService, gameController gc ){
				this.gameService = gameService;
				this.gc = gc;
				condition ="";
				
				this.setName("gameCondition Thread for "  + ((TicTacToe)gc.game).name);
				
			}
			@Override
			public void run() {
				main.p.println("GameConditioner for " + ((TicTacToe)gc.game).name +  " has initialised!");
				try {
					 condition = getWinnner(gameService);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			public String update() {
				return condition;
			}
			
			 String getWinnner(Future<?> f) throws Exception {
				 
				 main.p.println(f.get());
				return ((Integer)f.get() == 1) ? "Player X has won!":"Player O has won!";
			}
		}
	}
	
	

	
	
	

	
	
	
	

}
