FROM    psteiner/base

MAINTAINER psteiner <psteiner@redhat.com>

USER root

ENV BPMHOME jboss-eap-6.4

# transfer JBoss products & config files
ADD Docker_Files/software/ $HOME/tmp/
ADD Docker_Files/config/ $HOME/tmp/
ADD Docker_Files/niogit $HOME/tmp/niogit/
ADD Docker_Files/repositories $HOME/tmp/repositories/
ADD Docker_Files/index $HOME/tmp/index/
ADD Docker_Files/15ad5bfa-7532-3eea-940a-abbbbc89f1e8.xml $HOME/tmp/

# let the user 'psteiner' own the files
RUN chown -R psteiner:psteiner $HOME

# continue as 'psteiner'
USER psteiner

# unpack and install BPM
RUN unzip $HOME/tmp/jboss-eap* -d $HOME
RUN unzip -o $HOME/tmp/jboss-bpm* -d $HOME

# copy configurations to their place
RUN mv $HOME/tmp/applic* $HOME/$BPMHOME/standalone/configuration

# copy out process to the right place
RUN mv $HOME/tmp/niogit $HOME/.niogit
RUN mv $HOME/tmp/repositories $HOME/repositories
RUN mv $HOME/tmp/15ad5bfa-7532-3eea-940a-abbbbc89f1e8.xml $HOME/
RUN mv $HOME/tmp/index $HOME/.index

# cleanup
RUN rm $HOME/tmp/jboss-*

# open ports
EXPOSE 8080 9990

WORKDIR $HOME

# ENTRYPOINT
CMD $HOME/$BPMHOME/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 -Dorg.kie.override.deploy.enabled=true


# Build command -> docker build --rm -t workspacefuse_iotdatacenterbpm .
# run command -> docker run -d -p 10000:8080 workspacefuse_iotdatacenterbpm
# run and connect -> docker run -p 10000:8080 -i -t workspacefuse_iotdatacenterbpm /bin/bash
