pipeline {
    agent any  // 이 파이프라인이 실행될 Jenkins 에이전트를 지정합니다. 'any'는 사용 가능한 임의의 에이전트에서 실행될 수 있음을 의미합니다.
    tools { 
        nodejs 'nodejs-20.11.1'  // 'node'는 글로벌 도구 구성에서 설정한 Node.js의 이름
    }

    stages {
        stage("Build") {
            steps {
                sh 'node --version'
                sh 'npm --version'
                // sh "npm install"
                // sh "npm run build"
            }
        }
        
        stage('Deploy Redis') {
            steps {
                script {
                    echo "Redis 컨테이너 실행 상태 확인 중..."
                    def isRedisRunning = sh(script: "docker ps --filter name=nowdoboss_redis --filter status=running", returnStdout: true).trim()
                    echo "Redis 실행 상태: ${isRedisRunning}"

                    // Redis가 실행 중이지 않으면 실행
                    if (isRedisRunning == "") {
                        sh "docker-compose -f CICD/docker-compose-redis.yml up -d"
                    }
                }
            }
        }
        stage('Start SonarQube') {
            steps {
                script {
                    echo "SonarQube 컨테이너 실행 상태 확인 중..."
                    def isSonarQubeRunning = sh(script: "docker ps --filter name=nowdoboss_sonarqube --filter status=running", returnStdout: true).trim()
                    echo "SonarQube 실행 상태: ${isSonarQubeRunning}"

                    // SonarQube가 실행 중이지 않으면 실행
                    if (isSonarQubeRunning == "") {
                        sh "docker-compose -f CICD/docker-compose-sonarqube.yml up -d"
                    }
                }
            }
        }

        // stage('SonarQube Analysis - FrontEnd') {
        //     steps {
        //         dir('FrontEnd') {
        //             withSonarQubeEnv('SonarQube Server') {
        //                 sh 'sonar-scanner -Dsonar.projectKey=nowdoboss'
        //             }
        //         }
        //     }
        // }

        stage('SonarQube Analysis - SpringBootServer') {
            steps {
                dir('BackEnd/SpringBootServer') {
                    withSonarQubeEnv('SonarQube Server') {
                        sh 'chmod +x ./gradlew'
                        sh './gradlew sonar -Dsonar.projectKey=nowdoboss'
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {  // 'Deploy with Docker Compose'라는 이름의 단계를 정의합니다. 이 단계에서는 Docker Compose를 사용한 배포가 이루어집니다.
            steps {
                script {
                    // 이전 실행에서 사용된 컨테이너 및 네트워크 정리
                    sh "docker-compose down --volumes"  // 'docker-compose down --volumes' 명령을 실행하여, 이전에 실행되었던 모든 컨테이너를 종료하고 관련된 볼륨을 삭제합니다. 이는 환경을 깨끗하게 정리하여 다음 배포가 깔끔한 상태에서 이루어질 수 있도록 합니다.

                    // 새로운 푸시에 대한 스크립트 실행
                    sh "docker-compose up --build -d"  // 'docker-compose up --build -d' 명령을 실행하여, Docker Compose 파일에 정의된 모든 서비스를 빌드하고 백그라운드 모드로 실행합니다. '--build' 옵션은 이미지가 새로운 코드로 재빌드되도록 합니다.
                }
            }
        }

        // stage('SonarQube Analysis - ReactServer') {
        //     steps {
        //         dir('FrontEnd') {
        //             withSonarQubeEnv('SonarQube Server') {
        //                 sh 'npm run sonarqube'
        //             }
        //         }
        //     }
        // }
    }
}
