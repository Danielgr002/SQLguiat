package com.daniel;

import java.sql.*;

public class Ej2 {
    public static void main(String[] args) {
        Connection connection;
        Statement sentenciaSQL;
        ResultSet resultat;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite.db");
            sentenciaSQL = connection.createStatement();
            String res = "CREATE TABLE IF NOT EXISTS Departaments (\n" +
                    "    IdDepartament INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "    NomDepartament VARCHAR(100) NOT NULL,\n" +
                    "    Responsable VARCHAR(100)\n" +
                    ");";
            sentenciaSQL.executeUpdate(res);

            res="ALTER TABLE Empleats DROP COLUMN IdDepartament";

            sentenciaSQL.executeUpdate(res);

            res="ALTER TABLE Empleats ADD COLUMN IdDepartament INTEGER REFERENCES Departaments(IdDepartament)";
            sentenciaSQL.executeUpdate(res);


            res="INSERT INTO Departaments (NomDepartament, Responsable) VALUES\n" +
                    "('Recursos Humans', 'Marta Pérez'),\n" +
                    "('Desenvolupament', 'Jaume Martí'),\n" +
                    "('Comptabilitat', 'Fina Soler');";
            sentenciaSQL.executeUpdate(res);
            res="UPDATE Empleats SET IdDepartament = CASE\n" +
                    "    WHEN NIF = '123456789' THEN 1 -- Recursos Humans\n" +
                    "    WHEN NIF = '987654321' THEN 2 -- Desenvolupament\n" +
                    "    WHEN NIF = '111111111' THEN 3 -- Comptabilitat\n" +
                    "    WHEN NIF = '222222222' THEN 1 -- Recursos Humans\n" +
                    "    WHEN NIF = '333333333' THEN 2 -- Desenvolupament\n" +
                    "    WHEN NIF = '444444444' THEN 3 -- Comptabilitat\n" +
                    "END;";
            sentenciaSQL.executeUpdate(res);

            resultat = sentenciaSQL.executeQuery("SELECT e.NIF, e.Nom, e.Cognoms, d.NomDepartament, e.IdDepartament AS EmpleatIdDepartament\n"+
                                                "FROM Empleats e\n"+
                                                "LEFT JOIN Departaments d ON e.IdDepartament = d.IdDepartament;\n");

            while (resultat.next()){
                String nif= resultat.getString("NIF");
                String nom = resultat.getString("Nom");
                String cognom = resultat.getString("Cognoms");
                String nom_dep = resultat.getString("NomDepartament");
                System.out.println(nif+" | "+nom+" | "+cognom+" | "+nom_dep);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("Conexon establecida...");


    }
}
