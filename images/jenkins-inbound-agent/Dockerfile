FROM jenkins/inbound-agent:4.10-3
USER root
RUN  apt-get update && apt install curl gnupg2 --yes \
        python3 \
        python3-pip \
        make \
        unzip \
        jq \
        pkg-config \
        wget \
        git \
        curl \
        sudo \
        rsync \
        dnsutils \
        iproute2 \
    && rm -rf /var/lib/apt/lists/*
RUN wget https://download.clis.cloud.ibm.com/ibm-cloud-cli/2.1.1/IBM_Cloud_CLI_2.1.1_amd64.tar.gz \
        && tar -xvzf "./IBM_Cloud_CLI_2.1.1_amd64.tar.gz" \
        &&  ./Bluemix_CLI/install \
        && ibmcloud update -f \
        && ibmcloud plugin install cloud-object-storage -f \
        && ibmcloud plugin install container-service -f \
        && ibmcloud plugin install power-iaas -f
RUN pip3 install --upgrade pip ; pip3 install python-openstackclient; pip3 install ansible
ENV PATH $GOPATH/bin:/usr/local/go/bin:$PATH
RUN ssh-keygen -t rsa -N "" -f ~/.ssh/id_rsa
ENV VERSION="0.8.0"
RUN wget https://github.com/containerd/nerdctl/releases/download/v$VERSION/nerdctl-$VERSION-linux-amd64.tar.gz
RUN sudo tar zxvf  nerdctl-$VERSION-linux-amd64.tar.gz -C  /usr/local/bin/
RUN rm -f nerdctl-$VERSION-linux-amd64.tar.gz
RUN sed -i '2 a ip link set dev eth0 mtu 1420' /usr/local/bin/jenkins-agent
