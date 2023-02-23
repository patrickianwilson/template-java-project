import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.dockerSupport
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.DockerCommandStep
import jetbrains.buildServer.configs.kotlin.buildSteps.dockerCommand
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.buildSteps.GradleBuildStep
import jetbrains.buildServer.configs.kotlin.buildSteps.ScriptBuildStep


import jetbrains.buildServer.configs.kotlin.triggers.finishBuildTrigger
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.ui.add
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2021.2"

project {

    buildType(Build)
    buildType(IntTestDev)
    buildType(DeployDev)
    buildType(DeployProd)
}

object Build : BuildType({
    name = "Build"
    maxRunningBuilds = 5
    artifactRules = "+:build/**/* => build_artifacts,+:src/main/deploy/Dockerfile => build_artifacts"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        gradle {
            tasks = "release"
            useGradleWrapper = false
            dockerImagePlatform = GradleBuildStep.ImagePlatform.Linux
            dockerPull = true
            dockerImage = "inquest.registry.jetbrains.space/p/buildtools/buildimages/buildimage:latest"
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        dockerSupport {
            loginToRegistry = on {
                dockerRegistryId = "PROJECT_EXT_5"
            }
        }
    }
})

object DeployDev : BuildType({
    name = "Deploy to DEV"

    maxRunningBuilds = 1
    type = BuildTypeSettings.Type.DEPLOYMENT
    enablePersonalBuilds = false
    artifactRules = "+:build/**/* => build_artifacts,+:src/main/deploy/Dockerfile => build_artifacts"
    params {
        param("PATH", "${'$'}CONTAINER_PATH:${'$'}PATH")
    }
    steps {
        dockerCommand {
            name = "Build Docker Image"
            commandType = build {
                source = file {
                    path = "Dockerfile"
                }
                contextDir = "."
                platform = DockerCommandStep.ImagePlatform.Linux
                namesAndTags = """
                    module-service:%build.number%
                    inquest.registry.jetbrains.space/p/buildtools/services/mercury-service:1.0.0.%build.number%
                    inquest.registry.jetbrains.space/p/buildtools/services/mercury-service:latest
                """.trimIndent()
                commandArgs = "--pull"
            }
        }
        dockerCommand {
            name = "Push Docker Image"
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
            commandType = push {
                /* FIXME */
                namesAndTags = """
                   inquest.registry.jetbrains.space/p/buildtools/services/mercury-service:1.0.0.%build.number%
                    inquest.registry.jetbrains.space/p/buildtools/services/mercury-service:latest
                """.trimIndent()
            }
        }
        script {
            name = "Cassius Import"
            /* FIXME */
            scriptContent = """
                echo "Environment For Build Num: ${'$'}{BUILD_NUMBER}"
                env
                
                echo "Current Directory Contents"
                ls -al
                                    
                echo "Authenticate GCloud"
                gcloud auth activate-service-account --key-file ${'$'}GOOGLE_APPLICATION_CREDENTIALS
                                    
                echo "GCloud Tool Config"
                gcloud config list
                
                echo "Importing Image Into Cassius"
                cassius deployment-bundle import --deploymentBundleName mercury-service-devel --version=1.0.0.${'$'}BUILD_NUMBER --image=inquest.registry.jetbrains.space/p/buildtools/services/mercury-service:1.0.0.${'$'}BUILD_NUMBER
                
                echo "Deploying Version to Dev Application Environment"
                cassius environment deploy --deploymentBundleVersion=1.0.0.${'$'}BUILD_NUMBER --appId MercuryService --envName DEV
            """.trimIndent()
            dockerImage = "inquest.registry.jetbrains.space/p/buildtools/buildimages/buildimage:latest"
            dockerImagePlatform = ScriptBuildStep.ImagePlatform.Linux
            dockerPull = true
        }
    }

    triggers {
        finishBuildTrigger {
            buildType = "${Build.id}"
            successfulOnly = true
        }
    }
    features {
        dockerSupport {
            loginToRegistry = on {
                dockerRegistryId = "PROJECT_EXT_5"
            }
        }
    }

    dependencies {
        dependency(Build) {
            snapshot {
                onDependencyFailure = FailureAction.CANCEL
                onDependencyCancel = FailureAction.CANCEL
            }

            artifacts {
                artifactRules = "build_artifacts/**"
            }
        }
    }
})

object IntTestVcsRoot: GitVcsRoot({
    /* FIXME */

    id("SshGitGitJetbrainsSpaceInquestBuildtoolsModuleMercuryServiceIntegTestsGitRefsHeadsMaster")
    name = "ssh://git@git.jetbrains.space/inquest/buildtools/Module-MercuryServiceIntegTests.git#refs/heads/master"
    url = "ssh://git@git.jetbrains.space/inquest/buildtools/Module-MercuryServiceIntegTests.git"
    branch = "refs/heads/master"
    branchSpec = "refs/heads/*"
    authMethod = uploadedKey {
        userName = "git"
        uploadedKey = "SpaceSshKey"
    }
})

object IntTestDev: BuildType({

    name = "IntTestDev"
    artifactRules = "+:build/**/* => build_artifacts,+:src/main/deploy/Dockerfile => build_artifacts"
    vcs {
        /* FIXME */
        root(RelativeId("SshGitGitJetbrainsSpaceInquestBuildtoolsModuleMercuryServiceIntegTestsGitRefsHeadsMaster"))
    }

    steps {
        gradle {
            tasks = "intTest"
            useGradleWrapper = false
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
    }

    dependencies {
        dependency(DeployDev) {
            snapshot {
                onDependencyFailure = FailureAction.CANCEL
                onDependencyCancel = FailureAction.CANCEL
            }

            artifacts {
                artifactRules = "build_artifacts/**"
            }
        }
    }
})
object DeployProd : BuildType({
    name = "Deploy to Prod"

    maxRunningBuilds = 1
    type = BuildTypeSettings.Type.DEPLOYMENT
    enablePersonalBuilds = false
    params {
        param("PATH", "${'$'}CONTAINER_PATH:${'$'}PATH")
    }
    steps {
        script {
            name = "Cassius Deploy"
            /* FIXME */

            scriptContent = """
                echo "Environment For Build Num: ${'$'}{BUILD_NUMBER}"
                env
                
                echo "Current Directory Contents"
                ls -al
                                    
                echo "Authenticate GCloud"
                gcloud auth activate-service-account --key-file ${'$'}GOOGLE_APPLICATION_CREDENTIALS
                                    
                echo "GCloud Tool Config"
                gcloud config list
                
                echo "Deploying Version to Dev Application Environment"
                cassius environment deploy --deploymentBundleVersion=1.0.0.${'$'}BUILD_NUMBER --appId MercuryService --envName Prod
            """.trimIndent()
            dockerImage = "inquest.registry.jetbrains.space/p/buildtools/buildimages/buildimage:latest"
            dockerImagePlatform = ScriptBuildStep.ImagePlatform.Linux
            dockerPull = true
        }
    }

    triggers {
        finishBuildTrigger {
            buildType = "${Build.id}"
            successfulOnly = true
        }
    }
    features {
        dockerSupport {
            loginToRegistry = on {
                dockerRegistryId = "PROJECT_EXT_5"
            }
        }
    }

    dependencies {
        dependency(DeployDev) {
            snapshot {
                onDependencyFailure = FailureAction.CANCEL
                onDependencyCancel = FailureAction.CANCEL
            }

            artifacts {
                artifactRules = "build_artifacts/**"
            }
        }
    }
})
