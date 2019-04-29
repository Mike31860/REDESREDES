package io.connection;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import io.model.Cell;

/**
 * Server
 */
public class Server {

	public static final int WIDTH = 455;
	public static final int HEIGHT = 455;

	private ServerSocket serverSocket;

	private Hashtable<String, Integer> users;
	private Hashtable<Integer, String> indexUsers;
	private Hashtable<Integer, Cell> cells;
	private int currentId;
	private Hashtable<String, Connection> connections;
	private Hashtable<String, ConectionUDPSer> connectionsUDP;
	private int port;
	private Random random;

	/**
	 * With default port 8888
	 */
	public Server() {
		this(8888);
	}

	public Server(int port) {
		this.port = port;
		this.cells = new Hashtable<Integer, Cell>();
		this.indexUsers = new Hashtable<Integer, String>();
		this.users = new Hashtable<String, Integer>();
		connections = new Hashtable<String, Connection>();
		connectionsUDP = new Hashtable<String, ConectionUDPSer>();
		random = new Random();

		initCells();
	}

	private void initCells() {
		for (int i = 0; i < 20; i++) {

			// if x = 0 or y=0 then the circle would be in 0,y or x,0 so,
			// circle shape doesn't draw correctly.
			// the same with x=width or y =height, therefor
			// x and y were added with 10 and width and heigth
			// were substract by 10

			Cell cell = createRandomCell(10, 10);

			cells.put(currentId++, cell);
		}

	}

	private Cell createRandomCell(int value, int radius) {
		int x = random.nextInt(WIDTH - 10) + 10;
		int y = random.nextInt(HEIGHT - 10) + 10;
		int r = random.nextInt(255);
		int g = random.nextInt(255);
		int b = random.nextInt(255);

		Cell cell = new Cell(x, y, r, g, b, value, radius);
		return cell;
	}

	/**
	 * init server for accept connections request
	 */
	public void init() {
		try {
			serverSocket = new ServerSocket(port);
			 DatagramSocket socketUDP = new DatagramSocket(5000);
			 socketUDP.setBroadcast(true);
			 byte[] buffer = new byte[1024];
			 
			 ConectionUDPSer conectar = new ConectionUDPSer(this, socketUDP, buffer);
			 conectar.start();
			while (true) {
				Socket socket = serverSocket.accept();
				
				Connection conn = new Connection(this, socket);
				
				conn.start();
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void process(String data, Connection conn) {
		// TODO Auto-generated method stub

		
		String header = "";
		if (data.contains("join")) {
			String nick = data.split(":")[1].replace("\n", "");
			header = "welcome:" + nick;
			conn.setNickName(nick);
			addUserCell(conn, nick);
			sendInfoCells(header, conn);
			

		}

		else if (data.contains("move")) {
			// move:nick=direction (up,down,left,right)
			String[] moveInfo = data.split(":")[1].split("=");
			String nick = moveInfo[0];
			String direction = moveInfo[1].replace("\n", "");

			int index = users.get(nick);
			Cell cell = cells.get(index);

			if (direction.toLowerCase().equals("left")) {
				cell.move('l');
			} else if (direction.toLowerCase().equals("right")) {
				cell.move('r');
			} else if (direction.toLowerCase().equals("up")) {
				cell.move('u');
			} else if (direction.toLowerCase().equals("down")) {
				cell.move('d');
			} else {
				header = "invalid direction";
			}

			eat(cell);
			sendInfoCells(header, conn);

		}

	}
	
	public String processUDP(String data, ConectionUDPSer conn) {
		// TODO Auto-generated method stub

		String respuesta="";
		String header = "";
		if (data.contains("join")) {
		//	String nick = data.split(":")[1].replace("@", "");
			String nick = data.split(":")[1];
			header = "welcome:" + nick;
			connectionsUDP.put(nick, conn);
			
		   respuesta=sendInfoCellsUDP(header, conn);
			System.out.println(respuesta);

		}

	 return respuesta;

	}

	
	
	
	
	

	private void eat(Cell cell) {

		Hashtable<Integer, Cell> toPut = new Hashtable<Integer, Cell>();
		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		for (Integer id : cells.keySet()) {

			Cell food = cells.get(id);

			if (food != cell && food.eatable(cell)) {
				toRemove.add(id);
				sendLost(id);
				cell.setValue(cell.getValue()+food.getValue());
				cell.setRadius(cell.getRadius()+0.5);
				Cell randomCell = createRandomCell(10, 10);
				toPut.put(currentId++, randomCell);
			}

			

		}
		
		for(Integer id: toRemove) {
			cells.remove(id);
		}

		for (Integer id : toPut.keySet()) {
			cells.put(id, toPut.get(id));
		}

	}

	private void sendLost(Integer cellId) {
		String user = this.indexUsers.get(cellId);
		if (user != null) {
			this.indexUsers.remove(cellId);
			this.users.remove(user);
			this.connections.get(user).write("lost:have been eaten by a cell");
			this.connections.remove(user);
		}

	}

	private void addUserCell(Connection conn, String nick) {

		Cell cell = createRandomCell(20, 30);

		cells.put(currentId, cell);
		int index = currentId;
		this.indexUsers.put(index, nick);
		this.users.put(nick, index);
		connections.put(nick, conn);
		currentId += 1;

		System.out.println("Joined : " + nick + " with cell in :" + cell.getX() + "," + cell.getY());

	}
	
	public String sendInfoCellsUDP(String header, ConectionUDPSer co) {
		// TODO Auto-generated method stub
		String info = "";
		for (ConectionUDPSer conn : connectionsUDP.values()) {

			

			for (Integer id : cells.keySet()) {

				Cell cell = cells.get(id);

				info += cell.getX() + ";" + cell.getY() + ";" + cell.getR() + ";" + cell.getG() + ";" + cell.getB()
						+ ";" + cell.getValue() + ";" + cell.getRadius();

				String nick = indexUsers.get(id);
				if (nick != null) {
					info += ";"+nick;
				}

				info += "@";
				
				
			}
			
			if(co==conn)
			{
				String informa=info;
				info=header+"@"+informa;
				
			}

		

		}
           return info;
	}
	
	
	
	

	private void sendInfoCells(String header, Connection co) {
		// TODO Auto-generated method stub

		for (Connection conn : connections.values()) {

			String info = "";

			for (Integer id : cells.keySet()) {

				Cell cell = cells.get(id);

				info += cell.getX() + ";" + cell.getY() + ";" + cell.getR() + ";" + cell.getG() + ";" + cell.getB()
						+ ";" + cell.getValue() + ";" + cell.getRadius();

				String nick = indexUsers.get(id);
				if (nick != null) {
					info += ";"+nick;
				}

				info += "\n";
				
				
			}
			
			if(co==conn)
			{
				
				String informa=info;
				info=header+"\n"+informa;
				
			}
            
			conn.write(info);
			

		}

		
		//sendInfoCellsUDP(header, conn);
		
		
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.init();
	}

}