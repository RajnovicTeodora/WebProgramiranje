package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
public class PokusajService {
	
	@Context
	ServletContext ctx;
	
	public PokusajService() {
		
	}
	
	@PostConstruct
	public void init() {
		
	}
	
	@POST
	@Path("/pokusaj")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response pokusaj(String tekst, @Context HttpServletRequest request) {
		request.getSession().setAttribute("tekst", tekst);
		return Response.status(200).build();
	}
	

}
