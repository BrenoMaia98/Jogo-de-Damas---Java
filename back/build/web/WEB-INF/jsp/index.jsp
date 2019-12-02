<%@page import="java.sql.Time"%>
<%@page import="java.sql.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="classes.Database"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Jogo de damas</title>
        
        <style>
            table{
                margin-top: 10px;
            }
            table, th, td{
                border: 1px solid black;
                text-align: center;
            }
            th{
                padding: 3px;
            }
        </style>
    </head>
    
    <body>
        <div style = "margin-bottom: 20px">
            <strong>Histórico</strong>
            <table>
                <tr>
                    <th>Data</th>
                    <th>Horário</th>
                    <th>Jogador 1</th>
                    <th>Jogador 2</th>
                    <th>Vencedor</th>
                    <th>Empate</th>
                </tr>
                <%
                    Database db = new Database("127.0.0.1", 3306, "root", "");
                    db.connect("jogodamas");
                    ResultSet res = db.obterHistorico();  
                        while (res.next()){
                            Date data = res.getDate("data");
                            Time horario = res.getTime("horario");
                            String jogador_1 = res.getString("jogador_1"),
                                   jogador_2 = res.getString("jogador_2"),
                                   vencedor = res.getString("vencedor"),
                                   empate = res.getString("empate");
                            %>
                            <tr>
                                <td><%= data %></td>
                                <td><%= horario %></td>
                                <td><%= jogador_1 %></td>
                                <td><%= jogador_2 %></td>
                                <td><%= vencedor %></td>
                                <td><%= empate %></td>
                            </tr>
                <%}%>
            </table>
        </div>
        <div>
            <strong>Classificação</strong>
            <table>
                <tr>
                    <th>Posição</th>
                    <th>Jogador</th>
                    <th>Pontuação</th>
                    <th>N° Vitórias</th>
                    <th>N° Empates</th>
                    <th>N° Derrotas</th>
                    <th>Saldo de damas</th>
                </tr>
                <%
                    db.connect("jogodamas");
                    res = db.obterClassificacao();
                        while (res.next()){
                            String nome_jogador = res.getString("nome_jogador");
                            int posicao = res.getInt("posicao"),
                                pontuacao = res.getInt("pontuacao"),
                                numero_vitorias = res.getInt("numero_vitorias"),
                                numero_empates = res.getInt("numero_empates"),
                                numero_derrotas = res.getInt("numero_derrotas"),
                                saldo_damas = res.getInt("saldo_damas");
                            %>
                            <tr>
                                <td><%= posicao %></td>
                                <td><%= nome_jogador %></td>
                                <td><%= pontuacao %></td>
                                <td><%= numero_vitorias %></td>
                                <td><%= numero_empates %></td>
                                <td><%= numero_derrotas %></td>
                                <td><%= saldo_damas %></td>
                            </tr>
                <%}%>
            </table>
        </div>
    </body>
</html>