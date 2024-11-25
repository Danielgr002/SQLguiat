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

            System.out.println("Consueltes 1");
            while (resultat.next()){
                String nif= resultat.getString("NIF");
                String nom = resultat.getString("Nom");
                String cognom = resultat.getString("Cognoms");
                String nom_dep = resultat.getString("NomDepartament");
                System.out.println(nif+" | "+nom+" | "+cognom+" | "+nom_dep);
            }

            System.out.println("Consueltes 2");
            resultat= sentenciaSQL.executeQuery("SELECT NIF, Nom, Cognoms\n"+
                                                    "FROM EMPLEATS;");
            while (resultat.next()){
                String nif = resultat.getString("NIF");
                String nom = resultat.getString("Nom");
                String cognom = resultat.getString("Cognoms");
                System.out.println(nif+" | "+nom+" | "+cognom);
            }
            System.out.println("Consulta 3");
            System.out.println("que departamento:");
            String departament="Recursos Humans";
            res="SELECT e.Nom, d.NomDepartament\n"+
                    "FROM Empleats e\n"+
                    "LEFT JOIN Departaments d ON e.IdDepartament = d.IdDepartament\n"+
                    "WHERE d.NomDepartament=?";
            PreparedStatement statement = connection.prepareStatement(res);
            statement.setString(1,departament);
            resultat = statement.executeQuery();
            while (resultat.next()){
                String nom = resultat.getString("Nom");
                String nom_dep = resultat.getString("NomDepartament");
                System.out.println("Nombre: " + nom + ", NomDepartament: "+nom_dep);
            }

            System.out.println("Consulta 4");
            System.out.println("Que Salario:");
            Double salari=2700.5;
            res="SELECT Nom, Salari\n"+
                    "FROM Empleats\n"+
                    "WHERE Salari>?";
            PreparedStatement statement2 = connection.prepareStatement(res);
            statement2.setDouble(1,salari);
            resultat = statement2.executeQuery();
            while (resultat.next()){
                String nom = resultat.getString("Nom");
                Double sal = resultat.getDouble("Salari");
                System.out.println("Nombre: " + nom + ", Salari: "+sal);
            }

            System.out.println("Consulta 5");
            resultat= sentenciaSQL.executeQuery("SELECT NIF, Nom, Cognoms\n" +
                                                    "FROM Empleats\n" +
                                                    "WHERE IdDepartament IN \n" +
                                                    "   (SELECT IdDepartament\n" +
                                                    "    FROM Empleats\n" +
                                                    "    GROUP BY IdDepartament\n" +
                                                    "    HAVING COUNT(*) > 2);");
            while (resultat.next()){
                String nif = resultat.getString("NIF");
                String nom = resultat.getString("Nom");
                String cog = resultat.getString("Cognoms");
                System.out.println("Nombre: " + nom + ", Cognoms: "+cog+", NIF: "+nif);
            }


        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("Conexon establecida...");

    }
}
