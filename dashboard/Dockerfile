#######################################################################
#                                                                     #
# Creates a base Fedora image with JBoss Data Virtualization 6.0.0.GA #
#                                                                     #
#######################################################################

# Use the centos base image
FROM psteiner/base

MAINTAINER psteiner <psteiner@redhat.com>

USER root

# copy required files
ADD Docker_Files/software/jboss-dv-installer-6.1.0.redhat-3.jar $HOME/tmp/
ADD Docker_Files/config/standalone.xml $HOME/tmp/
ADD Docker_Files/config/Install* $HOME/tmp/
ADD Docker_Files/config/appl* $HOME/tmp/

RUN chown -R psteiner:psteiner $HOME/*

############################################
# Install JBoss Data Virtualization 6.1.0.GA
############################################
USER psteiner

RUN java -jar $HOME/tmp/jboss-dv-installer-6.1.0.redhat-3.jar $HOME/tmp/Install_Log.xml

RUN mv $HOME/tmp/appl* $HOME/DV-6.1.0/jboss-eap-6.3/standalone/configuration
RUN mv $HOME/tmp/standalone.xml $HOME/DV-6.1.0/jboss-eap-6.3/standalone/configuration
RUN curl -o $HOME/DV-6.1.0/jboss-eap-6.3/standalone/deployments/postgresql-9.3-1101.jdbc41.jar https://jdbc.postgresql.org/download/postgresql-9.3-1103.jdbc41.jar

# start.sh
RUN echo "#!/bin/sh"
RUN echo "echo JBoss Data Virtualization Start script" >> $HOME/run.sh
RUN echo "$HOME/DV-6.1.0/jboss-eap-6.3/bin/standalone.sh -c standalone.xml -b 0.0.0.0 -bmanagement 0.0.0.0" >> $HOME/run.sh
RUN chmod +x $HOME/run.sh

# Clean up
RUN rm -rf $HOME/tmp/*

EXPOSE 22 3306 5432 8080 9990 27017 9999

CMD /home/psteiner/run.sh

# docker build --rm -t workspacefuse_iotdatacenterdashboard .
# docker run -P -i -t workspacefuse_iotdatacenterdashboard /bin/bash
