<?php
	include "koneksi.php";
	//error_log(print_r($_POST, 1));
    $total_bayar = $_POST['total_bayar'];
    $id_keranjang = $_POST['id_keranjang'];
	$id_pelanggan = $_POST['id_pelanggan'];
	$jenis = $_POST['jenis'];
    
	//input data ke database
	$q = "INSERT INTO `transaksi` (`id_transaksi`, `id_keranjang`, `id_pelanggan`, `total_bayar`, `waktu`, `jenis`) VALUES (NULL, '$id_keranjang', '$id_pelanggan', '$total_bayar', CURRENT_TIMESTAMP, '$jenis')";
	$hasil = mysqli_query ($koneksi, $q);
	$id_transaksi = mysqli_insert_id($koneksi);
	
    
    if ($hasil){
        /*
    	- Delete tabel item_beli dan delete keranjang where id_keranjang
    	*/
        $q2 = "DELETE FROM item_beli WHERE id_keranjang = $id_keranjang";
	    $hasil2 = mysqli_query ($koneksi, $q2);
	    
	    $q3 = "DELETE FROM keranjang WHERE id_keranjang = $id_keranjang";
	    $hasil2 = mysqli_query ($koneksi, $q3);
		echo '{"success":1,"message":"Invoice berahasil di buat","id_transaksi":'.$id_transaksi.'}';
	}
	else{
		echo '{"success":0,"message":"Maaf, terjadi kesalahan"}';
	}
	
?>