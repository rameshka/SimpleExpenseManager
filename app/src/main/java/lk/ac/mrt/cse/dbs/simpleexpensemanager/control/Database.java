package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

/**
 * Created by Piyumi on 11/18/2016.
 * Access database
 */
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ExpenseManager.db";
    public static final String ACCOUNT_TABLE_NAME = "account";
    public static final String ACCOUNT_COLUMN_AC = "accountNo";
    public static final String ACCOUNT_COLUMN_NAME = "bankName";
    public static final String ACCOUNT_COLUMN_ACHNAME= "accountHolderName";
    public static final String ACCOUNT_COLUMN_BALANCE = "balance";

    public static final String LOG_TABLE_NAME = "log";
    public static final String LOG_COLUMN_AC = "accountNo";
    public static final String LOG_COLUMN_DATE = "date";
    public static final String LOG_COLUMN_AMOUNT = "amount";
    public static final String LOG_COLUMN_EXPENSETYPE = "expenseType";
    public static final String LOG_COLUMN_TRANSID="transID";


    public Database(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table log " +
                        "(transID  integer primary key AUTOINCREMENT ,date  text , accountNo char(6) NOT NULL, expenseType text, amount real)"
        );

        db.execSQL(
                "create table account " +
                        "(accountNo char(6) primary key, bankName text, accountHolderName text, balance real)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS account");
        db.execSQL("DROP TABLE IF EXISTS log");
        onCreate(db);
    }

    public boolean insertAccount (String accountNo, String bankName, String accountHolderName, double balance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountNo", accountNo);
        contentValues.put("bankName", bankName);
        contentValues.put("accountHolderName", accountHolderName);
        contentValues.put("balance", balance);
        db.insert("account", null, contentValues);
        return true;
    }


    public boolean insertTranaction (String date, String accountNo, String expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date",date);
        contentValues.put("accountNo", accountNo);
        contentValues.put("expenseType", expenseType);
        contentValues.put("amount", amount);
        db.insert("log", null, contentValues);
        return true;
    }

    public Cursor getData(String accountNo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from account where id="+accountNo+"", null );
        return res;
    }

    //return number of rows from database for table account
    public int accountNumberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ACCOUNT_TABLE_NAME);
        return numRows;
    }

    //return number of rows from database for table log
    public int logNumberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, LOG_TABLE_NAME);
        return numRows;
    }


    //update account details if needed
    public boolean updateAccount (String accountNo, String bankName, String accountHolderName, double balance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bankName", bankName);
        contentValues.put("accountHolderName", accountHolderName);
        contentValues.put("balance", balance);
        db.update("account", contentValues, "accountNo= ? ", new String[] { accountNo } );
        return true;
    }

    public Integer deleteAccount (String accountNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("account",
                "accountNo = ? ",
                new String[] { accountNo });
    }

    //retrive all account Detalis
    public ArrayList<String> getAllAccounts() {
        ArrayList<String> array_list = new ArrayList<String>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from account", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(ACCOUNT_COLUMN_AC)));
            array_list.add(res.getString(res.getColumnIndex(ACCOUNT_COLUMN_NAME)));
            array_list.add(res.getString(res.getColumnIndex(ACCOUNT_COLUMN_ACHNAME)));
            array_list.add(res.getString(res.getColumnIndex(ACCOUNT_COLUMN_BALANCE)));

            res.moveToNext();
        }
        return array_list;
    }

    //retrive tranaction details
    public ArrayList<String> getAllTransactions() {
        ArrayList<String> array_list = new ArrayList<String>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from log", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(LOG_COLUMN_DATE)));
            array_list.add(res.getString(res.getColumnIndex(LOG_COLUMN_AC)));
            array_list.add(res.getString(res.getColumnIndex(LOG_COLUMN_EXPENSETYPE)));
            array_list.add(res.getString(res.getColumnIndex(LOG_COLUMN_AMOUNT)));

            res.moveToNext();
        }
        return array_list;
    }

}