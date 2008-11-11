#!/bin/bash


ERR_JDK=1
ERR_JAVA=2
ERR_CLASSPATH=3


JDK="/opt/jdk1.6.0_10"
JAVA="$JDK/bin/java"
CLASSPATH="`pwd`/bin"


HOST="localhost"
PORTA="9999"


function checa_existencia {
    echo -n "Checando a existencia de '$1'..."
    if [ ! -e $1 ];
    then
        echo -e "\nErro: '$1' nao existe. Abortando a execucao."
        exit $2
    else
        echo " [OK]"
    fi
}


checa_existencia $JDK	    $ERR_JDK
checa_existencia $JAVA      $ERR_JAVA
checa_existencia $CLASSPATH $ERR_CLASSPATH


pushd $CLASSPATH >/dev/null 2>&1


echo "Inicializando o 'Cliente' conectando no host '$HOST' e na porta '$PORTA'"
$JAVA cliente.Cliente $HOST $PORTA


popd >/dev/null 2>&1

