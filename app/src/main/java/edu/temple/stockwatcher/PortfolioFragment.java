package edu.temple.stockwatcher;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;


public class PortfolioFragment extends Fragment
{
    public static String BUNDLE_KEY = "portfolio";
    String file_name = "portfolioStockList";

    stockSelectedInterface parent;
    ListView portfolio_list;
    Portfolio portfolio;
    File file;
    TextView empty_portfolio_tv;

    public PortfolioFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);
        empty_portfolio_tv = (TextView) view.findViewById(R.id.notif_textView);


        portfolio_list = (ListView) view.findViewById(R.id.listView);

        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            portfolio = (Portfolio) bundle.getSerializable(BUNDLE_KEY); //receives portfolio object from main activity
        }

        //set up adapter
        PortfolioAdapter adapter = new PortfolioAdapter(getContext(),portfolio);
        portfolio_list.setAdapter(adapter);

        if (portfolio.size()==0)
        { //dont show textview if no portofolio exists
            empty_portfolio_tv.setVisibility(View.VISIBLE);
        }
        else
        {
            empty_portfolio_tv.setVisibility(View.GONE);
        }

        File dir = getActivity().getFilesDir();
        file = new File(dir, file_name);

        portfolio_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                ((stockSelectedInterface) getActivity()).stockSelected(portfolio.get(i)); //send to main
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof stockSelectedInterface)
        {
            parent = (stockSelectedInterface) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement stockSelectedInterface");
        }
    }//end of onAttach

    @Override
    public void onDetach()
    {
        super.onDetach();
        parent = null;
    }


    public interface stockSelectedInterface
    {
        public void stockSelected(Stock stock);
    }

    public void addStock(Stock stock)
    {
        portfolio.add(stock);
        writeFile(stock);
        empty_portfolio_tv.setVisibility(View.GONE);

        ((PortfolioAdapter) portfolio_list.getAdapter()).notifyDataSetChanged();
    }

    public void deletePortfolio()
    {
        portfolio.remove(); //remove stock arraylist inside portfolio object
        ((PortfolioAdapter) portfolio_list.getAdapter()).notifyDataSetChanged();
        empty_portfolio_tv.findViewById(View.VISIBLE);//when no portfolio exists, show the textview
    }


    public void writeFile(Stock stock)
    { // append new stock to our file
        try
        {
            Writer writer;
            writer = new BufferedWriter(new FileWriter(file, true));
            writer.append(stock.getCompanySymbol());
            writer.append('\n');
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}//end of class
