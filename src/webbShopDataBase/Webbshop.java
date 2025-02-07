package webbShopDataBase;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Webbshop {

    // Metod för att hämta databasanslutningen från properties
    private static Connection getConnection() throws SQLException, IOException {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream("src/webbShopDataBase/setting.properties")) {
            properties.load(inputStream);
        }

        String connectionString = properties.getProperty("connectionString");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");

        return DriverManager.getConnection(connectionString, user, password);
    }

    public static void main(String[] args) throws SQLException, IOException {
        try (Connection conn = getConnection();
             Scanner scanner = new Scanner(System.in)) {

            BeställningRepository repository = new BeställningRepository();

            // Inloggning
            System.out.print("Användarnamn: ");
            String username = scanner.nextLine();
            System.out.print("Lösenord: ");
            String password = scanner.nextLine();

            int kundId = repository.login(conn, username, password);

            if (kundId == -1) {
                System.out.println("Inloggning misslyckades");
                return;
            }

            // Hämta och visa alla tillgängliga skor från databasen
            System.out.println("Tillgängliga skor:");

            String query = "SELECT id, storlek, färg, pris, märke, skor_antal FROM Skor WHERE skor_antal > 0";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                List<Skor> skor = new ArrayList<>();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int storlek = rs.getInt("storlek");
                    String färg = rs.getString("färg");
                    double pris = rs.getDouble("pris");
                    String märke = rs.getString("märke");
                    int antal = rs.getInt("skor_antal");

                    skor.add(new Skor(id, storlek, färg, märke, antal));
                    System.out.println("ID: " + id + ", Storlek: " + storlek + ", Färg: " + färg +
                            ", Pris: " + pris + ", Märke: " + märke + ", Antal tillgängliga: " + antal);
                }

                System.out.print("Ange sko-ID att lägga till: ");
                int skoId = scanner.nextInt();
                scanner.nextLine(); // Rensa bufferten

                // Hämta och visa alla beställningar för kunden
                System.out.println("Dina beställningar:");

                // Först kontrollera om beställningen tillhör kunden
                String checkOrderQuery = "SELECT id, status FROM beställning WHERE id = ? AND kundid = ?";
                String orderQuery = "SELECT id, status FROM beställning WHERE kundid = ?";

                //hämta och Visa alla kundens beställningar
                try (PreparedStatement stmtOrder = conn.prepareStatement(orderQuery)) {
                    stmtOrder.setInt(1, kundId);

                    try (ResultSet rsOrder = stmtOrder.executeQuery()) {
                        List<OrderInfo> beställningar = new ArrayList<>();

                        while (rsOrder.next()) {
                            int id = rsOrder.getInt("id");
                            String status = rsOrder.getString("status");
                            beställningar.add(new OrderInfo(id, status));
                            System.out.println("ID: " + id + ", Status: " + status);
                        }

                        // Kontrollera eller skapa en ny beställning
                        System.out.print("Ange beställnings-ID (skriv 'null' för ny beställning): ");
                        String input = scanner.nextLine();

                        Integer orderId;
                        if (input.equalsIgnoreCase("null")) {
                            // Skapa direkt en ny beställning
                            System.out.println("Skapar en ny beställning...");
                            orderId = repository.createNewOrder(conn, kundId);
                            repository.addToCart(conn, kundId, orderId, skoId);
                            System.out.println("En ny beställning skapades och skon lades till!");
                            return;
                        } else {
                            try {
                                orderId = Integer.parseInt(input);
                            } catch (NumberFormatException e) {
                                System.out.println("Ogiltigt beställnings-ID format.");
                                return;
                            }
                        }

                        // Kontrollera om beställningen tillhör kunden och få status
                        try (PreparedStatement checkStmt = conn.prepareStatement(checkOrderQuery)) {
                            checkStmt.setInt(1, orderId);
                            checkStmt.setInt(2, kundId);

                            try (ResultSet checkRs = checkStmt.executeQuery()) {
                                if (checkRs.next()) {
                                    String status = checkRs.getString("status");

                                    if ("AKTIV".equalsIgnoreCase(status)) {
                                        repository.addToCart(conn, kundId, orderId, skoId);
                                        System.out.println("Sko tillagd till den aktiva beställningen!");
                                    } else if ("BETALD".equalsIgnoreCase(status)) {
                                        System.out.println("Fel: Beställningen är redan betald. Du kan inte lägga till fler produkter.");
                                    } else {
                                        System.out.println("Fel: Beställningen har en ogiltig status: " + status);
                                    }
                                } else {
                                    System.out.println("Fel: Kunde inte hitta beställningen ,skriv 'null' för att skapa ny beställning.");
                                }
                            }
                        }
                    }
                }
            }
        }
    }}