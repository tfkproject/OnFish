<?php
include("koneksi.php");

if(isset($_POST)){
	$id_jenis = $_POST['id_jenis'];
	$nama_ikan = $_POST['nama_ikan'];
	
	$foto_sebelum = $_POST['foto_sebelum'];
	$file_foto = $_FILES['foto']['name'];
	$file_foto_temp = $_FILES['foto']['tmp_name'];
	
	if($file_foto == null){
		$foto = $foto_sebelum;
	}
	else{
		$foto = $file_foto;
		move_uploaded_file($file_foto_temp, "../img/ikan_jenis/".$file_foto);
	}
	
	$sql = "UPDATE `jenis_ikan` SET `nama_ikan` = '$nama_ikan', `foto` = '$foto' WHERE `id_jenis_ikan` = '$id_jenis' ";
	$eksekusi = mysqli_query($koneksi, $sql);
	if($eksekusi){
		?>
		<script>
			window.location = "jenis_ikan.php";
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