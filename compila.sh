#!/bin/bash


ERR_JDK=1
ERR_JAVAC=2
ERR_CLASSPATH=3


JDK="/opt/jdk1.6.0_10"
JAVAC="$JDK/bin/javac"
CLASSPATH="`pwd`/bin"


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


if [ ! -e bin ]
then
	echo -n "Criando o diretorio dos binarios em './bin' "
	mkdir bin
	echo '[OK]'
fi


checa_existencia $JDK	    $ERR_JDK
checa_existencia $JAVAC     $ERR_JAVAC
checa_existencia $CLASSPATH $ERR_CLASSPATH


echo -n 'Compilando os fontes... '
$JAVAC -d ./bin ./src/*/*.java
echo '[OK]'

