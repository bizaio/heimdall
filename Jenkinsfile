
node {
	def server
	def buildInfo
	def rtMaven
	
	stage ('Clone Source and switch Branch') {
        git branch: env.BRANCH_NAME, credentialsId: 'bizabuilder', url: 'https://github.com/bizaio/heimdall.git'
    }
	
    stage ('Artifactory configuration') {
        server = Artifactory.server "artifactory"
        rtMaven = Artifactory.newMavenBuild()
        rtMaven.tool = "Maven3"
        rtMaven.deployer releaseRepo: 'libs-release-local', snapshotRepo: 'libs-snapshot-local', server: server
        rtMaven.resolver releaseRepo: 'libs-release', snapshotRepo: 'libs-snapshot', server: server
        buildInfo = Artifactory.newBuildInfo()
    }

    stage ('Test') {
        rtMaven.run pom: 'pom.xml', goals: 'clean test'
    }
        
    stage ('Install') {
        rtMaven.run pom: 'pom.xml', goals: 'install', buildInfo: buildInfo
    }
 
    stage ('Deploy') {
        rtMaven.deployer.deployArtifacts buildInfo
    }
        
    stage ('Publish build info') {
        server.publishBuildInfo buildInfo
    }
}