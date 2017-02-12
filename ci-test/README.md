ci-test
-----------
用于测试gitlab的集成测试

# 1. 安装ci</br>

    # For Debian/Ubuntu
    $ curl -L https://packages.gitlab.com/install/repositories/runner/gitlab-ci-multi-runner/script.deb.sh | sudo bash
    $ sudo apt-get install gitlab-ci-multi-runner

    # For CentOS
    $ curl -L https://packages.gitlab.com/install/repositories/runner/gitlab-ci-multi-runner/script.rpm.sh | sudo bash
    $ sudo yum install gitlab-ci-multi-runner


# 2. 注册 Runner</br>

    安装好 GitLab Runner 之后，我们只要启动 Runner 然后和 CI 绑定就可以了：
    打开你 GitLab 中的项目页面，在项目设置中找到 runners
    运行 sudo gitlab-ci-multi-runner register
    输入 CI URL
    输入 Token
    输入 Runner 的名字
    选择 Runner 的类型，简单起见还是选 Shell 吧
    完成

    当注册好 Runner 之后，可以用 sudo gitlab-ci-multi-runner list 命令来查看各个 Runner 的状态：
    $ sudo gitlab-runner list
    Listing configured runners          ConfigFile=/etc/gitlab-runner/config.toml
    my-runner                           Executor=shell Token=cd1cd7cf243afb47094677855aacd3 URL=http://mygitlab.com/ci
    
# 3. .gitlab-ci.yml
    配置好 Runner 之后，我们要做的事情就是在项目根目录中添加 .gitlab-ci.yml 文件了。当我们添加了 .gitlab-ci.yml 文件后，每次提交代码或者合并 MR 都会自动运行构建任务了。
    还记得 Pipeline 是怎么触发的吗？Pipeline 也是通过提交代码或者合并 MR 来触发的！那么 Pipeline 和 .gitlab-ci.yml 有什么关系呢？
    其实 .gitlab-ci.yml 就是在定义 Pipeline 而已拉！
    
    基本写法

    我们先来看看 .gitlab-ci.yml 是怎么写的：

    # 定义 stages
    stages:
        - build
        - test

    # 定义 job
    job1:
        stage: test
        script:
        - echo "I am job1"
        - echo "I am in test stage"

    # 定义 job
    job2:
        stage: build
        script:
            - echo "I am job2"
            - echo "I am in build stage"

    写起来很简单吧！用 stages 关键字来定义 Pipeline 中的各个构建阶段，然后用一些非关键字来定义 jobs。

    每个 job 中可以可以再用 stage 关键字来指定该 job 对应哪个 stage。job 里面的 script 关键字是最关键的地方了，也是每个 job 中必须要包含的，它表示每个 job 要执行的命令。

    回想一下我们之前提到的 Stages 和 Jobs 的关系，然后猜猜上面例子的运行结果？

    I am job2
    I am in build stage
    I am job1
    I am in test stage

    根据我们在 stages 中的定义，build 阶段要在 test 阶段之前运行，所以 stage:build 的 jobs 会先运行，之后才会运行 stage:test 的 jobs。
    常用的关键字

    下面介绍一些常用的关键字，想要更加详尽的内容请前往 官方文档
    stages

    定义 Stages，默认有三个 Stages，分别是 build, test, deploy。
    types

    stages 的别名。
    before_script

    定义任何 Jobs 运行前都会执行的命令。
    after_script

        要求 GitLab 8.7+ 和 GitLab Runner 1.2+

    定义任何 Jobs 运行完后都会执行的命令。
    variables && Job.variables

        要求 GitLab Runner 0.5.0+

    定义环境变量。如果定义了 Job 级别的环境变量的话，该 Job 会优先使用 Job 级别的环境变量。
    cache && Job.cache

        要求 GitLab Runner 0.7.0+

    定义需要缓存的文件。每个 Job 开始的时候，Runner 都会删掉 .gitignore 里面的文件。如果有些文件 (如 node_modules/) 需要多个 Jobs 共用的话，我们只能让每个 Job 都先执行一遍 npm install。

    这样很不方便，因此我们需要对这些文件进行缓存。缓存了的文件除了可以跨 Jobs 使用外，还可以跨 Pipeline 使用。

    具体用法请查看 官方文档。
    Job.script

    定义 Job 要运行的命令，必填项。
    Job.stage

    定义 Job 的 stage，默认为 test。
    Job.artifacts

    定义 Job 中生成的附件。当该 Job 运行成功后，生成的文件可以作为附件 (如生成的二进制文件) 保留下来，打包发送到 GitLab，之后我们可以在 GitLab 的项目页面下下载该附件。

    注意，不要把 artifacts 和 cache 混淆了。
    实用例子

    下面给出一个我自己在用的例子：

    stages:
        - install_deps
        - test
        - build
        - deploy_test
        - deploy_production

    cache:
        key: ${CI_BUILD_REF_NAME}
        paths:
            - node_modules/
            - dist/


    # 安装依赖
    install_deps:
      stage: install_deps
      only:
        - develop
        - master
      script:
        - npm install


    # 运行测试用例
    test:
      stage: test
      only:
        - develop
        - master
      script:
        - npm run test


    # 编译
    build:
      stage: build
      only:
        - develop
        - master
      script:
        - npm run clean
        - npm run build:client
        - npm run build:server


    # 部署测试服务器
    deploy_test:
      stage: deploy_test
      only:
        - develop
      script:
        - pm2 delete app || true
        - pm2 start app.js --name app


    # 部署生产服务器
    deploy_production:
      stage: deploy_production
      only:
        - master
      script:
        - bash scripts/deploy/deploy.sh

    上面的配置把一次 Pipeline 分成五个阶段：
        安装依赖(install_deps)
        运行测试(test)
        编译(build)
        部署测试服务器(deploy_test)
        部署生产服务器(deploy_production)
    
    设置 Job.only 后，只有当 develop 分支和 master 分支有提交的时候才会触发相关的 Jobs。
    注意，我这里用 GitLab Runner 所在的服务器作为测试服务器。