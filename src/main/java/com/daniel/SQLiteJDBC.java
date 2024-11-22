package com.daniel;

import java.sql.*;

public class SQLiteJDBC {
    public static void main(String[] args) {
        Connection connection;
        Statement sentenciaSQL;
        ResultSet res;
        try {

            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/Empleats.db");
            System.out.println("Conexon establecida...");
            System.out.println("CONSULTA 1");
            sentenciaSQL = connection.createStatement();
            res = sentenciaSQL.executeQuery("SELECT * FROM Empleats WHERE salari > 2000");
            System.out.println("Nif         |   Nom     |    Cognoms    |       Salari");
            System.out.println("---------------------------------------------------------------");
            while (res.next()){
                String nif = res.getString("nif");
                String nom = res.getString("nom");
                String cognoms = res.getString("cognoms");
                Double salari = res.getDouble("salari");
                System.out.println(nif +"   |   "+nom+" \t|    "+cognoms+"  \t|     "+salari);
            }

            System.out.println("CONSULTA 2");
            sentenciaSQL = connection.createStatement();
            res = sentenciaSQL.executeQuery("SELECT * FROM Empleats WHERE cognoms='Soler Sánchez'");
            System.out.println("Nif         |   Nom     ");
            System.out.println("------------------------");
            while (res.next()){
                String nif = res.getString("nif");
                String nom = res.getString("nom");
                System.out.println(nif +"   |   "+nom);
            }

            System.out.println("CONSULTA 3");
            sentenciaSQL = connection.createStatement();
            res = sentenciaSQL.executeQuery("SELECT * FROM Empleats ORDER BY salari DESC");
            System.out.println("Nif         |   Nom     |    Cognoms    |       Salari");
            System.out.println("------------------------------------------------------");
            while (res.next()){
                String nif = res.getString("nif");
                String nom = res.getString("nom");
                String cognoms = res.getString("cognoms");
                Double salari = res.getDouble("salari");
                System.out.println(nif +"   |   "+nom+" \t|    "+cognoms+"  \t|     "+salari);
            }

            System.out.println("CONSULTA 4");
            sentenciaSQL = connection.createStatement();
            res = sentenciaSQL.executeQuery("SELECT * FROM Empleats ORDER BY salari DESC");
            System.out.println("Nif         |   Nom     |    Cognoms    |       Salari");
            System.out.println("------------------------------------------------------");
            while (res.next()){
                
            }

            res.close();
            sentenciaSQL.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}