<?php
error_reporting(E_ERROR | E_PARSE); // To hide any notice/warning messages
include("db_config.php");
$response = array();

if (isset($_POST['id']) && isset($_POST['name']) && isset($_POST['address'])) {
    $id = $_POST['id'];
    $name = $_POST['name'];
    $address = $_POST['address'];
    $stmt = $db->prepare("UPDATE info SET name=?, address=? WHERE id=?");
    $stmt->bind_param("ssi", $name, $address, $id);
    $stmt->execute();
    $row_count = $stmt->affected_rows;
    if ($row_count > 0) {
        $response["success"] = 1;
        $response["message"] = "Updated Successfully.";
    } else {
        $response["success"] = 0;
        $response["message"] = "Failed To Update.";
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Failed To Update.";
}
echo json_encode($response);

$db->close();
?>
