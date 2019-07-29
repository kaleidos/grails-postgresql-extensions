.PHONY: test

image :
	docker build -f Dockerfile -t grails/postgres-extensions .

clean :
	docker rmi -f grails/postgres-extensions

test :
	./gradlew clean
	docker run --rm --name pg_extensions_test -p 5432:5432 -d grails/postgres-extensions
	./gradlew check
	docker container kill pg_extensions_test

all : image test