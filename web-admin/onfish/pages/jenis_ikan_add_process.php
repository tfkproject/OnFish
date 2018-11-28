<?php
include("koneksi.php");

if(isset($_POST)){
	$nama_ikan = $_POST['nama_ikan'];
	$file_foto = $_FILES['foto']['name'];
	$file_foto_temp = $_FILES['foto']['tmp_name'];
	
	$sql = "INSERT INTO `jenis_ikan` (`id_jenis_ikan`, `nama_ikan`, `foto`) VALUES (NULL, '$nama_ikan', '$file_foto')";
	move_uploaded_file($file_foto_temp, "../img/ikan_jenis/".$file_foto);
	
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
			alert('Data gagal diinput, mohon koreksi ulang.');
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