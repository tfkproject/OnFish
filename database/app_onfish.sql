-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Nov 28, 2018 at 09:26 AM
-- Server version: 10.0.34-MariaDB-0ubuntu0.16.04.1
-- PHP Version: 7.0.30-1+ubuntu16.04.1+deb.sury.org+1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `app_onfish`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id_admin` int(11) NOT NULL,
  `nama` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(8) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id_admin`, `nama`, `email`, `password`) VALUES
(1, 'Administrator', 'admin@admin.com', '123');

-- --------------------------------------------------------

--
-- Table structure for table `dagangan`
--

CREATE TABLE `dagangan` (
  `id_dagangan` int(11) NOT NULL,
  `id_penjual` int(11) NOT NULL,
  `id_jenis_ikan` int(11) NOT NULL,
  `berat_tersedia` int(11) NOT NULL,
  `harga_per_kg` int(11) NOT NULL,
  `foto_dagangan` text COLLATE utf8_unicode_ci NOT NULL,
  `deskripsi` text COLLATE utf8_unicode_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `dagangan`
--

INSERT INTO `dagangan` (`id_dagangan`, `id_penjual`, `id_jenis_ikan`, `berat_tersedia`, `harga_per_kg`, `foto_dagangan`, `deskripsi`) VALUES
(1, 1, 1, 20, 25000, 'ikan_tenggiri1.jpg', 'Ikan tenggiri segar, harga murah saja. Siap di antar'),
(2, 2, 1, 5, 26000, 'ikan_tenggiri2.jpg', 'sdfsdfwer werwrewkjsdkfs sdfsdf'),
(5, 5, 5, 12, 45000, 'cumi-fresh-segar.jpg', 'sdffdfdd'),
(4, 4, 4, 8, 45000, 'ikan parang.jpg', 'erstsertgsdfgfd sdgfd gs'),
(12, 1, 1, 9, 4000, 'HSL.jpeg', 'test'),
(15, 7, 2, 13, 222, 'IMG_null.jpg', 'jsjavaushsv\nsksjehdudkekdkdjd jdjdhe\njejsjen'),
(16, 8, 6, 14, 55000, 'IMG_1536114149.jpg', 'ikan tongkol segar, tersedia tidak banyak'),
(17, 10, 1, 2, 25000, 'IMG_1537266475.jpg', 'Produk pertama'),
(18, 12, 1, 1, 20000, 'IMG_1537269700.jpg', 'ikan tenggiri kembar'),
(19, 8, 5, 15, 15000, 'IMG_1537369151.jpg', 'baru dan segar');

-- --------------------------------------------------------

--
-- Table structure for table `item_beli`
--

CREATE TABLE `item_beli` (
  `id_item_beli` int(11) NOT NULL,
  `id_keranjang` int(11) NOT NULL,
  `id_dagangan` int(11) NOT NULL,
  `id_pelanggan` int(11) NOT NULL,
  `jum_kg` int(11) NOT NULL,
  `harga_total` int(11) NOT NULL,
  `waktu` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `item_beli`
--

INSERT INTO `item_beli` (`id_item_beli`, `id_keranjang`, `id_dagangan`, `id_pelanggan`, `jum_kg`, `harga_total`, `waktu`) VALUES
(8, 6, 4, 5, 18, 810000, '2018-07-29 00:30:29'),
(5, 3, 5, 4, 1, 45000, '2018-07-16 08:55:52'),
(4, 3, 1, 4, 2, 50000, '2018-07-16 08:55:19'),
(36, 18, 17, 12, 1, 25000, '2018-09-18 11:22:52'),
(32, 16, 16, 3, 1, 55000, '2018-09-05 14:15:15'),
(25, 15, 4, 9, 1, 45000, '2018-08-16 03:10:10'),
(21, 13, 1, 7, 1, 25000, '2018-08-13 00:25:39'),
(35, 18, 18, 12, 1, 20000, '2018-09-18 11:22:17'),
(37, 18, 2, 12, 1, 26000, '2018-09-18 11:22:59');

-- --------------------------------------------------------

--
-- Table structure for table `jenis_ikan`
--

CREATE TABLE `jenis_ikan` (
  `id_jenis_ikan` int(11) NOT NULL,
  `nama_ikan` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `foto` text COLLATE utf8_unicode_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `jenis_ikan`
--

INSERT INTO `jenis_ikan` (`id_jenis_ikan`, `nama_ikan`, `foto`) VALUES
(1, 'Ikan Tenggiri', 'ikan_tenggiri.jpg'),
(7, 'Ikan Biang', 'IMG-20180919-WA0006.jpg'),
(3, 'Ikan Kembung', 'ikan_kembung.jpg'),
(4, 'Ikan Parang', 'ikan parang.jpg'),
(8, 'Kerang', 'IMG-20180919-WA0005.jpg'),
(5, 'Cumi - cumi', '060102000_1431503872-cumi.png'),
(6, 'Ikan tongkol', 'images.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `keranjang`
--

CREATE TABLE `keranjang` (
  `id_keranjang` int(11) NOT NULL,
  `id_pelanggan` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `keranjang`
--

INSERT INTO `keranjang` (`id_keranjang`, `id_pelanggan`) VALUES
(6, 5),
(3, 4),
(16, 3),
(18, 12),
(13, 7),
(15, 9);

-- --------------------------------------------------------

--
-- Table structure for table `pelanggan`
--

CREATE TABLE `pelanggan` (
  `id_pelanggan` int(11) NOT NULL,
  `nama_pelanggan` text COLLATE utf8_unicode_ci NOT NULL,
  `email` text COLLATE utf8_unicode_ci NOT NULL,
  `password` text COLLATE utf8_unicode_ci NOT NULL,
  `nomor_hp` text COLLATE utf8_unicode_ci NOT NULL,
  `alamat` text COLLATE utf8_unicode_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `pelanggan`
--

INSERT INTO `pelanggan` (`id_pelanggan`, `nama_pelanggan`, `email`, `password`, `nomor_hp`, `alamat`) VALUES
(5, 'pratiwi', 'ptiwipratiwii@gmail.com', '123456', '081277990807', 'jln antara'),
(3, 'tiwi', 'tiwi@gmail.com', 'tiwi123', '08126819572', 'Jl Antara'),
(7, 'luthfi', 'luthfi@gmail.com', '123lutfi', '081255447894', 'Bengkalis'),
(8, 'tes', 'tes@mail.com', '123456', '08212', 'vzh'),
(9, 'indah', 'indahkasih34@gmail.com', '12345678', '082285417213', 'jl. wobar'),
(10, 'mamas', 'mamas@gmail.com', 'mamas2002', '082170001033', 'selatbaru');

-- --------------------------------------------------------

--
-- Table structure for table `penjual`
--

CREATE TABLE `penjual` (
  `id_penjual` int(11) NOT NULL,
  `nama_penjual` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `email` text COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `no_hp` text COLLATE utf8_unicode_ci NOT NULL,
  `alamat` text COLLATE utf8_unicode_ci NOT NULL,
  `lat` text COLLATE utf8_unicode_ci NOT NULL,
  `lon` text COLLATE utf8_unicode_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `penjual`
--

INSERT INTO `penjual` (`id_penjual`, `nama_penjual`, `email`, `password`, `no_hp`, `alamat`, `lat`, `lon`) VALUES
(1, 'Pratiwi', 'pratiwi@mail.com', 'pratiwi123', '6281277990807', 'Jl. Sekian sekian sekian', '1.6619025000000038', '101.44794921874998'),
(2, 'Bunga', '', '', '6282288449596', '', '', ''),
(3, 'Nirma', '', '', '6281277990807', '', '', ''),
(4, 'kamaruddin', '', '', '6281277990807', '', '', ''),
(5, 'luthfi', '', '', '6282386202297', '', '', ''),
(6, 'Joko', '', '', '6281277990807', '', '', ''),
(7, 'Lapak tes', 'lapak@mail.com', 'lapak123', '081234567890', 'Jl. Lapak, Gg. Lapak', '1.6619025000000038', '101.44794921874998'),
(8, 'pratiwi', 'tiwi1@gmail.com', 'tiwi123', '081277990807', 'jl antara gg cendana', '1.4796551', '102.11183419999999'),
(9, 'mr.toy', 'mrty@gmail.com', 'mr12345', '082391116659', 'wonosari tengah', '1.4900835', '102.11911479999999'),
(10, 'Ooz', 'ooz@dumai.com', 'rahasia', '1234567890', 'Dumai', '1.660752500000008', '101.44823828125001'),
(11, 'ooz', 'ooz.aja@gmail.com', 'qwerty1#34', '081371449682', 'jl sudirman', '1.6613862000000001', '101.44410839999999'),
(12, 'ooz aja', 'ooz@gmail.com', '123123', '081371449682', 'dumai', '1.6613862000000001', '101.44410839999999'),
(13, 'pratiwi', 'Pratiwi@gmail.com', '123tiwi', '081277990807', 'Jl. Kelapapati Darat', '1.4832995', '102.09614099999999');

-- --------------------------------------------------------

--
-- Table structure for table `record_item`
--

CREATE TABLE `record_item` (
  `id_record_item` int(11) NOT NULL,
  `id_item_beli` int(11) NOT NULL,
  `id_keranjang` int(11) NOT NULL,
  `id_dagangan` int(11) NOT NULL,
  `id_pelanggan` int(11) NOT NULL,
  `jum_kg` int(11) NOT NULL,
  `harga_total` int(11) NOT NULL,
  `waktu` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `record_item`
--

INSERT INTO `record_item` (`id_record_item`, `id_item_beli`, `id_keranjang`, `id_dagangan`, `id_pelanggan`, `jum_kg`, `harga_total`, `waktu`) VALUES
(1, 1, 1, 1, 1, 1, 25000, '2018-07-16 06:31:15'),
(2, 2, 2, 1, 3, 2, 50000, '2018-07-16 08:10:48'),
(3, 3, 2, 1, 3, 1, 25000, '2018-07-16 08:16:28'),
(4, 4, 3, 1, 4, 2, 50000, '2018-07-16 08:55:19'),
(5, 5, 3, 5, 4, 1, 45000, '2018-07-16 08:55:52'),
(6, 6, 4, 1, 5, 1, 25000, '2018-07-27 07:16:09'),
(7, 7, 5, 5, 5, 2, 90000, '2018-07-28 04:47:05'),
(8, 8, 6, 4, 5, 18, 810000, '2018-07-29 00:30:29'),
(9, 9, 7, 1, 3, 1, 25000, '2018-07-30 10:10:23'),
(10, 10, 7, 2, 3, 1, 26000, '2018-07-30 10:19:37'),
(11, 11, 7, 5, 3, 1, 45000, '2018-07-30 10:23:43'),
(24, 24, 14, 1, 9, 1, 25000, '2018-08-16 03:08:19'),
(13, 13, 8, 1, 3, 1, 25000, '2018-07-31 09:34:08'),
(14, 14, 8, 1, 3, 1, 25000, '2018-07-31 09:34:38'),
(15, 15, 9, 5, 6, 3, 135000, '2018-08-01 04:32:53'),
(16, 16, 9, 12, 6, 5, 20000, '2018-08-01 04:33:24'),
(17, 17, 10, 1, 6, 1, 25000, '2018-08-01 04:35:07'),
(18, 18, 11, 2, 6, 1, 26000, '2018-08-01 04:39:33'),
(23, 23, 14, 1, 9, 2, 50000, '2018-08-14 06:35:40'),
(20, 20, 12, 2, 1, 1, 26000, '2018-08-06 01:53:59'),
(21, 21, 13, 1, 7, 1, 25000, '2018-08-13 00:25:39'),
(22, 22, 8, 1, 3, 1, 25000, '2018-08-13 10:27:37'),
(25, 25, 15, 4, 9, 1, 45000, '2018-08-16 03:10:10'),
(26, 26, 8, 1, 3, 1, 25000, '2018-08-19 06:05:46'),
(34, 34, 17, 17, 8, 2, 50000, '2018-09-18 10:31:48'),
(30, 30, 12, 1, 1, 1, 25000, '2018-08-31 14:33:56'),
(32, 32, 16, 16, 3, 1, 55000, '2018-09-05 14:15:15'),
(35, 35, 18, 18, 12, 1, 20000, '2018-09-18 11:22:17'),
(36, 36, 18, 17, 12, 1, 25000, '2018-09-18 11:22:52'),
(37, 37, 18, 2, 12, 1, 26000, '2018-09-18 11:22:59');

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `id_transaksi` int(11) NOT NULL,
  `id_keranjang` int(11) NOT NULL,
  `id_pelanggan` int(11) NOT NULL,
  `total_bayar` int(11) NOT NULL,
  `waktu` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `jenis` varchar(1) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `transaksi`
--

INSERT INTO `transaksi` (`id_transaksi`, `id_keranjang`, `id_pelanggan`, `total_bayar`, `waktu`, `jenis`) VALUES
(1, 2, 3, 75000, '2018-07-16 08:41:36', 'A'),
(2, 1, 1, 25000, '2018-07-22 18:52:39', 'J'),
(3, 4, 5, 25000, '2018-07-27 07:16:18', 'A'),
(4, 5, 5, 90000, '2018-07-28 04:47:28', 'A'),
(5, 7, 3, 96000, '2018-07-30 10:27:15', ''),
(6, 9, 6, 155000, '2018-08-01 04:33:47', 'A'),
(7, 10, 6, 25000, '2018-08-01 04:35:26', 'J'),
(8, 11, 6, 26000, '2018-08-01 04:39:39', 'A'),
(9, 14, 9, 75000, '2018-08-16 03:09:01', ''),
(10, 8, 3, 100000, '2018-08-19 06:06:01', 'A'),
(11, 12, 1, 25000, '2018-08-31 14:34:05', 'A'),
(12, 17, 8, 50000, '2018-09-18 10:32:07', 'J');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id_admin`);

--
-- Indexes for table `dagangan`
--
ALTER TABLE `dagangan`
  ADD PRIMARY KEY (`id_dagangan`);

--
-- Indexes for table `item_beli`
--
ALTER TABLE `item_beli`
  ADD PRIMARY KEY (`id_item_beli`);

--
-- Indexes for table `jenis_ikan`
--
ALTER TABLE `jenis_ikan`
  ADD PRIMARY KEY (`id_jenis_ikan`);

--
-- Indexes for table `keranjang`
--
ALTER TABLE `keranjang`
  ADD PRIMARY KEY (`id_keranjang`);

--
-- Indexes for table `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD PRIMARY KEY (`id_pelanggan`);

--
-- Indexes for table `penjual`
--
ALTER TABLE `penjual`
  ADD PRIMARY KEY (`id_penjual`);

--
-- Indexes for table `record_item`
--
ALTER TABLE `record_item`
  ADD PRIMARY KEY (`id_record_item`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id_transaksi`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id_admin` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `dagangan`
--
ALTER TABLE `dagangan`
  MODIFY `id_dagangan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `item_beli`
--
ALTER TABLE `item_beli`
  MODIFY `id_item_beli` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- AUTO_INCREMENT for table `jenis_ikan`
--
ALTER TABLE `jenis_ikan`
  MODIFY `id_jenis_ikan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `keranjang`
--
ALTER TABLE `keranjang`
  MODIFY `id_keranjang` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `pelanggan`
--
ALTER TABLE `pelanggan`
  MODIFY `id_pelanggan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `penjual`
--
ALTER TABLE `penjual`
  MODIFY `id_penjual` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `record_item`
--
ALTER TABLE `record_item`
  MODIFY `id_record_item` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- AUTO_INCREMENT for table `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `id_transaksi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
