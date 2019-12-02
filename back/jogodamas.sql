-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 02-Dez-2019 às 03:42
-- Versão do servidor: 10.1.35-MariaDB
-- versão do PHP: 7.2.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `jogodamas`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `classificacao`
--

CREATE TABLE `classificacao` (
  `nome_jogador` varchar(50) NOT NULL,
  `pontuacao` int(11) NOT NULL,
  `numero_vitorias` int(11) NOT NULL,
  `numero_empates` int(11) NOT NULL,
  `numero_derrotas` int(11) NOT NULL,
  `saldo_damas` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `classificacao`
--

INSERT INTO `classificacao` (`nome_jogador`, `pontuacao`, `numero_vitorias`, `numero_empates`, `numero_derrotas`, `saldo_damas`) VALUES
('Freeza', 10, 1, 0, 0, 5),
('Goku', 40, 5, 0, 1, 18),
('Yamcha', -50, 0, 0, 5, -23);

-- --------------------------------------------------------

--
-- Estrutura da tabela `historico`
--

CREATE TABLE `historico` (
  `id` int(4) NOT NULL,
  `data` date NOT NULL,
  `horario` time NOT NULL,
  `jogador_1` varchar(50) NOT NULL,
  `jogador_2` varchar(50) NOT NULL,
  `vencedor` varchar(50) NOT NULL,
  `empate` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `historico`
--

INSERT INTO `historico` (`id`, `data`, `horario`, `jogador_1`, `jogador_2`, `vencedor`, `empate`) VALUES
(2, '2019-12-02', '00:02:03', 'Goku', 'Freeza', 'Freeza', '0'),
(3, '2019-12-03', '08:08:06', 'Goku', 'Yamcha', 'Goku', '0'),
(4, '2019-12-04', '16:21:08', 'Goku', 'Yamcha', 'Goku', '0'),
(5, '2019-12-05', '19:20:37', 'Goku', 'Yamcha', 'Goku', '0'),
(6, '2019-12-06', '19:09:39', 'Goku', 'Yamcha', 'Goku', '0'),
(7, '2019-12-07', '22:09:40', 'Goku', 'Yamcha', 'Goku', '0');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `classificacao`
--
ALTER TABLE `classificacao`
  ADD PRIMARY KEY (`nome_jogador`);

--
-- Indexes for table `historico`
--
ALTER TABLE `historico`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `historico`
--
ALTER TABLE `historico`
  MODIFY `id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
