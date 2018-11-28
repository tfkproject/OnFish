<?php
	include "koneksi.php";
	//error_log(print_r($_POST, 1));
    $id_transaksi = $_POST['id_transaksi'];
    $status = $_POST['status'];

	//select data ke database
	$q = "update `transaksi` set `status` = '$status' WHERE `id_transaksi` = '$id_transaksi'";
	$hasil = mysqli_query ($koneksi, $q);
    if ($hasil){
		$json_data = '{"success":1,"message":"Status berhasil diupadte"}';
        echo $json_data;
	}
	else{
		$json_data = '{"success":0,"message":"Maaf, terjadi kesalahan"}';
        echo $json_data;
	}
?>