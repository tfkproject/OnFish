<?php
include("koneksi.php");

if(isset($_POST)){
	$id_dagangan = $_POST['id_dagangan'];
	$id_petani = $_POST['id_petani'];
	$id_kategori = $_POST['id_kategori_ikan'];
	$harga_per_kg = $_POST['harga_per_kg'];
	$deskripsi = $_POST['deskripsi'];
	
	$foto_sebelum = $_POST['foto_sebelum'];
	$file_foto = $_FILES['foto']['name'];
	$file_foto_temp = $_FILES['foto']['tmp_name'];
	
	if($file_foto == null){
		$foto = $foto_sebelum;
	}
	else{
		$foto = "http://203.153.21.11/app/onfish/img/ikan_dagangan/".$file_foto."";
		move_uploaded_file($file_foto_temp, "../img/ikan_dagangan/".$file_foto);
	}
	
	$sql = "UPDATE `dagangan` SET `id_petani` = '$id_petani', `id_kategori_ikan` = '$id_kategori', `harga_per_kg` = '$harga_per_kg', `foto_dagangan` = '$foto', `deskripsi` = '$deskripsi' WHERE `id_dagangan` = '$id_dagangan' ";
	$eksekusi = mysqli_query($koneksi, $sql);
	if($eksekusi){
		?>
		<script>
			window.location = "dagangan.php";
		</script>
		<?php
	}
	else{
		?>
		<script>
			alert('Data gagal diupdate, mohon koreksi ulang.');
			history.back(-1);
		</script>
		<?php
	}
}
else{
	?>
	<script>
		alert('Mohon isi semua field');
		history.back(-1);
	</script>
	<?php
}
?>