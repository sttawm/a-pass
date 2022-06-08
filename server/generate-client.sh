json=/tmp/openapi-$RANDOM.json
curl -f "http://localhost:9000/assets/swagger.json" > $json && \
	echo "Using json file $json" && \
	#openapi-generator generate -i $json --generator-name scala-sttp -o client
        openapi-generator generate -i $json --generator-name scala-akka -o client --additional-properties=mainPackage=sttawm.client

# 'Fix' the build
gsed -i "s:^scalaVersion://scalaVersion:g" client/build.sbt
gsed -i "s:^crossScalaVersions://crossScalaVersions:g" client/build.sbt


