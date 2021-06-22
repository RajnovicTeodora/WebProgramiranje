package services;

import java.util.ArrayList;
import java.util.Comparator;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Manifestation;
import dao.ManifestationDAO;
import exception.ManifestationNotFoundException;

@Path("/manifestations")
public class ManifestationService {

  @Context
  ServletContext ctx;

  public ManifestationService() {

  }

  @PostConstruct
  public void init() {
    if (ctx.getAttribute("manifestationDAO") == null) {
      String contextPath = ctx.getRealPath("");
      ctx.setAttribute("manifestationDAO", new ManifestationDAO(contextPath));
    }

  }

  @GET
  @Path("/list")
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayList<Manifestation> getManifestations() {
    ManifestationDAO dao = (ManifestationDAO) ctx.getAttribute("manifestationDAO");
    ArrayList<Manifestation> manifestations = dao.findAllList();
    manifestations.sort(new Comparator<Manifestation>() {
      @Override
      public int compare(Manifestation o1, Manifestation o2) {
        return o2.getDate().compareTo(o1.getDate());
      }
    });
    // TODO: sort should be most recent
    return manifestations;

  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Manifestation findOne(@PathParam("id") String id) {
    ManifestationDAO dao = (ManifestationDAO) ctx.getAttribute("manifestationDAO");
    int intId = -1;
    try {

      intId = Integer.parseInt(id);

    } catch (NumberFormatException e) {
      System.out.println(id + " is not a valid integer number");
      return null;
    }

    Manifestation manifestation = dao.findById(intId);
    if(manifestation == null)
    	throw new ManifestationNotFoundException("Manifestation with the id " + id + " not found");
    return manifestation;
  }
  


  /*
   * @GET
   * 
   * @Path("/{name}/{location}/{dateFrom}/{dateTo}/{priceFrom}/{priceTo}")
   * 
   * @Produces(MediaType.APPLICATION_JSON) public ArrayList<Manifestation>
   * findBySearchCriteria(@PathParam("name") String name,
   * 
   * @PathParam("location") String location, @PathParam("dateFrom") String
   * dateFrom, @PathParam("dateTo") String dateTo,
   * 
   * @PathParam("priceFrom") String priceFrom, @PathParam("priceTo") String
   * priceTo) { System.out.println("cao"); ManifestationDAO dao =
   * (ManifestationDAO) ctx.getAttribute("manifestationDAO");
   * ArrayList<Manifestation> manifestations = new ArrayList<Manifestation>();
   * System.out.println("cao"); System.out.println(name + " - " + location + " - "
   * + priceFrom + " - " + priceTo); return manifestations; }
   */
}
