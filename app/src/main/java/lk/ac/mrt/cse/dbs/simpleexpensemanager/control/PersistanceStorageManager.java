package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Piyumi on 11/15/2016.
 * Manages Persistance Stoarage of the App
 *
 */
public class PersistanceStorageManager extends ExpenseManager{
    private Context context ;
    private Database mydb;
    private ArrayList<String> getAccountDb;
    private ArrayList<String> getTransactionDb;

    public PersistanceStorageManager(Context passedContext) {
        context = passedContext;
        setup();
    }
    public void createAccounts()

    {
        //retrieving account details from the database

        int entries =mydb.accountNumberOfRows();

        if(entries>0) {
        getAccountDb = mydb.getAllAccounts();
        int a=0;

        for(int k=0;k<entries;k++) {
            Log.i("dbchvalue", getAccountDb.get(0));
            Account tempAccount = new Account(getAccountDb.get(a), getAccountDb.get(a+1), getAccountDb.get(a+2), Double.parseDouble(getAccountDb.get(a+3)));
            getAccountsDAO().addAccount(tempAccount);

            a=a+4;

        }
    }

    }

    //return logsize
    public int getLogsSize()
    {

        return mydb.logNumberOfRows();
    }

    //retrive logs from database
   public void createLogs() {
       int entries = mydb.logNumberOfRows();

       if (entries > 0) {

           getTransactionDb = mydb.getAllTransactions();
           int a=0;

           for(int k=0;k<entries;k++) {

               String startDateString = getTransactionDb.get(a);
               DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
               Date startDate;

               String expense = getTransactionDb.get(a+2);
               ExpenseType expenseEnum = ExpenseType.valueOf(expense);

               try {
                   startDate = df.parse(startDateString);

                   getTransactionsDAO().logTransaction(startDate, getTransactionDb.get(a+1), expenseEnum, Double.parseDouble(getTransactionDb.get(a+3)));
                   a=a+4;

               } catch (ParseException e) {
                   e.printStackTrace();
               }

           }

       }
   }


    //when new account being created saved in to database

    public void updateNewAccount( String  accountNumStr,String bankNameStr,String accountHolderStr,double initialBalance)
    {
         mydb.insertAccount(accountNumStr, bankNameStr, accountHolderStr,initialBalance);
    }


    //when new transaction being created saved in to datbase

    public void updateNewLog(String accountNo,int day,int month,int year,String expenseType,double amount)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date transactionDate = calendar.getTime();

        String DATE_FORMAT_NOW = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        String stringDate = sdf.format(transactionDate );

        mydb.insertTranaction(stringDate,accountNo,expenseType,amount);

    }

    public void setup()  {
        /*** Begin generating dummy data for In-Memory implementation ***/


        TransactionDAO inMemoryTransactionDAO = new InMemoryTransactionDAO();
        setTransactionsDAO(inMemoryTransactionDAO);

        AccountDAO inMemoryAccountDAO = new InMemoryAccountDAO();
        setAccountsDAO(inMemoryAccountDAO);


        // dummy data
        mydb = new Database(context);

        int i = mydb.logNumberOfRows();
        Log.i("logsrows",Integer.toString(i));

       //retive data from database at the start of the app
       createAccounts();
       createLogs();

        /*** End ***/
    }

}
