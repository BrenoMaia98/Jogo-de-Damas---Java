package classes;

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
        
        public void inserirRegistroPartida(Date data, Time horario, String jogador_1, String jogador_2, String vencedor, boolean empate) throws SQLException{
                String sql = "INSERT INTO `historico` (`data`, `horario`, `jogador_1`, `jogador_2`, `vencedor`, `empate`)"
				+ "VALUES ('"+data+"', '"+horario+"', '"+jogador_1+"', '"+jogador_2+"', '"+vencedor+"',"+empate+")";
		stmt.executeUpdate(sql);
        }
        
        public void inserirPontuacaoJogador(String nome_jogador, int saldo_damas, String situacao) throws SQLException{
                boolean jogadorJaCadastrado = false;
                int pontuacao = 0, numero_vitorias = 0, numero_empates = 0, numero_derrotas = 0;
                switch(situacao){
                    case "vitoria":
                        pontuacao+=10;
                        numero_vitorias++;
                        break;
                    case "derrota":
                        pontuacao-=10;
                        numero_derrotas++;
                        break;
                    case "empate":
                        numero_empates++;
                        break;
                }
                String sql = "SELECT * FROM `classificacao` WHERE nome_jogador='"+nome_jogador+"'";
                ResultSet res = stmt.executeQuery(sql);
               
                while (res.next()){
                    jogadorJaCadastrado = true;
                    pontuacao = res.getInt("pontuacao")+pontuacao;
                    numero_vitorias = res.getInt("numero_vitorias")+numero_vitorias;
                    numero_empates = res.getInt("numero_empates")+numero_empates;
                    numero_derrotas = res.getInt("numero_derrotas")+numero_derrotas;
                    saldo_damas = res.getInt("saldo_damas")+saldo_damas;
                }
  
                if(!jogadorJaCadastrado){
                    sql = "INSERT INTO `classificacao` (`nome_jogador`, `pontuacao`, `numero_vitorias`, `numero_empates`, `numero_derrotas`, `saldo_damas`)"
				+ "VALUES ('"+nome_jogador+"',"+pontuacao+", "+numero_vitorias+", "+numero_empates+","+numero_derrotas+","+saldo_damas+")";
                    stmt.executeUpdate(sql);
                }
                else{
                    updatePontuacaoJogador(nome_jogador, pontuacao, numero_vitorias, numero_empates, numero_derrotas, saldo_damas);
                }
                
        }
        
        public void updatePontuacaoJogador(String nome_jogador, int pontuacao, int numero_vitorias, int numero_empates, int numero_derrotas, int saldo_damas) throws SQLException{          
                String sql = "UPDATE `classificacao` SET pontuacao="+pontuacao+",numero_vitorias="+numero_vitorias+
                        ",numero_empates="+numero_empates+",numero_derrotas="+numero_derrotas+",saldo_damas="+saldo_damas+" WHERE nome_jogador='"+nome_jogador+"'";
		stmt.executeUpdate(sql);
        }
        
        public ResultSet obterHistorico() throws SQLException{
                String sql = "SELECT * FROM historico";
		ResultSet res = stmt.executeQuery(sql);
                return res;
        }
        
        public ResultSet obterClassificacao() throws SQLException{
            stmt.executeQuery("SET @row_number := 0;");
                String sql = "SELECT " +
                            "(@row_number:=@row_number + 1) AS posicao, nome_jogador, pontuacao, "+
                            "numero_vitorias, numero_empates, numero_derrotas, saldo_damas FROM classificacao ORDER BY pontuacao desc";
		ResultSet res = stmt.executeQuery(sql);
                return res;
        }
	
	
	public void close() throws SQLException {
		connection.close();
	}
}
