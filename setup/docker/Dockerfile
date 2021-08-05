FROM ubuntu:18.04

# おまじない
ENV DEBIAN_FRONTEND=noninteractive

# locale 設定
RUN apt-get update
RUN apt-get upgrade -y
RUN apt-get full-upgrade -y
RUN apt-get autoremove -y
RUN apt-get install -y locales aptitude
RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

# 開発環境構築
RUN aptitude install -y openssh-server apt-utils nano
RUN aptitude install -y make build-essential libssl-dev zlib1g-dev libbz2-dev libreadline-dev libsqlite3-dev wget curl llvm libncurses5-dev libncursesw5-dev xz-utils tk-dev libffi-dev liblzma-dev python-openssl zlib1g-dev libffi-dev automake
RUN aptitude install -y sudo git net-tools unzip
# python3 for cefpyco
RUN aptitude install -y build-essential libssl-dev libffi-dev python3-dev
RUN aptitude install -y python3-pip

# ユーザーを作成
ARG ROOT_PASSWORD
RUN echo 'root:password' | chpasswd
ARG DOCKER_UID=1000
ARG DOCKER_USER=docker
ARG DOCKER_PASSWORD=password
　RUN useradd -s /bin/bash -m --uid ${DOCKER_UID} --groups sudo ${DOCKER_USER} \
  && echo ${DOCKER_USER}:${DOCKER_PASSWORD} | chpasswd

# SUDO 追加
RUN echo 'Defaults visiblepw'             >> /etc/sudoers
RUN echo '${DOCKER_USER} ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers

# SSH設定
RUN mkdir /var/run/sshd
# RUN sed -i 's/#\Port 22/Port 6970/' /etc/ssh/sshd_config
RUN sed -i 's/#\?PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config
RUN sed -i 's/#PasswordAuthetication/PasswordAuthetication/' /etc/ssh/sshd_config
RUN sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd

# profile設定
ENV NOTVISIBLE "in users profile"
RUN echo "export VISIBLE=now" >> /etc/profile

# SSHポート開放
EXPOSE 22

# SSH待機
CMD ["/usr/sbin/sshd", "-D"]

# Ceforeセットアップ Rootユーザ
RUN su docker && cd /home/docker && git clone -v https://github.com/elu697/alpine.git && chown -hR docker:docker alpine && cd alpine/setup/cefore && bash cefore_setup.sh
