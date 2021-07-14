    package com.example.onlineshopping1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DatabaseName = "ECommerceDB";
    SQLiteDatabase ECommerceDB;

    public DatabaseHelper(Context context) {
        super(context, DatabaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table customer(CustID integer primary key autoincrement, CustName text, Username text not null," +
                "Password text not null,Gender text, Birthdate text, Job text, securityAns text not null);");

        db.execSQL("create table category (CatID integer primary key autoincrement, CatName text);");

        db.execSQL("create table product(ProdID integer primary key autoincrement, ProdName text, Price integer not null," +
                "Quantity integer not null,Barcode text, Cat_ID integer, FOREIGN KEY(Cat_ID) REFERENCES category (CatID));");

        db.execSQL("create table orders(OrderID integer primary key autoincrement, OrderDate text not null, Address text not null," +
                "CustomerID integer ,FOREIGN KEY(CustomerID) References customer(CustID));");

        db.execSQL("create table orderDetails(Quantity integer," +
                "Order_ID integer, Prod_ID integer, FOREIGN KEY(Order_ID) References orders(OrderID)," +
                "Foreign key(Prod_ID) references categories(ProdID), primary key (Order_ID, Prod_ID));");

        db.execSQL("create table cart(id integer primary key , Cust_ID integer, Prod_ID integer, " +
                "FOREIGN KEY(Cust_ID) References customer(CustID)," +
                "Foreign key(Prod_ID) references categories(ProdID));");


        ContentValues row = new ContentValues();
        row.put("CatName", "Horror");
        db.insert("category", null, row);
        row.put("CatName", "Fiction");
        db.insert("category", null, row);
        row.put("CatName", "Young Adult");
        db.insert("category", null, row);
        row.put("CatName", "Comic Books");
        db.insert("category", null, row);
        row.put("CatName", "Murder");
        db.insert("category", null, row);

        row = new ContentValues();
        row.put("ProdName", "Harry Potter");
        row.put("Price", 8);
        row.put("Quantity", 2);
        row.put("Cat_ID", 2);
        row.put("Barcode","9781408855713");
        db.insert("product", null, row);
        row.put("ProdName", "The Great Gatsby");
        row.put("Price", 9);
        row.put("Quantity", 5);
        row.put("Cat_ID", 2);
        row.put("Barcode","9789771446101");
        db.insert("product", null, row);
        row.put("ProdName", "The Shinning");
        row.put("Price", 10);
        row.put("Quantity", 5);
        row.put("Cat_ID", 1);
        row.put("Barcode","6251001246667");
        db.insert("product", null, row);
        row.put("ProdName", "It");
        row.put("Price", 5);
        row.put("Quantity", 5);
        row.put("Cat_ID", 1);
        row.put("Barcode","6222010610992");
        db.insert("product", null, row);
        row.put("ProdName", "Maze Runner");
        row.put("Price", 11);
        row.put("Quantity", 5);
        row.put("Cat_ID", 3);
        row.put("Barcode","6221508010719");
        db.insert("product", null, row);
        row.put("ProdName", "The Fault in Our Stars");
        row.put("Price", 6);
        row.put("Quantity", 5);
        row.put("Cat_ID", 3);
        row.put("Barcode","6221151004691");
        db.insert("product", null, row);
        row.put("ProdName", "Spider-man");
        row.put("Price", 5);
        row.put("Quantity", 4);
        row.put("Cat_ID", 4);
        row.put("Barcode","622300731093");
        db.insert("product", null, row);
        row.put("ProdName", "Batman");
        row.put("Price", 4);
        row.put("Quantity", 6);
        row.put("Cat_ID", 4);
        row.put("Barcode","622300731383");
        db.insert("product", null, row);
        row.put("ProdName", "Crooked House");
        row.put("Price", 9);
        row.put("Quantity", 5);
        row.put("Cat_ID", 5);
        row.put("Barcode","9780007154852");
        db.insert("product", null, row);
        row.put("ProdName", "The Murder at the vicarage");
        row.put("Price", 7);
        row.put("Quantity", 3);
        row.put("Cat_ID", 5);
        row.put("Barcode","9789953719085");
        db.insert("product", null, row);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists customers");
        db.execSQL("drop table if exists categories");
        db.execSQL("drop table if exists products");
        db.execSQL("drop table if exists orders");
        db.execSQL("drop table if exists orderDetails");
        onCreate(db);
    }

    public void addCustomer(String name, String username, String password, String gender, String DOB, String job, String securityAns) {
        ContentValues row = new ContentValues();
        row.put("CustName", name);
        row.put("Username", username);
        row.put("Password", password);
        row.put("Gender", gender);
        row.put("Birthdate", DOB);
        row.put("Job", job);
        row.put("securityAns", securityAns);
        ECommerceDB = getWritableDatabase();
        ECommerceDB.insert("customer", null, row);
        ECommerceDB.close();
    }

    public Cursor checkUsername(String username) {
        ECommerceDB = getReadableDatabase();
        String[] arg = {username};
        Cursor cursor = ECommerceDB.rawQuery("select CustID,Username from customer where Username = ?", arg);
        cursor.moveToFirst();
        return cursor;
    }

    public boolean checkPassword(String username, String pass) {
        ECommerceDB = getReadableDatabase();
        String[] arg = {username};
        Cursor cursor = ECommerceDB.rawQuery("select Password from customer where Username = ?", arg);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.getString(0).equals(pass))
                return true;
            else
                return false;
        } else
            return false;
    }

    public boolean updatePassword(String username, String newPass, String securityAns) {
        ECommerceDB = getReadableDatabase();
        String rightAns = "";
        String[] arg = {username};
        Cursor cursor = ECommerceDB.rawQuery("select securityAns from customer where Username = ?", arg);
        if (cursor != null) {
            cursor.moveToFirst();
            rightAns = cursor.getString(0);
        }
        if (securityAns.equals(rightAns)) {
            ECommerceDB = getWritableDatabase();
            ContentValues row = new ContentValues();
            row.put("Username", username);
            row.put("Password", newPass);
            ECommerceDB.update("customer", row, "Username = ?", new String[]{username});
            ECommerceDB.close();
            return true;
        } else
            return false;
    }

    public Cursor searchText(String input) {
        ECommerceDB = getReadableDatabase();
        String[] arg = {input};
        Cursor cursor = ECommerceDB.rawQuery("select ProdID,ProdName,Price,Quantity,Cat_ID from product " +
                "where ProdName like '%" + input + "%';", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor searchTextByCat(String input, int catID) {
        ECommerceDB = getReadableDatabase();
        String[] arg = {String.valueOf(catID)};
        Cursor cursor = ECommerceDB.rawQuery("select ProdID,ProdName,Price,Quantity,Cat_ID from product " +
                "where ProdName like '%" + input + "%' AND Cat_ID like ?;", arg);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getProducts() {
        ECommerceDB = getReadableDatabase();
        Cursor cursor = ECommerceDB.rawQuery("select ProdID,ProdName,Price,Quantity,Cat_ID from product " +
                "where Quantity > 0;", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        ECommerceDB.close();
        return cursor;
    }

    public Cursor getCategories() {
        ECommerceDB = getReadableDatabase();
        String[] rowDetails = {"CatID", "CatName"};
        Cursor cursor = ECommerceDB.query("category", rowDetails, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        ECommerceDB.close();
        return cursor;
    }

    public Cursor getProductByCat(int Cat_ID) {
        ECommerceDB = getReadableDatabase();
        String[] arg = {String.valueOf(Cat_ID)};
        Cursor cursor = ECommerceDB.rawQuery("select ProdID,ProdName,Price,Quantity,Cat_ID from product where Cat_ID = ?", arg);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        ECommerceDB.close();
        return cursor;
    }

    public void AddToCart(int prodID, int custID) {
        ECommerceDB = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("Prod_ID", prodID);
        row.put("Cust_ID", custID);
        ECommerceDB.insert("cart", null, row);
        ECommerceDB.close();
    }

    public Cursor GetCart(int custID) {
        ECommerceDB = getReadableDatabase();
        String[] arg = {String.valueOf(custID)};
        Cursor cursor = ECommerceDB.rawQuery("select ProdID,ProdName,Price,Quantity,Cat_ID from cart,product where Cust_ID = ? " +
                "and cart.Prod_ID = product.ProdID", arg);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        ECommerceDB.close();
        return cursor;
    }

    public void updateCart(ArrayList<Product> products, int id, HashMap<Integer, Integer> quantity,int Userid) {
        ECommerceDB = getWritableDatabase();
        ECommerceDB.delete("cart","Cust_ID='"+Userid+"'",null);
        int q;
        for (int i = 0; i < products.size(); i++) {
            q = quantity.get(products.get(i).ID);
            for (int j = 0; j < q; j++) {
                AddToCart(products.get(i).ID, id);
            }
        }
        ECommerceDB.close();
    }
    public Cursor barcodeSearch(String barcode) {
        ECommerceDB = getWritableDatabase();
        String[] arg = {barcode};
        Cursor cursor = ECommerceDB.rawQuery("select ProdID,ProdName,Price,Quantity,Cat_ID from product where Barcode = ? ", arg);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        ECommerceDB.close();
        return cursor;
    }

    public HashMap<Integer, Integer> getCart(int id) {
        HashMap<Integer, Integer> quantity = new HashMap<Integer, Integer>();

        ArrayList<Product> cartList = new ArrayList<Product>();
        Cursor c2 = GetCart(id);

        while (!c2.isAfterLast() && c2.getCount() > 0) {
            int key = c2.getInt(0);
            if (quantity.containsKey(key)) {
                quantity.put(key, quantity.get(key) + 1);
            } else {
                quantity.put(key, 1);
                cartList.add(new Product(c2.getInt(0), c2.getString(1), c2.getInt(2)
                        , c2.getInt(3), c2.getInt(4)));
            }
            c2.moveToNext();
        }

        return quantity;
    }

    public ArrayList<Product> getData(Cursor cursor, int id) {
        HashMap<Integer, Integer> quantity = getCart(id);

        ArrayList<Product> arrayL = new ArrayList<Product>();

        int q = 0;
        while (!cursor.isAfterLast()) {
            if (quantity.containsKey(cursor.getInt(0)))
                q = quantity.get(cursor.getInt(0));
            else
                q = 0;
            arrayL.add(new Product(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)
                    , (cursor.getInt(3) - q), cursor.getInt(4)));
            cursor.moveToNext();
        }
        return arrayL;
    }
    public int addOrder(String date, int CustID, String address) {
        ContentValues row = new ContentValues();
        row.put("OrderDate", date);
        row.put("Address", address);
        row.put("CustomerID", CustID);
        ECommerceDB = getWritableDatabase();
        int orderID = (int) ECommerceDB.insert("orders", null, row);
        ECommerceDB.close();
        return orderID;
    }
    public void addOrderDetails(int Quantity, int Prod_ID, int Order_ID) {
        ContentValues row = new ContentValues();
        row.put("Quantity", Quantity);
        row.put("Order_ID", Order_ID);
        row.put("Prod_ID", Prod_ID);
        ECommerceDB = getWritableDatabase();
        ECommerceDB.insert("orderDetails", null, row);
        ECommerceDB.close();
    }
    public void updateProducts(ArrayList<Product> products, HashMap<Integer, Integer> quantity,int Userid) {
        ECommerceDB = getWritableDatabase();
        ECommerceDB.delete("cart","Cust_ID='"+Userid+"'",null);
        int q, id;
        for (int i = 0; i < products.size(); i++) {
            q = products.get(i).quantity - quantity.get(products.get(i).ID);
            id = products.get(i).ID;
            ContentValues row = new ContentValues();
            row.put("ProdID", id);
            row.put("Quantity", q);
            ECommerceDB.update("product", row, "ProdID = ?", new String[]{String.valueOf(id)});
        }
        ECommerceDB.close();
    }
    public Cursor GetOrders(int custID) {
        ECommerceDB = getReadableDatabase();
        String[] arg = {String.valueOf(custID)};
        Cursor cursor = ECommerceDB.rawQuery("select OrderID,OrderDate,Prod_ID from orders,orderDetails where CustomerID = ? " +
                "and orders.OrderID = orderDetails.Order_ID", arg);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        ECommerceDB.close();
        return cursor;
    }
    public HashMap<String, Integer> GetOrdersInProduct(int custID) {
        Cursor c = GetOrders(custID);
        if (c != null) {
            c.moveToFirst();
        }
        ECommerceDB = getReadableDatabase();
        HashMap<String, Integer> AmountsEachMonth = new HashMap<>();
        int amount = 0;
        while(!c.isAfterLast()){
            String[] arg = {String.valueOf(c.getInt(2))};
            Cursor cursor = ECommerceDB.rawQuery("select ProdID,Price from product where product.ProdID = ? ", arg);
            if (cursor != null) {
                cursor.moveToFirst();
            }
            String[] parts = c.getString(1).split("-");
            if (AmountsEachMonth.containsKey(parts[1]))
                amount = AmountsEachMonth.get(parts[1]);
            else
                amount = 0;
            AmountsEachMonth.put(parts[1],amount+cursor.getInt(1));
            c.moveToNext();
        }
        ECommerceDB.close();
        return AmountsEachMonth;
    }

}
