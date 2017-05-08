package edu.temple.stockwatcher;

import java.io.Serializable;
import java.util.ArrayList;

public class Portfolio implements Serializable
{

    private ArrayList<Stock> stocks;

    public Portfolio(ArrayList<Stock> stocks)
    {
        this.stocks = stocks;
    }

    public Portfolio()
    {
        this.stocks = new ArrayList<Stock>();
    }

    public void add (Stock stock)
    {
        stocks.add(stock);
    }

    public Stock get(int index)
    {
        return stocks.get(index);
    }

    public int size()
    {
        return stocks.size();
    }

    public void remove()
    {
        this.stocks.clear();
    } // to remove the content of array list
}
