# simple-springboot

使用 GitHub Actions 一键部署 Java 应用，可根据自身服务器配置修改相关文件🚀

## 构建流程

以下是构建流传的核心代码说明。

```yml
build:
  runs-on: ubuntu-latest

  steps:
    # 设置java环境
    - name: Set up JDK 17
      uses: actions/setup-java@v4

    # 构建jar包
    - name: Build with Maven
      run: mvn clean package -DskipTests

    # 上传构建包，提供给下一个任务使用
    - name: Upload JAR as artifact
      uses: actions/upload-artifact@v4

push-docker:
  runs-on: ubuntu-latest
  needs: build

  steps:
    # 下载构建好的jar包
    - name: Download JAR artifact
      uses: actions/download-artifact@v4

    # 登录 Docker Hub
    - name: Login to Docker Hub
      uses: docker/login-action@v3

    # 构建镜像并且推送
    - name: Build and push Docker image
      uses: docker/build-push-action@v6

deploy:
  runs-on: ubuntu-latest
  needs: push-docker

  steps:
    # 使用ssh登录服务器
    - name: SSH into Server and Deploy
      uses: appleboy/ssh-action@v1.2.1
      with:
        # 自定义脚本，停止容器 => 删除容器 => 删除镜像 => 启动容器
        script: |
          docker stop ${{ env.IMAGE_NAME }} 2>/dev/null
          docker rm ${{ env.IMAGE_NAME }} 2>/dev/null
          docker rmi ${{ env.IMAGE_FULL_NAME }} || true

          docker pull ${{ env.IMAGE_FULL_NAME }}
          docker run -d \
            --name ${{ env.IMAGE_NAME }} \
            --restart unless-stopped \
            -p 8080:8080 \
            ${{ env.IMAGE_FULL_NAME }}
```

## 环境变量

进入你的 GitHub 仓库 → Settings → Secrets and variables → Actions → New repository secret。

| Secret 名称        | 作用说明                                            |
| ------------------ | --------------------------------------------------- |
| SERVER_HOST        | 部署服务器的公网 IP                                 |
| SSH_USERNAME       | 登录服务器的 SSH 用户名，默认为 root                |
| SSH_PRIVATE_KEY    | 用于 SSH 登录服务器的私钥内容（需与服务器公钥配对） |
| DOCKERHUB_USERNAME | Docker Hub 账号用户名                               |
| DOCKERHUB_TOKEN    | Docker Hub 的访问令牌                               |
