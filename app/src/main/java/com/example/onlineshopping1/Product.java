package com.example.onlineshopping1;

import java.io.Serializable;

public class Product {
    int ID;
    String name;
    int price;
    int quantity;
    int CatID;

    public Product(int ID, String name, int price, int quantity, int catID) {
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        CatID = catID;
    }
}

