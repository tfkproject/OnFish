<?php
	include "koneksi.php";
	//error_log(print_r($_POST, 1));
    $id_dagangan = $_POST['id_dagangan'];
    $id_pelanggan = $_POST['id_pelanggan'];
    $jum_kg = $_POST['jum_kg'];
    $harga_total = $_POST['harga_total'];

    /*
    buat request keranjang
    */
    $sql = "select * from keranjang where id_pelanggan = $id_pelanggan";
    $exs = mysqli_query($koneksi, $sql);
    $row = mysqli_fetch_array($exs);
    //jika keranjang tidak ada, insert baru
    if($row['id_keranjang'] == null){
        $sql2 = "insert into keranjang (`id_keranjang`, `id_pelanggan`) values (NULL, '$id_pelanggan')";
        $exs2 = mysqli_query($koneksi, $sql2);
        
        $id_keranjang = mysqli_insert_id($koneksi);
    }
    //jika ada, ambil id_keranjang
    else{
        $id_keranjang = $row['id_keranjang'];
    }
    

	//input data ke item_beli
	$q = "INSERT INTO `item_beli` (`id_item_beli`, `id_keranjang`, `id_dagangan`, `id_pelanggan`, `jum_kg`, `harga_total`, `waktu`) VALUES (NULL, '$id_keranjang', '$id_dagangan', '$id_pelanggan', '$jum_kg', '$harga_total',CURRENT_TIMESTAMP)";
	$hasil = mysqli_query ($koneksi, $q);
	$id_item_beli = mysqli_insert_id($koneksi);
	
	//masukkan juga ke record_item
	$q2 = "INSERT INTO `record_item` (`id_record_item`, `id_item_beli`, `id_keranjang`, `id_dagangan`, `id_pelanggan`, `jum_kg`, `harga_total`,`waktu`) VALUES (NULL, '$id_item_beli', '$id_keranjang', '$id_dagangan', '$id_pelanggan', '$jum_kg', '$harga_total', CURRENT_TIMESTAMP)";
	$hasil2 = mysqli_query ($koneksi, $q2);
    
    if ($hasil/* && $hasil2*/){
		echo '{"success":1,"message":"Berhasil ditambahkan ke keranjang."}';
	}
	else{
		echo '{"success":0,"message":"Maaf, terjadi kesalahan"}';
	}
?>