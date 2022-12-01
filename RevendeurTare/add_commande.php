<?php
require_once("Commande.php");

//crée l'objet à partir des informations du formulaire
$objet = new Commande($_POST["type"],$_POST["qte"]);

//encode l'objet en json et l'affiche
$objetSerialized = json_encode($objet);
echo $objetSerialized."\n";