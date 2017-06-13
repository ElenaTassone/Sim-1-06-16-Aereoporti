package it.polito.tdp.flight.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.polito.tdp.flight.model.Airline;
import it.polito.tdp.flight.model.Airport;
import it.polito.tdp.flight.model.Route;


public class FlightDAO {

	private Map<Integer, Airport> airportMap ;
	private List<Route> rotte ;
	
	public List<Airport> getAllAirports() {
		
		String sql = "SELECT * FROM airport" ;
		
		List<Airport> list = new ArrayList<>() ;
		airportMap = new TreeMap <Integer, Airport> () ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Airport temp = new Airport(
						res.getInt("Airport_ID"),
						res.getString("name"),
						res.getString("city"),
						res.getString("country"),
						res.getString("IATA_FAA"),
						res.getString("ICAO"),
						res.getDouble("Latitude"),
						res.getDouble("Longitude"),
						res.getFloat("timezone"),
						res.getString("dst"),
						res.getString("tz")) ;
				list.add(temp);
				airportMap.put(temp.getAirportId(), temp) ;
			}
			
			conn.close();
			
			return list ;
		} catch (SQLException e) {

			e.printStackTrace();
			return null ;
		}
	}
	

	public List<Airline> getAllCompagnie() {

		String sql = "SELECT * FROM airline" ;
		
		List<Airline> list = new ArrayList<>() ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				list.add(new Airline(res.getInt("Airline_ID"),
						res.getString("Name"), res.getString("Alias"),
						res.getString("IATA"), res.getString("ICAO"),
						res.getString("Callsign"), res.getString("Country"),
						res.getString("Active")));
			}
			
			conn.close();
			
			return list ;
		} catch (SQLException e) {

			e.printStackTrace();
			return null ;
		}
	}

	
	

	public List<Airport> getAllAirportsDataAirline(Airline a) {
		String sql = "SELECT * FROM route WHERE Airline_ID=?" ;
		
		List<Airport> list = new ArrayList<>() ;
		
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, a.getAirlineId());
			
			ResultSet res = st.executeQuery() ;
			rotte = new ArrayList<Route> () ;
			while(res.next()) {
				Route r = new Route ( res.getString("Airline"), 
						res.getInt("Airline_ID"),
						res.getString("Source_airport"),
						res.getInt("Source_airport_ID"),
						res.getString("Destination_airport"), 
						res.getInt("Destination_airport_ID"),
						res.getString("Codeshare"), 
						res.getInt("Stops"),
						res.getString("Equipment")) ;
				rotte.add(r) ;
				
				
				int idSource =  r.getSourceAirportId() ;
				int idDestination = r.getDestinationAirportId() ;
				Airport source = airportMap.get(idSource) ;
				Airport destination = airportMap.get(idDestination) ;
				if(!list.contains(source))
					list.add(source) ;
				if(!list.contains(destination))
					list.add(destination);
				
			}
			
			conn.close();
			
			return list ;
		} catch (SQLException e) {

			e.printStackTrace();
			return null ;
		}
	}
	
	
	public List<Route> getRoute(Airline a ){
		if(rotte == null){
			rotte = new ArrayList<Route> () ;
			this.getAllAirportsDataAirline(a) ;
		}
		return this.rotte ;
	}
	
	public static void main(String args[]) {
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
		System.out.println(rotte);

	}

}
