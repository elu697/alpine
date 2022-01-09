FROM nvidia/cuda:11.4.2-cudnn8-devel-ubuntu20.04

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
RUN aptitude install -y make libjsoncpp-dev libjsonrpccpp-dev libgmp-dev libreadline-dev libmicrohttpd-dev ocl-icd-libopencl1 \
  opencl-headers mesa-common-dev build-essential libboost-all-dev build-essential libsm6 libxext6 libxrender-dev libssl-dev \
  zlib1g-dev libbz2-dev libreadline-dev libsqlite3-dev wget curl llvm libncurses5-dev libncursesw5-dev xz-utils tk-dev \
  libffi-dev liblzma-dev python-openssl zlib1g-dev libffi-dev automake
RUN aptitude install -y sudo git net-tools unzip

# python3 for cefpyco
RUN aptitude install -y build-essential libssl-dev libffi-dev
RUN aptitude install -y python3-pip python3-dev

RUN pip3 uninstall tensorboard-plugin-wit tb-nightly \
  tensorboard tf-estimator-nightly tensorflow-gpu \
  tensorflow tf-nightly tensorflow_estimator -y
RUN pip3 install -U --user pip numpy wheel
RUN pip3 install -U --user keras_preprocessing --no-deps
RUN pip3 install nvidia-pyindex
RUN pip3 install nvidia-tensorflow


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
RUN sed -i 's|#\?PermitRootLogin prohibit-password|PermitRootLogin yes|' /etc/ssh/sshd_config
RUN sed -i 's|#AuthorizedKeysFile|AuthorizedKeysFile /etc/ssh/authorized_pub_keys|' /etc/ssh/sshd_config
RUN sed -i 's|#PasswordAuthetication|PasswordAuthetication|' /etc/ssh/sshd_config
RUN sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd
ARG MY_SSH_PUB_KEY="ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQCi7Oqd8eNG1vfY6P7PbA7PWDX2AM2KcnAuMxXDJ2SUyrm9J/ALvz3QVEHuNGQFNTWDXP4BXru6tJaJR9610SZugpSrdS1vayZ98f1R6e0Z2gj6YQkGxFiLW2N+Ip0hQ0XIrAfMMHdBIZnsUKoE2Tr7L28Ium5paI5Ui8F+gAdvl9L86O4pB9f4TYX58Hm5g82bP2Hs0d2prbfAZUF5OnPn1uc05mUNT6HR+jhQcfplmFXujArJGjLJGCQ4DbbRee0jCBOiPFbowjf5WuvBH6FkL+UQj4MrTIvzxAj8Y5CIC2LwbQy1ZgsXj5l2u/aI34y6sWaBBvpNAkLJVlfWJkQ46RzNkDZbJZpZRH/w0LfP62ZXKHHDBU0ybvURXQYjoSOg5EIXJGcCIvJRvxMKzbsLSb5gfUUkXL1tUleiJ+N1AzfoQiVfXWtfUmu1EGXPvPMa/yzVD0WBuVq1jwxZsO7+yYuvP5B46ZhdW9ZypL1n/tPlhKNh7SIfoAFqr3/UaeM= you@miraitomirai21.local"
RUN echo ${MY_SSH_PUB_KEY} >> /etc/ssh/authorized_pub_keys

# profile設定
ENV NOTVISIBLE "in users profile"
RUN echo "export VISIBLE=now" >> /etc/profile

# Env setup
# ENV GPU_FORCE_64BIT_PTR=0
# ENV GPU_MAX_HEAP_SIZE=100
# ENV GPU_USE_SYNC_OBJECTS=1
# ENV GPU_MAX_ALLOC_PERCENT=100
# ENV GPU_SINGLE_ALLOC_PERCENT=100

# SSHポート開放
EXPOSE 22

# SSH待機
CMD ["/usr/sbin/sshd", "-D"]

# Ceforeセットアップ Rootユーザ
RUN su docker && cd /home/docker && git clone -v https://github.com/elu697/alpine.git && chown -hR docker:docker alpine && cd alpine/setup/ndn
RUN git config --global user.email "elyou.dev@gmail.com"
RUN git config --global user.name "elu697"
