package com.mycompany.tqs.gohouse;

/**
 *
 * @author joaos
 */
public class TempPropriadade {


    private String owner;
    private float price;
    private String location;
    private int tipo;
    private String url_imagem;
    private String title;
    private String smallDesc;

    public TempPropriadade(String owner,String title, String smallDesc, float price, String location, int tipo, String url_imagem) {
        this.owner = owner;
        this.title = title;
        this.smallDesc = smallDesc;
        this.price = price;
        this.location = location;
        this.tipo = tipo;
        this.url_imagem = url_imagem;
    }

    
    //----------------- Getters and Setters -----------------------
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getUrl_imagem() {
        return url_imagem;
    }

    public void setUrl_imagem(String url_imagem) {
        this.url_imagem = url_imagem;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSmallDesc() {
        return smallDesc;
    }

    public void setSmallDesc(String smallDesc) {
        this.smallDesc = smallDesc;
    }

    
    
    
    
    
    
}
