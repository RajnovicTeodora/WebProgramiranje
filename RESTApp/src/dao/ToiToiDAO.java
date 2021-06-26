package dao;

import java.util.List;

import beans.Location;
import beans.Manifestation;
import beans.RegisteredUser;
import beans.Ticket;

public class ToiToiDAO {
	
	
	public static Location getManifestationLocation(String path, int id) {
		LocationDAO dao = new LocationDAO(path);
		return dao.findById(id);		
	}
	
	public static RegisteredUser getUser(String path, String username) {
		RegisteredUserDAO dao = new RegisteredUserDAO(path);
		return (RegisteredUser) dao.findByUsername(username);
	}
	
	public static Manifestation getManifestation(String path, int id) {
		ManifestationDAO dao = new ManifestationDAO(path);
		return dao.findById(id);
	}
	
	public static List<Ticket> getUserTickets(String path, String username){
		TicketDAO dao = new TicketDAO(path);
		return dao.getUserTickets(username);
	}
	
	public static List<Manifestation> getVendorManifestations(String path, String username){
		ManifestationDAO dao = new ManifestationDAO(path);
		return dao.findByVendor(username);
	}
	
	

}
