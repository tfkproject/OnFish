<?php
session_start();
if (empty($_SESSION)) {
	header("location:index.php"); // jika belum login, maka dikembalikan ke file form_login.php
}
else{
	include("koneksi.php");
?>
<!DOCTYPE html>
<html lang="en">

<?php
include("head.php");

$id_jenis_ikan = $_GET['id_jenis_ikan'];
?>

<body>

    <div id="wrapper">

	<?php include("nav.php"); ?>
	
	
	<div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Edit Data Jenis Ikan</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Form Edit Data
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-lg-12">
                                    <form role="form" action="jenis_edit_process.php" method="POST" enctype="multipart/form-data">
                                        <?php
        									$query = "select * from `jenis_ikan` where id_jenis_ikan = '$id_jenis_ikan'";
        									$eksekusi = mysqli_query($koneksi, $query);
        									while($row = mysqli_fetch_array($eksekusi)){
    									?>
    									<input name="id_jenis" value="<?php echo $row['id_jenis_ikan'];?>" class="form-control" type="hidden">
                                        <div class="form-group">
                                            <label>Nama Ikan</label>
                                            <input name="nama_ikan" class="form-control" value="<?php echo $row['nama_ikan'];?>" placeholder="contoh: Ikan Tenggiri">
                                        </div>
                                        
                                        <div class="form-group">
                                            <label>Foto</label>
											<small>Required for better view <strong style="color: red;">200px X 200px</strong>.</small>
											<input type="file" name="foto" onchange="readURL(this);">
                                        </div>
										<script type="text/javascript">
											function readURL(input) {
												if (input.files && input.files[0]) {
													var reader = new FileReader();

													reader.onload = function (e) {
														$('#blah')
														.attr('src', e.target.result)
														.width(200)
														.height(200);
													}

													reader.readAsDataURL(input.files[0]);
												}
											}
										</script>
										<input name="foto_sebelum" value="<?php echo $row['foto'];?>" class="form-control" type="hidden">
										<div class="form-group">
                                            <img id="blah" src="<?php echo $row['foto'];?>" alt="Preview disini" />
                                        </div>
										<?php
        								}
										?>
                                        <button name="submit" value="submit" type="submit" class="btn btn-default">Submit</button>
                                        <button type="reset" class="btn btn-default">Reset</button>
                                    </form>
                                </div>
                                <!-- /.col-lg-6 (nested) -->
                            </div>
                            <!-- /.row (nested) -->
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <?php
	include("script.php");
	?>

</body>

</html>
<?php
}
?>