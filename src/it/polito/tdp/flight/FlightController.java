package it.polito.tdp.flight;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.flight.model.Airline;
import it.polito.tdp.flight.model.Airport;
import it.polito.tdp.flight.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FlightController {

	private Model m ;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Airline> boxAirline;

    @FXML
    private ComboBox<Airport> boxAirport;

    @FXML
    
    private TextArea txtResult;
    
    public void setModel(Model model){
    	this.m = model;
    	boxAirline.getItems().addAll(m.getCompagnie()) ;
    	}

    @FXML
    void doRaggiungibili(ActionEvent event) {
    	txtResult.clear();
    	Airline a = boxAirline.getValue() ;
    	

    }

    @FXML
    void doServiti(ActionEvent event) {
    	txtResult.clear();
    	Airline a = boxAirline.getValue() ;
    	List<Airport> aereoporti = m.getGrafo(a) ;
    	if(aereoporti.size()==0)
    		txtResult.setText("La compagnia " +a.toString()+" non serve alcun aereoporto! Selezionarne un'altra");
    		else{
    	txtResult.appendText("Aereoporti serviti da "+a.toString()+": \n");
    	for(Airport p : aereoporti){
    		txtResult.appendText(p.toString()+"\n") ;
    	};
    	boxAirport.getItems().addAll(aereoporti);
    	}
    }

    @FXML
    void initialize() {
        assert boxAirline != null : "fx:id=\"boxAirline\" was not injected: check your FXML file 'Flight.fxml'.";
        assert boxAirport != null : "fx:id=\"boxAirport\" was not injected: check your FXML file 'Flight.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Flight.fxml'.";

    }
}
