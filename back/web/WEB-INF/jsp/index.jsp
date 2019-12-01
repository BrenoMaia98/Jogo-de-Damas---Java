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
                            System.out.println(data);
                            System.out.println(horario);
                            System.out.println(jogador_1);
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
                    <th>Jogador</th>
                    <th>Posição</th>
                    <th>Pontuação</th>
                    <th>N° Vitórias</th>
                    <th>N° Empates</th>
                    <th>N° Derrotas</th>
                    <th>Saldo de damas</th>
                </tr>
                <tr>
                    <td>Breno</td>
                    <td>1</td>
                    <td>10</td>
                    <td>1</td>
                    <td>1</td>
                    <td>0</td>
                    <td>20</td>
                </tr>
                <tr>
                    <td>Ana</td>
                    <td>2</td>
                    <td>5</td>
                    <td>0</td>
                    <td>1</td>
                    <td>1</td>
                    <td>10</td>
                </tr>
            </table>
        </div>
    </body>
</html>