<h1>Compilateur RPC</h1>
<h2>Conception</h2>
<p>
Le fichier "Compilo.java" du package rpc va s'appuyer sur l'interface qu'on lui indique en argument pour en tirer les classes et méthodes à compiler.
Cette analyse doit être faite avec des expressions régulières. Pour le moment, le compilateur ne fait pas cette analyse, et on y incorpore directement les noms de classe et de méthode, les arguments et les sorties.

Le programe réécrira ensuite les autres fichiers dans le package rpc.v2.
Pour ce faire, il parcourera les fichiers et remplacera ce qui doit l'être à l'aide des valeurs récupérées dans l'interface.
</p>
<h2>Utilisation</h2>
<code>rpc.Compilateur [-f chemin_vers_les_classes_avec_/] <nom_du_fichier_java_de_l_interface> <port></code>