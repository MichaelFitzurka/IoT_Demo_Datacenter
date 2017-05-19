# DOCKER-VERSION 0.3.4
FROM    psteiner/base

LABEL maintainer "psteiner <psteiner@redhat.com>"

ENV JAVA_HOME /usr/lib/jvm/jre \
    PGDATA /var/lib/pgsql/9.3/data \
    PGINST /usr/pgsql-9.3 \
    HOME /home/postgres

USER root

############################################################################
# Install PostgreSQL 9.3
#
# Code taken from: https://github.com/DataVirtualizationByExample/DVWorkshop
###########################################################################
RUN yum -y install http://yum.postgresql.org/9.3/redhat/rhel-6-x86_64/pgdg-redhat93-9.3-3.noarch.rpm  && \
    yum -y install postgresql93-server postgresql93-contrib phpPgAdmin sudo; yum clean all && \
    service postgresql-9.3 initdb

############################################
# Copy artifacts needed for the Heise_Demo
############################################
ADD . $HOME

############################################
# lets give the user postgres a HOME-DIR
############################################
RUN usermod -d $HOME postgres

#############################################
# Create database for BPM-Suite BAM usage
#############################################
USER postgres
WORKDIR /tmp

RUN $PGINST/bin/pg_ctl start -w -D $PGDATA && \
   $PGINST/bin/psql --command "ALTER USER postgres PASSWORD 'change12_me';" && \
   $PGINST/bin/psql --command "CREATE USER psteiner PASSWORD 'change12_me';" && \
   $PGINST/bin/psql --command "CREATE DATABASE demo OWNER psteiner;"


USER root

############################################
# Copy artifacts to where they belong
############################################
RUN mv $HOME/pgpass /root/.pgpass && \
   chmod 0600 /root/.pgpass && \
   chmod +x $HOME/postgresql-populate.sh  && \
   mv $HOME/pg_hba.conf $PGDATA  && \
   chmod +r $PGDATA/pg_hba.conf && \
   mv $HOME/phpPgAdmin.conf /etc/httpd/conf.d/

# Adjust PostgreSQL configuration so that remote connections to the
# database are possible.
RUN echo "listen_addresses='*'" >> $PGDATA/postgresql.conf
RUN echo "" >> $PGDATA/postgresql.conf

EXPOSE 5432 80

USER root

CMD $HOME/postgresql-populate.sh

# Build command -> docker build --rm -t workspacefuse_postgres .
# run command -> docker run   -p 5432:5432 -p 80:80 -d workspacefuse_postgres
