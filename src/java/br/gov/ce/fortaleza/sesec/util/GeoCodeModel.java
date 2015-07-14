/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.util;

/**
 *
 * @author lucas.freire
 */
public class GeoCodeModel {
   private String latitude;
   private String longitude;
   
   public GeoCodeModel(){
   }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
   
    
    
    
}
