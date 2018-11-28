<?php
	include "koneksi.php";
	//error_log(print_r($_POST, 1));
    $id_item_beli = $_POST['id_item_beli'];
	
	//input data ke database
	$q = "DELETE FROM `item_beli` WHERE `id_item_beli` = '$id_item_beli'";
	$hasil = mysqli_query ($koneksi, $q);
	
	$q2 = "DELETE FROM `record_item` WHERE `id_item_beli` = '$id_item_beli'";
	$hasil2 = mysqli_query ($koneksi, $q2);
    if ($hasil && $hasil2){
		echo '{"success":1,"message":"berhasil dihapus dari keranjang."}';
	}
	else{
		echo '{"success":0,"message":"Maaf, terjadi kesalahan"}';
	}
?>