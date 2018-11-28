<?php
include("koneksi.php");

if(isset($_GET)){
	$id = $_GET['id_jenis_ikan'];
	
	$sql = "DELETE FROM `jenis_ikan` WHERE `id_jenis_ikan` = $id";
	$eksekusi = mysqli_query($koneksi, $sql);
	if($eksekusi){
		?>
		<script>
			window.location = "jenis.php";
		</script>
		<?php
	}
	else{
		?>
		<script>
			alert('Data gagal dihapus');
			history.back(-1);
		</script>
		<?php
	}
}
else{
	?>
	<script>
		alert('Maaf, terjadi error.');
		history.back(-1);
	</script>
	<?php
}
?>