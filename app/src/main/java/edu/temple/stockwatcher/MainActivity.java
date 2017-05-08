package edu.temple.stockwatcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Lenny Ramos
 * Lab10
 *
 * */

public class MainActivity extends AppCompatActivity implements PortfolioFragment.stockSelectedInterface
{

    StockDetailsFragment receiver;
    PortfolioFragment sender;

    public Portfolio portfolio;
    private final int POPUP_ACTIVITY = 1;

    boolean two_panes;
    File file;
    String new_stock;
    String stock_file_name = "stockList";
    Logger log = Logger.getAnonymousLogger();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        portfolio = new Portfolio();

        two_panes = (findViewById(R.id.stockdetails_frag)!= null);
        receiver = new StockDetailsFragment();
        sender = new PortfolioFragment();

        File directory = this.getFilesDir();
        file = new File(directory, stock_file_name);

        if (file.exists())
        { //if file exists when we open the program
            try
            {
                BufferedReader buffer_reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = buffer_reader.readLine()) != null)
                {
                    portfolio.add(new Stock("", line.toString())); //read the stock and populate the portfolio object
                }
                buffer_reader.close();
            } catch (IOException e)
                {
                e.printStackTrace();
                }
        }
        else
        { //if file doesnt exists
            log.info("No portfolio exists when we open the program");
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(PortfolioFragment.BUNDLE_KEY, portfolio); //send portfolio object to portfolio fragment
        sender.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.portfolio_frag, sender)
                .commit();

        receiver = new StockDetailsFragment();
        //if stockdetails fragment exists show the fragment
        if(two_panes)
        {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.stockdetails_frag, receiver)
                    .commit();
        }
    }//end of onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.add_button :
                searchPopUp();
                return true;

            case R.id.trash_button :
                boolean deleted;
                if(file.exists())
                {
                    if(deleted = file.delete())
                    {
                        log.info("stockList file deleted ");
                        portfolio.remove();
                        sender.deletePortfolio();
                    }
                    else
                    {
                        log.info("file.delete() result: " + deleted);
                    }
                }
                else
                {
                    log.info("no file to delete");
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }//end of onOptionsItemSelected

    private void fragTransition()
    {
        //Logger log = Logger.getAnonymousLogger();
        log.info("fragTransition() method executed");

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.portfolio_frag, receiver)
                .addToBackStack(null)
                .commit();

        getFragmentManager().executePendingTransactions();
    }//end of fragTransition

    @Override
    public void stockSelected(Stock stock)
    {
        if (!two_panes)
        {
            fragTransition();
        }
        receiver.showStockInfo(stock);// show name, price, graph inside stockdetails frag
        getFragmentManager().executePendingTransactions();
    }

    private void searchPopUp()
    { //popup search activity

        Intent intent = new Intent(this, StockSearchActivity.class);
        startActivityForResult(intent, POPUP_ACTIVITY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == POPUP_ACTIVITY)
        { //receive stock symbol based on user input from StockSearch activity

            if (resultCode == RESULT_OK)
            {
                new_stock = data.getStringExtra("symbol");
                sender.addStock(new Stock("", new_stock));
                System.out.println(data.getStringExtra("symbol"));
            }
            else if (resultCode == RESULT_CANCELED)
            {
                //nothing
                log.info("nothing");
            }
        }
    }

}//end of main
