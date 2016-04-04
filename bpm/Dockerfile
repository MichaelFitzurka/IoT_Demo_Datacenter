FROM    psteiner/base

MAINTAINER psteiner <psteiner@redhat.com>

USER root

ENV BPMHOME jboss-eap-6.4
ENV JBOSS_PATCH jboss-eap-6.4.6-patch.zip

# transfer JBoss products & config files
ADD Docker_Files/software/ $HOME/tmp/
ADD Docker_Files/config/ $HOME/tmp/
ADD Docker_Files/niogit $HOME/tmp/niogit/
ADD Docker_Files/repositories $HOME/tmp/repositories/
ADD Docker_Files/index $HOME/tmp/index/
ADD Docker_Files/15ad5bfa-7532-3eea-940a-abbbbc89f1e8.xml $HOME/tmp/
ADD LightWorkItemHandler/target/lightWorkItemHandler-1.0.0-SNAPSHOT.jar $HOME/tmp/

# let the user 'psteiner' own the files
RUN chown -R psteiner $HOME

# continue as 'psteiner'
USER psteiner

# unpack and install BPM
RUN unzip $HOME/tmp/jboss-eap-6.4.0.zip -d $HOME && \
    unzip -o $HOME/tmp/jboss-bpm* -d $HOME

# patch JBoss EAP
WORKDIR $HOME/jboss-eap-6.4
RUN ./bin/jboss-cli.sh "patch apply $HOME/tmp/$JBOSS_PATCH"

# copy configurations to their place
RUN mv $HOME/tmp/applic* $HOME/$BPMHOME/standalone/configuration  && \
    mv $HOME/tmp/kie-wb-deployment-descriptor.xml $HOME/$BPMHOME/standalone/deployments/business-central.war/WEB-INF/classes/META-INF

# copy process to the right place
RUN mv $HOME/tmp/niogit $HOME/.niogit  && \
    mv $HOME/tmp/repositories $HOME/repositories  && \
    mv $HOME/tmp/15ad5bfa-7532-3eea-940a-abbbbc89f1e8.xml $HOME/  && \
    mv $HOME/tmp/index $HOME/.index

# copy custom WorkItemHandler
RUN mkdir -p $HOME/.m2/repository/com/redhat/demo/iotdemo/lightWorkItemHandler/1.0.0-SNAPSHOT/ && \
    mv $HOME/tmp/lightWorkItemHandler-1.0.0-SNAPSHOT.jar $HOME/$BPMHOME/standalone/deployments/business-central.war/WEB-INF/lib/

# unpack and copy JBoss Data Grid modules
RUN unzip $HOME/tmp/jboss-datagrid-6.6.0-eap-modules-library.zip -d $HOME/tmp
RUN cp -R $HOME/tmp/jboss-datagrid-6.6.0-eap-modules-library/modules/* $HOME/$BPMHOME/modules

# cleanup
RUN rm -rf $HOME/tmp/jboss-*

WORKDIR $HOME

# open ports
EXPOSE 8080 9990

# ENTRYPOINT
CMD $HOME/$BPMHOME/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 -Dorg.kie.override.deploy.enabled=true


# Build command -> docker build --rm -t workspacefuse_iotdatacenterbpm .
# run command -> docker run -d -p 10000:8080 workspacefuse_iotdatacenterbpm
# run and connect -> docker run -p 10000:8080 -i -t workspacefuse_iotdatacenterbpm /bin/bash
