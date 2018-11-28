<?php
include("koneksi.php");

if(isset($_GET)){
	$id = $_GET['id_pelanggan'];
	
	$sql = "DELETE FROM `pelanggan` WHERE `id_pelanggan` = $id";
	$eksekusi = mysqli_query($koneksi, $sql);
	if($eksekusi){
		?>
		<script>
			window.location = "pelanggan.php";
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