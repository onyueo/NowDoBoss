# 우분투 베이스 이미지 사용
FROM ubuntu:latest

# 필수 패키지 설치
RUN apt-get update && apt-get upgrade -y
RUN apt-get install -y curl openssh-server rsync wget vim iputils-ping htop openjdk-8-jdk python3 python3-pip

# 하둡 다운로드 및 설치
RUN wget http://mirror.navercorp.com/apache/hadoop/common/hadoop-3.2.4/hadoop-3.2.4.tar.gz \
    && tar zxvf hadoop-3.2.4.tar.gz \
    && rm hadoop-3.2.4.tar.gz \
    && mv hadoop-3.2.4 /usr/local/hadoop

# Spark 다운로드 및 설치
RUN wget https://archive.apache.org/dist/spark/spark-3.2.1/spark-3.2.1-bin-hadoop3.2.tgz \
    && tar zxvf spark-3.2.1-bin-hadoop3.2.tgz \
    && rm spark-3.2.1-bin-hadoop3.2.tgz \
    && mv spark-3.2.1-bin-hadoop3.2 /usr/local/spark

# PySpark 및 필요 라이브러리 설치
RUN pip3 install pyspark

# 환경변수 설정
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
ENV HADOOP_HOME=/usr/local/hadoop
ENV SPARK_HOME=/usr/local/spark
ENV PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$JAVA_HOME/bin:$SPARK_HOME/bin:$SPARK_HOME/sbin

# 공통적으로 사용되는 하둡 설정 파일(.xml)과 및 쉘 스크립트 복사
COPY hadoop/common/core-site.xml $HADOOP_HOME/etc/hadoop/core-site.xml
COPY hadoop/common/mapred-stie.xml $HADOOP_HOME/etc/hadoop/mapred-site.xml
COPY hadoop/common/yarn-site.xml $HADOOP_HOME/etc/hadoop/yarn-site.xml
COPY hadoop/common/setup-hadoop.sh /usr/local/bin/setup-hadoop.sh
COPY hadoop/common/init-ssh-keys.sh /usr/local/bin/init-ssh-keys.sh
COPY hadoop/common/collect-ssh-keys.sh /usr/local/bin/collect-ssh-keys.sh
COPY hadoop/common/update-hosts.sh /usr/local/bin/update-hosts.sh

# 각 노드내에 스파크 설정 파일 및 스파크 관련 쉘 스크립트 복사
COPY spark/spark-env.sh $SPARK_HOME/conf/spark-env.sh
COPY spark/spark-defaults.conf $SPARK_HOME/conf/spark-defaults.conf
COPY spark/start-history-server.sh /usr/local/bin/start-history-server.sh

# 쉘 스크립트 실행 권한 부여 및 실행
RUN chmod +x /usr/local/bin/setup-hadoop.sh 
RUN chmod +x /usr/local/bin/init-ssh-keys.sh /usr/local/bin/collect-ssh-keys.sh 
RUN chmod +x /usr/local/bin/update-hosts.sh
RUN chmod +x /usr/local/bin/start-history-server.sh

# SSH 구성
RUN service ssh start

# 포트 설정
EXPOSE 9870 8088 19888 7077 8080 18080

# SSH 데몬 실행
CMD ["/usr/sbin/sshd", "-D"]
