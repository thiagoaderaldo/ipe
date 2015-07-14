package br.gov.ce.fortaleza.sesec.util;
 
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.map.GeocodeEvent;
import org.primefaces.event.map.ReverseGeocodeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.GeocodeResult;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
 
@ManagedBean
public class GeocodeView {
     
    private GeoCodeModel    model;   
    private MapModel geoModel;
    private MapModel revGeoModel;
    private String centerGeoMap = "-3.7318616, -38.5266704";
    private String centerRevGeoMap = "-3.7318616, -38.5266704";
    private String lat;
    
    @PostConstruct
    public void init() {
        geoModel = new DefaultMapModel();
        revGeoModel = new DefaultMapModel();
    }
     
    public void onGeocode(GeocodeEvent event) {
        
        List<GeocodeResult> results = event.getResults();
             
        if (results != null && !results.isEmpty()) {
            LatLng center = results.get(0).getLatLng();
            centerGeoMap = center.getLat() + "," + center.getLng();
             //System.out.println("Latitude: " + center.getLat() + "\n" + "Longitude: " + center.getLng() );
             
            for (int i = 0; i < results.size(); i++) {
                GeocodeResult result = results.get(i);
                geoModel.addOverlay(new Marker(result.getLatLng(), result.getAddress()));
            }
        }
    }

    public GeoCodeModel getModel() {
        return model;
    }

    public void setModel(GeoCodeModel model) {
        this.model = model;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
    
    public void setLat(){
    }
    
     public GeoCodeModel getSelected() {
        if (model == null) {
            model = new GeoCodeModel();
            
        }
        return model;
    }

    public void create(){
        System.out.println("Latidude: " + model.getLatitude() + 
                "\nLongitudade:"+ model.getLongitude());
    }
    
    
    public void getLatLng(){
        
    }
     
    public void onReverseGeocode(ReverseGeocodeEvent event) {
        List<String> addresses = event.getAddresses();
        LatLng coord = event.getLatlng();
         
        if (addresses != null && !addresses.isEmpty()) {
            
            centerRevGeoMap = coord.getLat() + "," + coord.getLng();
            revGeoModel.addOverlay(new Marker(coord, addresses.get(0)));
        }
    }
 
    public MapModel getGeoModel() {
        return geoModel;
    }
 
    public MapModel getRevGeoModel() {
        return revGeoModel;
    }
 
    public String getCenterGeoMap() {
        return centerGeoMap;
    }
 
    public String getCenterRevGeoMap() {
        return centerRevGeoMap;
    }
}