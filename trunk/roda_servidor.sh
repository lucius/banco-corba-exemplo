#!/bin/bash


ERR_JDK=1
ERR_ORBD=2
ERR_JAVA=3
ERR_CLASSPATH=4


JDK="/opt/jdk1.6.0_10"
ORBD="$JDK/bin/orbd"
JAVA="$JDK/bin/java"
CLASSPATH="`pwd`/bin"


HOST="localhost"
PORTA="9999"


function checa_existencia {
    echo -n "Checando a existencia de '$1'..."
    if [ ! -e $1 ]
    then
        echo -e "\nErro: '$1' nao existe. Abortando a execucao."
        exit $2
    else
        echo -e " [OK]"
    fi
}


function limpa_orb {
    ps -p $ORBD_PID >/dev/null 2>&1
    if [ $? ]
    then
        echo "Matando o processo 'orbd' numero $ORBD_PID..."
	kill -s SIGKILL $ORBD_PID >/dev/null 2>&1
    fi

    ORB_BD="$CLASSPATH/orb.db"
    if [ -e $ORB_BD ]
    then
        echo "Removendo o banco de dados '$ORB_BD' gerado..."
	rm -rf $ORB_BD >/dev/null 2>&1
    fi
}


checa_existencia $JDK	    $ERR_JDK
checa_existencia $ORBD	    $ERR_ORBD
checa_existencia $JAVA      $ERR_JAVA
checa_existencia $CLASSPATH $ERR_CLASSPATH


echo "Inicializando, em background, o 'orbd' como '$HOST' na porta '$PORTA'"
pushd $CLASSPATH >/dev/null 2>&1
$ORBD -ORBInitialHost $HOST -ORBInitialPort $PORTA &
ORBD_PID=$!
trap limpa_orb SIGINT


sleep 1


echo "Inicializando o 'Servidor' como '$HOST' na porta '$PORTA'"
$JAVA servidor.Servidor $HOST $PORTA


if [ ! $? ]
then
    limpa_orb
fi


popd >/dev/null 2>&1

