def call(){
    script {
        ansiColor('xterm') {
            echo ""
        }
        try {
            sh '''
                echo 'UI E2E Tests'
		cd ${WORKSPACE}/deploy
		if [[ $(systemctl is-active docker) == "inactive" ]]; then
		  sudo systemctl daemon-reload
                  sudo systemctl start docker
		fi
		wget http://pokgsa.ibm.com/gsa/pokgsa/home/h/r/hrmymt/web/public/console-built-${CONSOLE_BUILT_VERSION}.tgz
		mv console-built-${CONSOLE_BUILT_VERSION}.tgz console-built.tgz
		scp -i id_rsa -o StrictHostKeyChecking=no root@${BASTION_IP}:~/openstack-upi/auth/kubeadmin-password .
		kube_password=$(cat kubeadmin-password) 
                oc_server_url=$(make $TARGET:output TERRAFORM_OUTPUT_VAR=oc_server_url)
		cat > input.json << EOF
			{
			"apiurl": "${oc_server_url}",
			"password": "${kube_password}",
			"driver": "",
			"suite": "e2e",
			"browser": "firefox",
			"jtimeout": 240000,
			"failfast": "no"
			}
		EOF

		docker run -it --name UiE2e --net host --shm-size 3g -v $PWD:/host quay.io/miyamoto_h/ocp-console-e2e:${CONSOLE_IMAGE_VERSION} sh -c "cd / && ./console-e2e.sh | tee ui-e2e-output.txt"
		docker cp UiE2e:/ui-e2e-output.txt .
            '''
        }
        catch (err) {
            echo 'Error ! UI e2e tests failed!'
            env.FAILED_STAGE=env.STAGE_NAME
            throw err
        }
    }
}


