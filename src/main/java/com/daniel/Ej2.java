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

            System.out.println("Consulta 5:");
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
            System.out.println("No hay departamentos con mas de 2 Empleados(justo son 2)");

            System.out.println("Consulta 6:");
            System.out.println("Que nif:");
            String nif="123456789";
            res="SELECT e.NIF, e.Nom, e.Cognoms, d.IdDepartament\n" +
                    "FROM Empleats e JOIN Departaments d ON e.idDepartament = d.idDepartament\n" +
                    "WHERE e.NIF = ?;";
            PreparedStatement statement3 = connection.prepareStatement(res);
            statement3.setString(1,nif);
            resultat = statement3.executeQuery();
            while (resultat.next()){
                String id = resultat.getString("idDepartament");
                String Nif = resultat.getString("NIF");
                String nom = resultat.getString("Nom");
                System.out.println("Nombre: " + nom + ", NIF: "+Nif+" id: "+id);
            }

            System.out.println("Consulta 7:");
            resultat= sentenciaSQL.executeQuery("SELECT e.Nom, e.Cognoms, e.Salari, d.NomDepartament\n"+
                    "FROM Empleats e JOIN Departaments d ON e.IdDepartament = d.IdDepartament\n"+
                    "WHERE e.salari > (\n"+
                            "SELECT AVG(e2.Salari)\n"+
                            "FROM Empleats e2\n"+
                            "WHERE e2.IdDepartament = e.IdDepartament);");
            while (resultat.next()){
                String nom = resultat.getString("Nom");
                String cog = resultat.getString("Cognoms");
                Double sal = resultat.getDouble("Salari");
                String n_dep = resultat.getString("NomDepartament");
                System.out.println("Nombre: " + nom + ", Cognoms: "+cog+", NIF: "+nif+", Salari: "+sal+" Nom dep: "+ n_dep);
            }

            System.out.println("Consulta 8");
            System.out.println("Que Salario:");
            int id=1;
            res="SELECT d.IdDepartament, d.NomDepartament,\n" +
                    "     (SELECT SUM(e.Salari) \n" +
                    "     FROM Empleats e \n" +
                    "     WHERE e.IdDepartament = d.IdDepartament) AS suma_salaris\n" +
                    "FROM Departaments d\n" +
                    "     WHERE (SELECT SUM(e.Salari) \n" +
                    "                 FROM Empleats e \n" +
                    "                 WHERE e.IdDepartament = d.IdDepartament) > ?;\n";
            PreparedStatement statement4 = connection.prepareStatement(res);
            statement4.setInt(1,id);
            resultat = statement4.executeQuery();
            while (resultat.next()){
                String nom = resultat.getString("NomDepartament");
                int idd = resultat.getInt("IdDepartament");
                System.out.println("Nombre: " + nom + ", ID DEP: "+idd);
            }

            System.out.println("Consulta 9:");
            resultat= sentenciaSQL.executeQuery("SELECT NIF, Nom, Cognoms\n"+
                    "FROM Empleats\n"+
                    "WHERE IdDepartament IS NULL");

            while (resultat.next()){
                String Nif = resultat.getString("NIF");
                String cog = resultat.getString("Cognoms");
                String nom = resultat.getString("Nom");
                System.out.println("NIF: " + Nif + ", Cognoms: "+cog+", Nom: "+nom);
            }

            System.out.println("No hay empleados cuyo id de departamento sea null");

            System.out.println("Consulta 10");
            String NOMBRE="Recursos Humans";
            res="SELECT MAX(e.salari) AS salari_maxim, MIN(e.salari) AS salari_minim\n" +
                    "FROM Empleats e JOIN Departaments d ON e.IdDepartament = d.IdDepartament\n" +
                    "WHERE d.NomDepartament = ?;";
            PreparedStatement statement5 = connection.prepareStatement(res);
            statement5.setString(1,NOMBRE);
            resultat = statement5.executeQuery();
            while (resultat.next()){
                Double salriMax = resultat.getDouble("salari_maxim");
                Double salariMin = resultat.getDouble("salari_minim");
                System.out.println("MIN: " + salariMin + ", MAX: "+salriMax);
            }

            System.out.println("Consulta 11:");
            res= "SELECT e.NIF, e.Nom, e.Cognoms, d.NomDepartament\n" +
                    "FROM Empleats e JOIN Departaments d ON e.IdDepartament = d.IdDepartament\n" +
                    "WHERE d.NomDepartament LIKE ?;\n";
            PreparedStatement statement6 = connection.prepareStatement(res);
            statement6.setString(1,"Desenvolupament");
            resultat = statement6.executeQuery();
            while (resultat.next()){
                String nom = resultat.getString("Nom");
                String Nif = resultat.getString("NIF");
                String cog = resultat.getString("Cognoms");
                String nom_dep = resultat.getString("NomDepartament");
                System.out.println("NIF: " + Nif + ", Cognoms: "+cog+", Nom: "+nom+" Nom_dep: "+nom_dep);
            }



        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("Conexon establecida...");

    }
}
