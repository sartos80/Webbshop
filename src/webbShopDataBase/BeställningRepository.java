package webbShopDataBase;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Calendar;

    public class BeställningRepository {
        public int login(Connection conn, String username, String password) throws SQLException {
            String query = "SELECT id FROM kund WHERE namn = ? AND lösenord = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
            return -1;
        }
        public String getOrderStatus(Connection conn, int orderId) throws SQLException {
            if (orderId < 0) {
                throw new IllegalArgumentException("Beställnings-ID får inte vara  negativt.");
            }

            String query = "SELECT status FROM beställning WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, orderId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("status"); // Returnera statusen
                    } else {
                        throw new SQLException("Ingen beställning hittades med det angivna ID:  " );

                    }
                }
            }
        }
        public OrderInfo getOrderInfo(Connection conn, int kundId, Integer beställningId) throws SQLException {
            String query = "SELECT id, status FROM beställning WHERE kundid = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, kundId);

                try (ResultSet rs = stmt.executeQuery()) {
                    boolean beställningHittad = false;

                    while (rs.next()) {
                        int currentOrderId = rs.getInt("id");
                        String status = rs.getString("status");

                        if (beställningId != null && beställningId == currentOrderId) {
                            beställningHittad = true;

                            if ("BETALD".equalsIgnoreCase(status)) {
                                throw new SQLException("Kunden kan inte lägga en produkt i en betald beställning.");
                            }

                            if ("AKTIV".equalsIgnoreCase(status)) {
                                return new OrderInfo(currentOrderId, status);
                            }
                        }
                    }
                    if (!beställningHittad || beställningId == null) {
                        int newOrderId = createNewOrder(conn, kundId);
                        return new OrderInfo(newOrderId, "AKTIV");
                    }

                    throw new SQLException("Ett fel uppstod med beställning ID: " + beställningId + ".");
                }
            }
        }

        public Integer createNewOrder(Connection conn, int kundId) throws SQLException {
            // för att införa en ny beställning
            String query = "INSERT INTO beställning (kundid, datum, status, pris) VALUES (?, NOW(), 'AKTIV', 0)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, kundId);

                int ändradeRader = stmt.executeUpdate();

                if (ändradeRader == 0) {
                    throw new SQLException("Kunde inte skapa en ny beställning.");
                }
            }
            String lastIdQuery = "SELECT LAST_INSERT_ID()";

            try (PreparedStatement stmt2 = conn.prepareStatement(lastIdQuery);
                 ResultSet skapatID = stmt2.executeQuery()) {

                if (skapatID.next()) {
                    return skapatID.getInt(1);  // Returnera det nya ID:t för beställningen
                } else {
                    throw new SQLException("Kunde inte hämta ID för den nya beställningen.");
                }
            }
        }

        public void addToCart(Connection conn, int kundId, Integer orderId, int productId) throws SQLException {
            conn.setAutoCommit(false); // Aktivera transaktioner

            try (CallableStatement stmt = conn.prepareCall("CALL AddToCart(?, ?,? )")) {
                stmt.setInt(1, kundId);

                // Om orderId är null, sätt det som null i proceduren
                if (orderId != null) {
                    stmt.setInt(2, orderId);
                } else {
                    stmt.setNull(2, Types.INTEGER);
                }

                stmt.setInt(3, productId);
                stmt.execute();

                conn.commit(); // Bekräfta transaktionen
            } catch (SQLException e) {
                conn.rollback(); // Avbryt transaktionen vid fel
                System.err.println("Ett fel inträffade: " + e.getMessage());
            }
        }
    }