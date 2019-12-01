package classes;

import classes.Database;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;

public class Main {

	public static void main(String[] args) throws SQLException, ParseException, ClassNotFoundException {

		Database db = new Database("127.0.0.1", 3306, "root", "");
                db.connect("jogodamas");
		try {
//                        LocalDate data = LocalDate.now();;
//                        LocalTime time = LocalTime.now();
//                        db.inserirRegistroPartida(Date.valueOf(data), Time.valueOf(time) , "Ana", "Breno", "Ana", false);
                        ResultSet res = db.obterHistorico();  
                        while (res.next()){
                            Date data = res.getDate("data");
                            Time horario = res.getTime("horario");
                            String jogador_1 = res.getString("jogador_1"),
                                   jogador_2 = res.getString("jogador_2"),
                                   vencedor = res.getString("vencedor"),
                                   empate = res.getString("empate");
                            System.out.println (" ROW = " + data + ": " + horario + ": " + jogador_1 + ": " + jogador_2 + ": "+ vencedor + ": "+ empate ) ;
                        }
                          
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
