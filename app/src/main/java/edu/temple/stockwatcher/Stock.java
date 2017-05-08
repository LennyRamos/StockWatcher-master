package edu.temple.stockwatcher;

import java.io.Serializable;

public class Stock implements Serializable {

    private String company_name;
    private String company_symbol;
    private double price;

    public Stock(String company_name, String company_symbol, double price)
    {
        this.company_name = company_name;
        this.company_symbol = company_symbol;
        this.price = price;
    }

    public Stock(String company_name, String company_symbol)
    {
        this.company_name = company_name;
        this.company_symbol = company_symbol;
    }

    public String getCompanyName()
    {
        return company_name;
    }

    public void setCompanyName(String company_name)
    {
        this.company_name = company_name;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public String getCompanySymbol()
    {
        return company_symbol;
    }

    public void setCompanySymbol(String company_symbol)
    {
        this.company_symbol = company_symbol;
    }
}
