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

$id_dagangan = $_GET['id_dagangan'];
?>

<body>

    <div id="wrapper">

	<?php include("nav.php"); ?>
	
	
	<div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Input Data Kategori Ikan</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Form Input Data
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-lg-12">
                                    <form role="form" action="dagangan_edit_process.php" method="POST" enctype="multipart/form-data">
                                        <?php
        									$query = "select * from `dagangan` where id_dagangan = '$id_dagangan'";
        									$eksekusi = mysqli_query($koneksi, $query);
        									while($row = mysqli_fetch_array($eksekusi)){
    									?>
    									<input name="id_dagangan" value="<?php echo $row['id_dagangan'];?>" class="form-control" type="hidden">
                                        <div class="form-group">
                                            <label>Pilih Kategori Ikan</label>
                                            <select name="id_kategori_ikan" class="form-control">
												<?php
												$id_kategori_ikan = $row['id_kategori_ikan'];
												$query = "select * from `kategori_ikan` where id_kategori_ikan = '$id_kategori_ikan'";
												$eksekusi = mysqli_query($koneksi, $query);
												$row_kat = mysqli_fetch_array($eksekusi);
												?>
												<option value="<?php echo $id_kategori_ikan?>"><?php echo $row_kat['nama_ikan'];?></option>
											<?php
											$query = "select * from `kategori_ikan`";
											$eksekusi = mysqli_query($koneksi, $query);
											while($row_kat = mysqli_fetch_array($eksekusi)){
											?>
												<option value="<?php echo $row_kat['id_kategori_ikan'];?>"><?php echo $row_kat['nama_ikan'];?></option>
											<?php
											}
											?>
											</select>
                                        </div>
                                        
                                        <div class="form-group">
                                            <label>Pilih Petani</label>
                                            <select name="id_petani" class="form-control">
												<?php
												$id_petani = $row['id_petani'];
												$query = "select * from `petani` where id_petani = '$id_petani'";
												$eksekusi = mysqli_query($koneksi, $query);
												$row_pet = mysqli_fetch_array($eksekusi);
												?>
												<option value="<?php echo $id_petani?>"><?php echo $row_pet['nama_petani'];?></option>
											<?php
											$query = "select * from `petani`";
											$eksekusi = mysqli_query($koneksi, $query);
											while($row_pet = mysqli_fetch_array($eksekusi)){
											?>
												<option value="<?php echo $row_pet['id_petani'];?>"><?php echo $row_pet['nama_petani'];?></option>
											<?php
											}
											?>
											</select>
                                        </div>
                                        
                                        <div class="form-group">
											<label>Harga Per Kg</label>
											<div class="form-group input-group">
												<span class="input-group-addon">Rp</span>														
												<input type="number" name="harga_per_kg" value="<?php echo $row['harga_per_kg'];?>" class="form-control">
											</div>
										</div>
                                        
                                        <div class="form-group">
                                            <label>Foto</label>
											<small>Required for better view <strong style="color: red;">200px X 200px</strong>.</small>
											<input type="file" name="foto" onchange="readURL(this);" >
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
										<input name="foto_sebelum" value="<?php echo $row['foto_dagangan'];?>" class="form-control" type="hidden">
										<div class="form-group">
                                            <img id="blah" src="<?php echo $row['foto_dagangan'];?>" alt="Preview disini" />
                                        </div>
										<div class="form-group">
											<label>Deskripsi</label>
											<div class="form-group">														
												<textarea name="deskripsi" class="form-control" placeholder="Deskripsi dagangan"><?php echo $row['deskripsi'];?></textarea>
											</div>
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