decrypt:
	sops -d -i src/main/resources/application-local.yaml
	sops -d -i src/main/resources/application-dev.yaml

encrypt:
	sops -e -i src/main/resources/application-local.yaml
	sops -e -i src/main/resources/application-dev.yaml