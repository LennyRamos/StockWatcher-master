package edu.temple.stockwatcher;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;


public class StockDetailsFragment extends Fragment
{

    View view;
    ImageView graph_image_view;
    TextView company_name;
    TextView company_price;

    Logger log = Logger.getAnonymousLogger();
    Thread thread1;


    public StockDetailsFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_stock_details, container, false);

        graph_image_view = (ImageView) view.findViewById(R.id.graph);
        company_name = (TextView) view.findViewById(R.id.stock_name);
        company_price = (TextView) view.findViewById(R.id.stock_price);

        return view;
    }

    public void showGraph(Stock stock)
    { //show graph
        Picasso.with(graph_image_view.getContext()).load("https://chart.yahoo.com/z?thread1=1d&s="+stock.getCompanySymbol()).into(graph_image_view);
    }

    public void showStockInfo(Stock stock)
    {
        showGraph(stock);
        retrieveStockPrice(stock);
    }

    public void retrieveStockPrice(Stock stock)
    {
        log.info("retrieveStockPriceData");
        final String urlString = "http://dev.markitondemand.com/MODApis/Api/v2/Quote/json/?symbol=" + stock.getCompanySymbol();
        thread1 = new Thread()
        {
            @Override
            public void run()
            {
                log.info("run() is called");
                try
                {
                        URL url = new URL(urlString);
                        BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    url.openStream()));

                        String tmpString = "";
                        String response = "";

                        while (tmpString != null)
                        {
                            response.concat(tmpString);
                            response = response + tmpString;
                            tmpString = reader.readLine();
                        }
                        Message msg = Message.obtain();
                        msg.obj = response;

                        Log.d("downloaded data", response);
                        responseHandler.sendMessage(msg);// get stock's price and company name
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };

        thread1.start();
    }//end of retrieveStockPrice

    Handler responseHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            try
            {
                JSONObject blockObject = new JSONObject((String) msg.obj);
                String name = blockObject.getString("Name");
                String price = blockObject.getString("LastPrice");

                Log.d("responseHandler:", "response handler is called");

                company_name.setText(name);
                company_price.setText("$"+price);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return true;
        }
    });

    @Override
    public void onDetach()
    {
        super.onDetach();
        if(thread1 != null)
        {
            thread1.interrupt(); //stop the thread1 after the fragment is closed
        }
    }



}
