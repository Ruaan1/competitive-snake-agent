import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.io.InputStreamReader;
import java.util.Random;
import za.ac.wits.snake.DevelopmentAgent;

public class MyAgent extends DevelopmentAgent {
	boolean visited[][];
	int move = 0;
    int X_Snake = 0;
    int Y_Snake = 0;
    int Neck_X = 0;
    int Neck_Y = 0;
    int direction;
    int boardWidth = 50;
    int boardHeight = 50;
    public static void main(String args[]) {
        MyAgent agent = new MyAgent();
        MyAgent.start(agent, args);
    }
    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String initString = br.readLine();
            String[] temp = initString.split(" ");
            int nSnakes = Integer.parseInt(temp[0]);
            int numObstacles=3;
            ArrayList<String> obstacles = new ArrayList<>();
            while (true) {
                String line = br.readLine();
                if (line.contains("Game Over")) {
                    break;
                }

                String apple1 = line;
                String [] Apple_Coordinates = apple1.split(" ");
                int X_apple;
                int Y_apple;
                X_apple = Integer.parseInt(Apple_Coordinates[0]);
                Y_apple = Integer.parseInt(Apple_Coordinates[1]);
                //do stuff with apples
                for (int j=0; j<numObstacles; j++) {
                	String obsLine = br.readLine();
                	obstacles.add(obsLine);
                }
                int mySnakeNum = Integer.parseInt(br.readLine());
                String [] Enemies = new String[4];
                for(int i=0; i<nSnakes; i++) {
                	String snakeLine = br.readLine();
                	Enemies[i]=snakeLine;
                	String[] Snake = snakeLine.split(" ");
                	if(i == mySnakeNum) {
                		if(Snake.length>=5) {
                				String Head_s = Snake[3];
                				String Neck_s = Snake[4];
                				X_Snake = Integer.parseInt(Head_s.split(",")[0]);
                				Y_Snake = Integer.parseInt(Head_s.split(",")[1]);
                				Neck_X = Integer.parseInt(Neck_s.split(",")[0]);
                				Neck_Y = Integer.parseInt(Neck_s.split(",")[1]);
                				if (X_Snake == Neck_X) {     
                                    if (Y_Snake < Neck_Y) {
                                        direction = 0; 
                                    } else {
                                        direction = 1; 
                                    }
                                } else if (Y_Snake == Neck_Y) {
                                    if (X_Snake < Neck_X) {
                                        direction = 2; 
                                    } else {
                                        direction = 3; 
                                    }
                                }
                			}
                		}
                	}
                move = calculateMoveBFS(X_apple, Y_apple,X_Snake,Y_Snake,br,nSnakes,mySnakeNum,obstacles,direction,Enemies);
        		//finished reading, calculate move:
        		System.out.println(move);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int calculateMoveBFS(int X_apple, int Y_apple,int X_Snake,int Y_Snake,BufferedReader br, int nSnakes,int mySnakeNum,ArrayList<String> obstacles, int direction, String[] enemies)throws IOException {
        
    	
        Queue<int[]> queue = new LinkedList<>();
        int [] temp = {X_Snake, Y_Snake,direction};
        int [][] DJ = new int [boardHeight][boardWidth];
        DJ[Y_Snake][X_Snake]=0;
        queue.add(temp);

        
        visited = new boolean[boardHeight][boardWidth];
        for(String enemy:enemies) {
        	String[] Snake = enemy.split(" ");
        	if(Snake[0].equals("dead")) {
        		continue;
        	}
        	for(int i=3;i<Snake.length-1;i++) {
        		String [] Enemy1 = Snake[i].split(",");
        		String [] Enemy2 = Snake[i+1].split(",");
        		int x1 = Integer.parseInt(Enemy1[0]);
        		int y1 = Integer.parseInt(Enemy1[1]);
        		int x2 = Integer.parseInt(Enemy2[0]);
        		int y2 = Integer.parseInt(Enemy2[1]);
        		if(y1==y2) {
        			if(x1>x2) {
        				for(int j=x2;j<=x1;j++) {
        					visited[y1][j]=true;
        				}
        			}else {
        				for(int j=x1;j<=x2;j++) {
        					visited[y1][j]=true;
        				}
        			}
        		}else {
        			if(y1>y2) {
        				for(int j=y2;j<=y1;j++) {
        					visited[j][x1]=true;
        				}
        			}else {
        				for(int j=y1;j<=y2;j++) {
        					visited[j][x1]=true;
        				}
        			}
        			
        		}
        		
        	}
        }
        for (String obstacle : obstacles) {
            String[] coordinates = obstacle.split(" ");
            for (String coord : coordinates) {
                String[] xy = coord.split(",");
                int obs_X = Integer.parseInt(xy[0]);
                int obs_Y = Integer.parseInt(xy[1]);
                visited[obs_Y][obs_X]=true;
            }
        }
        visited[Y_Snake][X_Snake] = true;

        
        int[][] moves = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        
        while (!queue.isEmpty()) { 
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];
           
            if(current.length<4) {
            	int initialDirection = current[2]; 
            	for (int i = -1; i <= 2; i++) {
                    int[] move = moves[(initialDirection + i + 4) % 4]; 
                    int newX = x + move[0]; 
                    int newY = y + move[1]; 
                    if (isValidMove(newX, newY, obstacles) && !visited[newY][newX]) {
                        queue.add(new int[]{newX, newY,initialDirection});
                        visited[newY][newX] = true;
                        DJ[newY][newX] = DJ[y][x]+1;
                    }
                }
            }
            if (x == X_apple && y == Y_apple) { 
                
            	int distance = DJ[Y_apple][X_apple];
                while (distance > 1) {
                	
                	if(x+1<50 && (DJ[y][x+1]==distance - 1)) {
                		x = x+1;
                	}
                	else if(x-1>=0 && (DJ[y][x-1]==distance - 1)) {
                		x = x-1;
                	}
                	else if(y+1<50 && (DJ[y+1][x]==distance - 1)) {
                		y = y+1;
                	}else {
                		y=y-1;
                	}
                	distance--;
                }
                
                if (x < X_Snake) {
                    return 2; 
                } else if (x > X_Snake) {
                    return 3; 
                } else if (y < Y_Snake) {
                    return 0; 
                } else if(y > Y_Snake){
                    return 1; 
                }
            }
        }
        if(Y_Snake-1>=0 && DJ[Y_Snake-1][X_Snake]>=1){
        	return 0;
        }else if(Y_Snake+1<50 && DJ[Y_Snake+1][X_Snake]>=1) {
        	return 1;
        }else if(X_Snake-1>=0 && DJ[Y_Snake][X_Snake-1]>=1) {
        	return 2;
        }else {
        	return 3;
        }
        
    }
    public boolean isValidMove(int x, int y, ArrayList<String> obstacles) {
        return x >= 0 && x < boardWidth && y >= 0 && y < boardHeight;
    }
}
