<?php
include("koneksi.php");

if(isset($_POST)){
	$id_dagangan = $_POST['id_dagangan'];

	$q = "SELECT foto_dagangan FROM dagangan WHERE `id_dagangan` = '$id_dagangan'";
	$x = mysqli_query($koneksi, $q);
	$r = mysqli_fetch_array($x);
	$filename = $r['foto_dagangan'];
	
	$sql = "DELETE FROM `dagangan` WHERE `id_dagangan` = '$id_dagangan'";

	$eksekusi = mysqli_query($koneksi, $sql);
	if ($eksekusi){
        //upload the image
        unlink("../ikan_dagangan/".$filename);
		
		echo '{"success":1,"message":"Data berhasil dihapus"}';
	}
	else{
		echo '{"success":0,"message":"Maaf, terjadi kesalahan"}';
	}
}
else{
	
}
?>