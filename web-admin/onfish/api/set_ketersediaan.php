<?php
	include "koneksi.php";
	//error_log(print_r($_POST, 1));
    $id_dagangan = $_POST['id_dagangan'];
    $jum_kg = $_POST['jum_kg'];
    
	//input data ke item_beli
	$q = "UPDATE `dagangan` SET berat_tersedia = '$jum_kg' WHERE id_dagangan = '$id_dagangan'";
	$hasil = mysqli_query ($koneksi, $q);
    
    if ($hasil){
		echo '{"success":1,"message":""}';
	}
	else{
		echo '{"success":0,"message":"Maaf, terjadi kesalahan"}';
	}
?>