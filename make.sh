#!/bin/bash

if [ "$1" = "-debug" ]
then
	debug=$1
	shift
fi

mvni() {
	mvn -Ddependency-check.skip=true clean install -DskipTests -Dskip.test.db.update.verifier=true  -Dskip.odata.doc=true  -Dskip.docker.image.build=true -Dskip.docker.image.saveToFile=true
}

m=${1:-5}

for dir in test-logger test-jpa test-client test-rest test-ejb test-ear
do
	list=$(find "${dir}" -name \*.java -mmin -$m)
	echo "${list}"
	if [ $(echo ${list} | wc -w) -eq 0 ]; then continue ; fi
	if [ -z "${debug}" ]
	then
		echo "${list}" | cut -d/ -f1 | while read i ; do echo "###### Building $i ###########" ; cd $i ; mvni ; done
	fi
done

if [ -n "${debug}" ]
then
	exit
fi

cd test-ear
mvni
./redeploy.sh 
