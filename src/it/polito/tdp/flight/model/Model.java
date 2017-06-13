package it.polito.tdp.flight.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.flight.db.FlightDAO;

public class Model {
	
	private FlightDAO dao ;
	private List<Airline> compagnie ; 
	private Map<Integer, Airport> aereoporti ;
	private DirectedWeightedMultigraph<Airport, Route> graph ;
	
	public Model(){
		dao = new FlightDAO() ;
		graph = null ;
	}

	public List<Airline> getCompagnie() {
		if(compagnie == null){
			compagnie = new ArrayList<Airline>() ; 
			compagnie = dao.getAllCompagnie() ; 
			aereoporti = null ;
		}
		return compagnie;
	}

	public   List<Airport> getGrafo (Airline a){
		//if(graph==null){
			aereoporti = null ;
			graph = new DirectedWeightedMultigraph<Airport, Route> (Route.class) ;
			List <Airport> lista = this.getAirportAirline (a) ;
			Graphs.addAllVertices(graph, lista ) ;
			graph = this.addArchi(graph, a);
			
//		}
		return lista ;
	}
		
	
	
	private DirectedWeightedMultigraph<Airport, Route> addArchi(DirectedWeightedMultigraph<Airport, Route> grafo, Airline a) {
			List<Route> rotte = dao.getRoute(a) ;
			for(Route r : rotte){
				Airport s = aereoporti.get(r.getSourceAirportId()) ;
				Airport t = aereoporti.get(r.getDestinationAirportId());
				grafo.addEdge(s, t, r) ;
				LatLng ls = new LatLng(s.getLatitude(), s.getLongitude()) ;
				LatLng lt = new LatLng(t.getLatitude(), t.getLongitude()) ;
				double d = LatLngTool.distance (ls, lt, LengthUnit.KILOMETER) ;
				grafo.setEdgeWeight(r, d);
			}
			return grafo ;
	}

	private List<Airport> getAirportAirline( Airline a) {
		List<Airport> ritorno = new ArrayList<Airport> () ;
		if(aereoporti == null){
			aereoporti = new TreeMap<Integer, Airport> () ;
			dao.getAllAirports() ;
			List<Airport> lista = dao.getAllAirportsDataAirline(a);
			for(Airport d : lista){
				aereoporti.put(d.getAirportId(), d) ;
				ritorno.add(d);
			}
		}
		return ritorno;
	}

	public List<Airport> trovaRaggiungibili (Airline compagnia, Airport partenza){
		List<Airport> lista = new ArrayList<Airport> () ;
		for(Airport a : this.getAirportAirline(compagnia)){
			a.setDistanza(partenza);
		}
		
		
		return lista ;
	}
	
	public static void main(String[] args) {
		FlightDAO dao = new FlightDAO() ;
		
		List<Airport> arps = dao.getAllAirports() ;
		//System.out.println(arps);
		
		List<Airline> arl = dao.getAllCompagnie() ;
//		System.out.println(arl);

		Airline r = null;
		for(Airline a : arl){
			if(a.getAirlineId()==19676)
				r = a ;
		}
		

		List<Airport> arp = dao.getAllAirportsDataAirline(r) ;
//		System.out.println(arp);
		
		List<Route> rotte = dao.getRoute(r) ;
//		System.out.println(rotte);
		Model m = new Model() ;
		
		
////		Set<Airport> aereoporti = m.getGrafo(r).vertexSet() ;
//		for(Airport p : aereoporti)
//    		System.out.println(p.toString()+"\n") ;
		
		
		

	}
}
