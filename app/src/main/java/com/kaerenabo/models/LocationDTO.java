package com.kaerenabo.models;



public class LocationDTO {

    private String tekst;
    private AddressDTO adresse;

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public AddressDTO getAdresse() {
        return adresse;
    }

    public void setAdresse(AddressDTO adresse) {
        this.adresse = adresse;
    }

}
