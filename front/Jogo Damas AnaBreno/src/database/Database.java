package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

public class Database {

    private Connection connection;
    private String user;
    private String password;
    private String server;
    private int numPort;
    private Statement stmt;

    public Database(String server, int numPort, String user, String password) throws SQLException {
        this.server = server;
        this.numPort = numPort;
        this.user = user;
        this.password = password;
    }

    public void connect(String database) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://" + server + ":" + numPort + "/" + database + "?useTimezone=true&serverTimezone=UTC";
        this.connection = DriverManager.getConnection(url, user, password);
        this.stmt = this.get();
    }

    public Statement get() throws SQLException {
        return this.connection.createStatement();
    }

    public void inserirRegistroPartida(Date data, Time horario, String jogador_1, String jogador_2, String vencedor, boolean empate) throws SQLException {
        String sql = "INSERT INTO `historico` (`data`, `horario`, `jogador_1`, `jogador_2`, `vencedor`, `empate`)"
                + "VALUES ('" + data + "', '" + horario + "', '" + jogador_1 + "', '" + jogador_2 + "', '" + vencedor + "'," + empate + ")";
        stmt.executeUpdate(sql);
    }

    public ResultSet obterHistorico() throws SQLException {
        String sql = "SELECT * FROM historico";
        ResultSet res = stmt.executeQuery(sql);
        return res;
    }

    public void close() throws SQLException {
        connection.close();
    }
}
