<?php
include("koneksi.php");

if(isset($_GET)){
	$id = $_GET['id_dagangan'];
	
	$sql = "DELETE FROM `dagangan` WHERE `id_dagangan` = $id";
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