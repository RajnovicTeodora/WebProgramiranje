package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.Comment;
import beans.Location;
import beans.Manifestation;
import beans.ManifestationStatus;
import beans.ManifestationType;
import beans.RegisteredUser;
import beans.Ticket;
import beans.TicketStatus;
import beans.TicketType;
import beans.User;


public class TicketDAO {
	private Map<String, Ticket> tickets = new HashMap<String, Ticket>();

	private String contextPath;

	public TicketDAO() {

	}

	public TicketDAO(String contextPath) {
		this.contextPath = contextPath;
		loadTickets(contextPath);
	}

	public Ticket findById(String id) {
		if (!tickets.containsKey(id)) {
			return null;
		}
		return tickets.get(id);
	}

	public Ticket addTicket(Ticket ticket) {
		if(findById(ticket.getId()) != null)
			return null;
		tickets.put(ticket.getId(), ticket);
		return ticket;
	}
	
	public Ticket updateTicket(Ticket ticket) {
		tickets.replace(ticket.getId(), ticket);
		return ticket;
	}

	public Collection<Ticket> findAll() {
		return tickets.values();
	}
	
	public ArrayList<Ticket> findAllList(){
		ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
		for (Ticket ticket : findAll()) {
			ticketList.add(ticket);
		}
		return ticketList;
	}

	public String findId() {
		int i = 1000000000;
		
		while(true) {
			if(tickets.containsKey(Integer.toString(i))) {
				i++;
			}else {
				return Integer.toString(i);
			}
		}
	}
	
	public boolean writeTicket(Ticket ticket) {
		FileWriter writer;
		try {
			writer = new FileWriter(this.contextPath + "Resources\\csvFiles\\tickets.csv", true);
			writer.write(ticket.toCsvString());
			writer.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 		
	}
	
	public boolean writeAllTickets() {
		FileWriter writer;
		try {
			writer = new FileWriter(this.contextPath + "Resources\\csvFiles\\tickets.csv", false);
			
			for(Ticket t : findAllList()) {
				writer.write(t.toCsvString());
			}
			writer.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
	}
	
	public List<Ticket> getUserTickets(String username){
		List<Ticket>  userTickets = new ArrayList<Ticket>();
		for (Ticket ticket : findAll()) {
			if(ticket.getBuyerUsername().equals(username)) {
				userTickets.add(ticket);
			}
		}
		return userTickets;
	}
	
	private void loadTickets(String contextPath) {
//		Manifestation m1 = new Manifestation(1, "Manifestation1", ManifestationType.FESTIVAL, 20, LocalDateTime.now().plusDays(1), 10 , ManifestationStatus.ACTIVE, new Location(), "ticket.png");
//		Manifestation m2 = new Manifestation(2, "Manifestation2", ManifestationType.FESTIVAL, 20, LocalDateTime.now().plusDays(3), 10 , ManifestationStatus.ACTIVE, new Location(), "ticket.png");
//		Manifestation m3 = new Manifestation(3, "Manifestation3", ManifestationType.FESTIVAL, 20, LocalDateTime.now(), 10 , ManifestationStatus.ACTIVE, new Location(), "ticket.png");
//		m1.setId(1);
//		m2.setId(2);
//		m3.setId(3);
//		
//		Ticket t1 = new Ticket("1", m1 , LocalDateTime.now().plusDays(20), 11, "Mika Mikic", TicketStatus.RESERVED, TicketType.REGULAR);
//		Ticket t2 = new Ticket("2", m2 , LocalDateTime.now().plusDays(30), 11, "Pera Peric", TicketStatus.RESERVED, TicketType.REGULAR);
//		Ticket t3 = new Ticket("3", m3 , LocalDateTime.now(), 11, "Laza Lazic", TicketStatus.RESERVED, TicketType.REGULAR);
//		
//		addTicket(t1);
//		addTicket(t2);
//		addTicket(t3);
		
		BufferedReader bufferedReader = null;
		try {

			FileReader reader = new FileReader(contextPath + "Resources\\csvFiles\\tickets.csv"); 
			bufferedReader = new BufferedReader(reader);
			String line;
			
			line =  bufferedReader.readLine();
			
			while (line != null){
				
				if ( line.charAt(0) == '#') {
					line = bufferedReader.readLine();
					continue;
				}
				
				String[] st = line.split(";");
				
				String id = st[0].trim();
				Manifestation manifestation = ToiToiDAO.getManifestation(contextPath, Integer.parseInt(st[1]));
				LocalDateTime date = LocalDateTime.parse(st[2]);
				double price = Double.parseDouble(st[3]);
				String buyerName = st[4];
				String byerUsername = st[5];
				TicketStatus status = TicketStatus.values()[Integer.parseInt(st[6])];
				TicketType type = TicketType.values()[Integer.parseInt(st[7])];
				
				Ticket ticket = new Ticket(id, manifestation, date, price, buyerName, status, type);
				ticket.setBuyerUsername(byerUsername);
				addTicket(ticket);
				
				line = bufferedReader.readLine();
			}			
			reader.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception e) {
				}
			}
		}
	}
}
