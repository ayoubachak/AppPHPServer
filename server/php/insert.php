<?php
error_reporting(E_ERROR | E_PARSE); // To hide any notice/warning messages
include("db_config.php");
$response = array();

if (!empty($_POST['name'])) {
    $name = $_POST['name'];
    $address = $_POST['address'];
    $stmt = $db->prepare("INSERT INTO info (name, address) VALUES (?, ?)");
    $stmt->bind_param("ss", $name, $address);
    if ($stmt->execute()) {
        $response["success"] = 1;
    } else {
        $response["success"] = 0;
    }
}else {
    $response["success"] = 0;
}
echo json_encode($response);



$db->close();
?>
