FROM postgres

ENV POSTGRES_PASSWORD=postgres_extensions
ENV POSTGRES_USER=postgres_extensions
ENV POSTGRES_DB=pg_extensions_test

RUN echo "CREATE EXTENSION hstore" >> /docker-entrypoint-initdb.d/hstore.sql