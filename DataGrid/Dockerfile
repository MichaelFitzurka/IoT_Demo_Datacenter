FROM    psteiner/base

MAINTAINER psteiner <psteiner@redhat.com>

ENV JDG_INSTALL_IMAGE jboss-datagrid-6.6.0-server.zip
ENV JDG_INSTALL_DIR jboss-datagrid-6.6.0-server
ENV JDG_LOCATION jdg

USER root

WORKDIR $HOME

# copy fuse distribution
ADD Docker_Files/software/$JDG_INSTALL_IMAGE $HOME/tmp/

# let the user 'psteiner' own the files
RUN chown -R psteiner:psteiner $HOME

# continue as 'psteiner'
USER psteiner

# extract
RUN unzip $HOME/tmp/$JDG_INSTALL_IMAGE
RUN mv $HOME/$JDG_INSTALL_DIR $HOME/$JDG_LOCATION

# remove some overhead
RUN rm -rf $HOME/tmp/$JDG_INSTALL_IMAGE

# creates user
RUN echo "admin=8d4c6fb95ba430e0fcf9536dc8efa2b9" >> $HOME/$JDG_LOCATION/standalone/configuration/mgmt-users.properties

# disable security for REST endpoint
RUN sed -i "s/rest-connector virtual-server=\"default-host\" cache-container=\"local\" security-domain=\"other\" auth-method=\"BASIC\"/rest-connector virtual-server=\"default-host\" cache-container=\"local\"/" $HOME/$JDG_LOCATION/standalone/configuration/standalone.xml

# open ports
EXPOSE 8080 9990 11222

# ENTRYPOINT $HOME/$FUSE_LOCATION/bin/fuse
CMD $HOME/$JDG_LOCATION//bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0

# Build command -> docker build --rm -t psteiner/jdg .
# run command -> docker run -d -p 11222:11222 psteiner/jdg
# run and connect -> docker run -p 11222:11222 -i -t psteiner/jdg /bin/bash
